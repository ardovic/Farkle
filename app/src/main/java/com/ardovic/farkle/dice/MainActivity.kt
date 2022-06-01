package com.ardovic.farkle.dice

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ardovic.farkle.dice.delegates.SystemDelegate
import com.ardovic.farkle.dice.engine.opengl.FrameDrawer
import com.ardovic.farkle.dice.engine.opengl.Renderer
import com.ardovic.farkle.dice.game.Game
import com.ardovic.farkle.dice.game.Command
import com.ardovic.farkle.dice.graphics.Button
import javax.microedition.khronos.opengles.GL10

class MainActivity : AppCompatActivity(), FrameDrawer {

    private var setupComplete: Boolean = false

    lateinit var buttons: List<Button>
    lateinit var game: Game

    private val renderRect = Rect()
    private val playerRect: Rect = Rect()
    private val deviceRect: Rect = Rect()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.GameTheme)
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(null)

        val rootLayout = FrameLayout(this)
        SystemDelegate.onCreateBgViewReturn(this, rootLayout)

        game = Game()


        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { _, insets ->
            if (!setupComplete) {

                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                C.statusBarHeight = systemBars.top
                C.navBarHeight = systemBars.bottom

                buttons = listOf(
                    Button(Button.Type.CCW),
                    Button(Button.Type.CW),
                    Button(Button.Type.GAS),
                    Button(Button.Type.BREAK)
                )

                playerRect.apply {
                    left = C.deviceCenterX - 100
                    top = C.deviceCenterY - 100
                    right = C.deviceCenterX + 100
                    bottom = C.deviceCenterY + 100
                }

                setupComplete = true
            }
            insets
        }
    }

    private var playerCommands: List<Command> = emptyList()

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val touchX = event.x.toInt()
        val touchY = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                playerCommands = buttons.firstOrNull { it.rect.contains(touchX, touchY) }
                    ?.let {
                        when (it.type) {
                            Button.Type.BREAK -> listOf(Command.DECELERATE)
                            Button.Type.GAS -> listOf(Command.ACCELERATE)
                            Button.Type.CW -> listOf(Command.ROTATE_CW, Command.ACCELERATE)
                            Button.Type.CCW -> listOf(Command.ROTATE_CCW, Command.ACCELERATE)
                        }
                    } ?: emptyList()

            }
            MotionEvent.ACTION_MOVE -> {}
            MotionEvent.ACTION_UP -> {
                playerCommands = emptyList()
            }
        }

        return true
    }

    private fun update() {
        game.player.commands = playerCommands
        game.update()
    }

    override fun onDrawFrame(gl: GL10, renderer: Renderer) {
        update()

        deviceRect.apply {
            left = (game.player.x - C.deviceCenterX).toInt()
            top = (game.player.y - C.deviceCenterY).toInt()
            right = (game.player.x + C.deviceCenterX).toInt()
            bottom = (game.player.y + C.deviceCenterY).toInt()
        }

        game.entities.forEach { entity ->
            if (entity.rect.intersect(deviceRect)) {

                // Assume 0, 0 Player -> Need to draw in center of screen

                val offsetX = (game.player.x - entity.x).toInt()
                val offsetY = (game.player.y - entity.y).toInt()

                renderRect.left = C.deviceCenterX - entity.radius - offsetX
                renderRect.top = C.deviceCenterY - entity.radius - offsetY
                renderRect.right = C.deviceCenterX + entity.radius - offsetX
                renderRect.bottom = C.deviceCenterY + entity.radius - offsetY

                entity.image?.let { renderer.draw(it, renderRect, entity.r) }

            }
        }

        buttons.forEach { it.draw(renderer) }

        renderer.batchDraw(gl) // Called each time a layer is needed
    }
}
