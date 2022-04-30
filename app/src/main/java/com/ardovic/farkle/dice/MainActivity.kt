package com.ardovic.farkle.dice

import android.os.Bundle
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.ardovic.farkle.dice.delegates.SystemDelegate
import com.ardovic.farkle.dice.game.Cup
import com.ardovic.farkle.dice.game.Dice
import com.ardovic.farkle.dice.game.GameObject
import com.ardovic.farkle.dice.graphics.Graphics
import com.ardovic.farkle.dice.opengl.Drawer
import com.ardovic.farkle.dice.opengl.Renderer
import javax.microedition.khronos.opengles.GL10
import kotlin.math.absoluteValue
import kotlin.random.Random

class MainActivity : AppCompatActivity(), Drawer {

    private lateinit var bgView: ImageView
    private lateinit var rootLayout: FrameLayout

    private var buffers: Array<MutableList<GameObject>> = Array(C.BUFFER_COUNT) { ArrayList() }

    private val cup = Cup()
    private val dices = Array(5) { Dice() }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.GameTheme)
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(null)

        rootLayout = FrameLayout(this)
        bgView = SystemDelegate.onCreateBgViewReturn(this, rootLayout)



        cup.x = C.deviceWidth - cup.w
        cup.y = C.deviceHeight - cup.h
        cup.stopMovement()

    }

    val random = Random.Default

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dices.forEach {
                    it.x = cup.x + cup.w / 2
                    it.y = cup.y + cup.h / 2
                    it.state = Graphics.stableStates.random()
                    it.image = Graphics.matrix[it.state.first][it.state.second]
                    it.finishState = Graphics.stableStates.random()
                    it.stopMovement()
                }
            }
            MotionEvent.ACTION_MOVE -> {


            }
            MotionEvent.ACTION_UP -> {

                dices.forEach {
                    it.dx = random.nextInt(-100, -50)
                    it.dy = random.nextInt(-120, -60)
                    it.dr = random.nextInt(-10, 10)
                }

            }
        }




        return true
    }

    private fun update() {
        dices.forEach {
            it.realisticMovement()
        }

        val handled = mutableListOf<Dice>()

        if (dices.any { it.dx.absoluteValue < 30 }) {

            dices.forEach { reference ->

                dices.forEach {
                    if (it != reference && it.collidesWith(reference) && !handled.contains(it)) {

                        if(it.dx > 0) {
                            it.dx += 1
                        } else if (it.dx < 0){
                            it.dx -= 1
                        }


                        if(it.dy > 0) {
                            it.dy += 1
                        } else if (it.dy < 0){
                            it.dy -= 1
                        }

                        handled.add(it);

                    }
                }
            }
        }
    }

    private fun copyToBuffer() {
        buffers.forEach {
            it.clear()
        }
        buffers[0].addAll(dices)
        buffers[1].add(cup)

    }

    override fun onDrawFrame(gl: GL10, renderer: Renderer) {
        update()
        copyToBuffer()
        buffers.forEach { list ->
            list.forEach { obj ->
                obj.draw(gl, renderer)
            }
            renderer.batchDraw(gl)
        }
    }
}