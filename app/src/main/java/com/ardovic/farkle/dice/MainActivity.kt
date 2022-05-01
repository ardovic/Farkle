package com.ardovic.farkle.dice

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ardovic.farkle.dice.MainActivity.State.*
import com.ardovic.farkle.dice.delegates.SystemDelegate
import com.ardovic.farkle.dice.game.Button
import com.ardovic.farkle.dice.game.Button.*
import com.ardovic.farkle.dice.game.Button.Id.*
import com.ardovic.farkle.dice.game.GameObject
import com.ardovic.farkle.dice.game.Sands
import com.ardovic.farkle.dice.game.Tank
import com.ardovic.farkle.dice.graphics.Graphics
import com.ardovic.farkle.dice.opengl.Drawer
import com.ardovic.farkle.dice.opengl.Renderer
import javax.microedition.khronos.opengles.GL10
import kotlin.random.Random

class MainActivity : AppCompatActivity(), Drawer {

    private lateinit var rootLayout: FrameLayout

    private var buffers: Array<MutableList<GameObject>> = Array(C.BUFFER_COUNT) { ArrayList() }


    lateinit var sands: Sands
    lateinit var tank: Tank
    lateinit var allButtons: Map<Id, Button>
    val visibleButtons: MutableList<Button> = mutableListOf()
    var activeButton: Button? = null


    var currentState = MID

    enum class State {
        MID,
        LEFT,
        RIGHT,
        MID_TO_RIGHT,
        MID_TO_LEFT,
        LEFT_TO_MID,
        RIGHT_TO_MID,
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.GameTheme)
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(null)

        rootLayout = FrameLayout(this)
        SystemDelegate.onCreateBgViewReturn(this, rootLayout)

        sands = Sands()
        tank = Tank()
        allButtons = hashMapOf(
            GO_LEFT to Button(GO_LEFT),
            GO_MID to Button(GO_MID),
            GO_RIGHT to Button(GO_RIGHT)
        )

        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            println(Log.d("HEX", "${systemBars.bottom}, ${systemBars.top}"))

            return@setOnApplyWindowInsetsListener insets
        }


    }

    val random = Random.Default

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val touchX = event.x.toInt()
        val touchY = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                activeButton = visibleButtons.find { it.matches(touchX, touchY) }
                activeButton?.apply { color = Graphics.COLOR_SEMI_TRANSPARENT }
            }
            MotionEvent.ACTION_MOVE -> {


            }
            MotionEvent.ACTION_UP -> {
                activeButton?.let {
                    when (it.id) {
                        GO_LEFT -> {
                            currentState = MID_TO_LEFT
                            transitionFrames = 40
                        }
                        GO_MID -> {
                            if (currentState == LEFT) {
                                currentState = LEFT_TO_MID
                            }
                            if (currentState == RIGHT) {
                                currentState = RIGHT_TO_MID
                            }
                            transitionFrames = 40
                        }
                        GO_RIGHT -> {
                            currentState = MID_TO_RIGHT
                            transitionFrames = 40
                        }
                    }
                }
                activeButton?.apply { color = Graphics.COLOR_ORIGINAL }
            }
        }

        return true
    }

    private var transitionFrames = 0

    private fun update() {
        sands.update()

        visibleButtons.clear()
        when (currentState) {
            MID -> {
                visibleButtons.add(allButtons[GO_LEFT]!!)
                visibleButtons.add(allButtons[GO_RIGHT]!!)
            }
            RIGHT -> {
                visibleButtons.add(allButtons[GO_MID]!!)
            }
            LEFT -> {
                visibleButtons.add(allButtons[GO_MID]!!)
            }
            MID_TO_RIGHT -> {
                if (transitionFrames < 20) {
                    tank.r -= 2
                } else {
                    tank.r += 2
                }
                if (transitionFrames > 0) {
                    tank.x += (C.deviceFiveSixthsX - tank.w / 2 - tank.x) / transitionFrames
                } else {
                    tank.r = 0
                    tank.x = C.deviceFiveSixthsX - tank.w / 2
                    currentState = RIGHT
                }
            }
            MID_TO_LEFT -> {
                if (transitionFrames < 20) {
                    tank.r += 2
                } else {
                    tank.r -= 2
                }
                if (transitionFrames > 0) {
                    tank.x += (C.deviceSixthX - tank.w / 2 - tank.x) / transitionFrames
                } else {
                    tank.r = 0
                    tank.x = C.deviceSixthX - tank.w / 2
                    currentState = LEFT
                }
            }
            RIGHT_TO_MID -> {
                if (transitionFrames < 20) {
                    tank.r += 2
                } else {
                    tank.r -= 2
                }
                if (transitionFrames > 0) {
                    tank.x += (C.deviceCenterX - tank.w / 2 - tank.x) / transitionFrames
                } else {
                    tank.r = 0
                    tank.x = C.deviceCenterX - tank.w / 2
                    currentState = MID
                }
            }
            LEFT_TO_MID -> {
                if (transitionFrames < 20) {
                    tank.r -= 2
                } else {
                    tank.r += 2
                }
                if (transitionFrames > 0) {
                    tank.x += (C.deviceCenterX - tank.w / 2 - tank.x) / transitionFrames
                } else {
                    tank.r = 0
                    tank.x = C.deviceCenterX - tank.w / 2
                    currentState = MID
                }
            }
        }


        if (transitionFrames > 0) {
            transitionFrames--
        }



        tank.update()
    }

    private fun copyToBuffer() {
        buffers.forEach {
            it.clear()
        }
        buffers[0].add(sands)
        buffers[1].add(tank)
        buffers[2].addAll(visibleButtons)

    }

    override fun onDrawFrame(gl: GL10, renderer: Renderer) {
        update()
        copyToBuffer()
        buffers.forEach { list ->
            list.forEach { obj ->
                obj.draw(renderer)
            }
            renderer.batchDraw(gl)
        }
    }
}