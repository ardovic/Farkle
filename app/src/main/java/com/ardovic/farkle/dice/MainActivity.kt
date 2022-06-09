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
import com.ardovic.farkle.dice.game.*
import com.ardovic.farkle.dice.graphics.Button
import javax.microedition.khronos.opengles.GL10

class MainActivity : AppCompatActivity(), FrameDrawer {

    lateinit var game: Game

    private val renderRect: Rect = Rect()
    private val deviceRect: Rect = Rect()

    private var touchCoordinate: Coordinate? = null

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
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_MOVE -> {}
            MotionEvent.ACTION_UP -> {
                touchCoordinate = convertTouchToRealCoordinate(touchX, touchY)
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


        renderer.batchDraw(gl) // Called each time a layer is needed
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
}
