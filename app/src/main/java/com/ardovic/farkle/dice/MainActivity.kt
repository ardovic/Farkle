package com.ardovic.farkle.dice

import android.graphics.Rect
import android.os.Bundle
import android.view.InputEvent
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ardovic.farkle.dice.delegates.SystemDelegate
import com.ardovic.farkle.dice.engine.opengl.FrameDrawer
import com.ardovic.farkle.dice.engine.opengl.Renderer
import com.ardovic.farkle.dice.game.Command
import com.ardovic.farkle.dice.game.Coordinate
import com.ardovic.farkle.dice.game.Game
import com.ardovic.farkle.dice.game.Spaceship
import com.ardovic.farkle.dice.game.task.*
import com.ardovic.farkle.dice.graphics.Button
import javax.microedition.khronos.opengles.GL10

class MainActivity : AppCompatActivity(), FrameDrawer {

    lateinit var game: Game

    private var gameState: GameState = GameState.RoamState.MacroControlsState // Default state

    private val renderRect: Rect = Rect()
    private val deviceRect: Rect = Rect()

    private var touchCoordinate: Coordinate? = null
    private var touchedButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.GameTheme)
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(null)

        val rootLayout = FrameLayout(this)
        SystemDelegate.onCreateBgViewReturn(this, rootLayout)

        game = Game()

        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            C.statusBarHeight = systemBars.top
            C.navBarHeight = systemBars.bottom
            insets
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val touchX = event.x.toInt()
        val touchY = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                when (gameState) {
                    is GameState.RoamState -> {
                        when (gameState) {
                            GameState.RoamState.MicroControlsState -> {
                                Button.microControlButtons.firstOrNull { it.rect.contains(touchX, touchY) }
                                    ?.let { button ->
                                        touchedButton = button
                                        if (button.type != Button.Type.MACRO_CONTROLS) {
                                            game.player.tasks.clear()
                                            when (button.type) {
                                                Button.Type.TURN_CCW -> game.player.tasks.add(TurnCWWTask)
                                                Button.Type.TURN_CW -> game.player.tasks.add(TurnCWTask)
                                                Button.Type.BREAK -> game.player.tasks.add(DecelerateTask)
                                                Button.Type.ACCELERATE -> game.player.tasks.add(AccelerateTask)
                                                else -> {}
                                            }
                                        }
                                    }

                            }
                            GameState.RoamState.MacroControlsState -> {
                                touchedButton = Button.macroControlButtons.firstOrNull { it.rect.contains(touchX, touchY) }
                            }
                        }
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {}
            MotionEvent.ACTION_UP -> {
                touchedButton?.let {
                    if (Button.microControlButtons.contains(it) && it.type != Button.Type.MACRO_CONTROLS) {
                        game.player.tasks.clear()
                        game.player.tasks.add(InertiaTask)
                    }
                    if (it.rect.contains(touchX, touchY)) {
                        when (it.type) {
                            Button.Type.SINGLE_TAP -> {}
                            Button.Type.DOUBLE_TAP -> {}
                            Button.Type.MICRO_CONTROLS -> {
                                gameState = GameState.RoamState.MicroControlsState
                            }
                            Button.Type.MACRO_CONTROLS -> {
                                gameState = GameState.RoamState.MacroControlsState
                            }
                        }
                    }
                } ?: run {
                    game.player.tasks.clear()
                    touchCoordinate = convertTouchToRealCoordinate(touchX, touchY)
                }
                touchedButton = null
            }
        }

        return true
    }

    private fun convertTouchToRealCoordinate(touchX: Int, touchY: Int): Coordinate {
        val offsetX = C.deviceCenterX - touchX
        val offsetY = C.deviceCenterY - touchY

        return Coordinate(
            x = game.player.x - offsetX,
            y = game.player.y - offsetY
        )
    }


    private fun update() {
        game.update(touchCoordinate)
        touchCoordinate = null
    }

    override fun onDrawFrame(gl: GL10, renderer: Renderer) {
        update()

        when (gameState) {
            is GameState.RoamState -> {
                renderRoamState(renderer)
                renderer.batchDraw(gl) // Called each time a layer is needed
                when (gameState) {
                    GameState.RoamState.MicroControlsState -> {
                        Button.microControlButtons.forEach { button ->
                            updateRenderRect(button.rect)
                            button.draw(renderer)
                        }
                    }
                    GameState.RoamState.MacroControlsState -> {
                        Button.macroControlButtons.forEach { button ->
                            updateRenderRect(button.rect)
                            button.draw(renderer)
                        }
                    }
                }
                renderer.batchDraw(gl) // Called each time a layer is needed
            }
        }
    }

    private fun renderRoamState(renderer: Renderer) {
        deviceRect.apply {
            left = (game.player.x - C.deviceCenterX)
            top = (game.player.y - C.deviceCenterY)
            right = (game.player.x + C.deviceCenterX)
            bottom = (game.player.y + C.deviceCenterY)
        }


        game.entities.forEach { entity ->
            if (entity.rect.intersect(deviceRect)) {

                // Assume 0, 0 Player -> Need to draw in center of screen

                updateRenderRect(entity.x, entity.y, entity.radius)

                entity.image?.let { renderer.draw(it, renderRect, entity.r) }

                if (entity is Spaceship) {
                    entity.image?.let {

                        updateRenderRect(entity.rightCenterX, entity.rightCenterY, 10)

                        renderer.draw(it, renderRect, entity.r)

                        updateRenderRect(entity.leftCenterX, entity.leftCenterY, 10)

                        renderer.draw(it, renderRect, entity.r)
                    }
                }

            }
        }
    }

    /**
     * Call with x and y relative to player being at 0, 0
     */
    private fun updateRenderRect(x: Int, y: Int, radius: Int) {
        val offsetX = (game.player.x - x)
        val offsetY = (game.player.y - y)

        renderRect.left = C.deviceCenterX - radius - offsetX
        renderRect.top = C.deviceCenterY - radius - offsetY
        renderRect.right = C.deviceCenterX + radius - offsetX
        renderRect.bottom = C.deviceCenterY + radius - offsetY
    }

    private fun updateRenderRect(left: Int, top: Int, right: Int, bottom: Int) {
        renderRect.left = left
        renderRect.top = top
        renderRect.right = right
        renderRect.bottom = bottom
    }

    private fun updateRenderRect(rect: Rect) {
        updateRenderRect(rect.left, rect.top, rect.right, rect.bottom)
    }
}
