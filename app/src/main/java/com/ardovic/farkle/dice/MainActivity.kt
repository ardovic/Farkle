package com.ardovic.farkle.dice

import android.os.Bundle
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ardovic.farkle.dice.MainActivity.State.*
import com.ardovic.farkle.dice.delegates.SystemDelegate
import com.ardovic.farkle.dice.engine.Drawable
import com.ardovic.farkle.dice.game.*
import com.ardovic.farkle.dice.game.Button.*
import com.ardovic.farkle.dice.game.Button.Id.*
import com.ardovic.farkle.dice.game.tank.TankComposite
import com.ardovic.farkle.dice.graphics.Graphics
import com.ardovic.farkle.dice.engine.opengl.FrameDrawer
import com.ardovic.farkle.dice.engine.opengl.Renderer
import javax.microedition.khronos.opengles.GL10
import kotlin.random.Random

class MainActivity : AppCompatActivity(), FrameDrawer {

    private lateinit var rootLayout: FrameLayout

    private var buffers: Array<MutableList<Drawable>> = Array(C.BUFFER_COUNT) { ArrayList() }


    lateinit var sands: Sands
    lateinit var enemies: Enemies
    //lateinit var tank: Tank
    lateinit var tankComposite: TankComposite
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

    companion object {
        const val TRANSITION_FRAMES = 30
    }

    private var transitionFrames = 0
    private var setupComplete: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.GameTheme)
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(null)

        rootLayout = FrameLayout(this)
        SystemDelegate.onCreateBgViewReturn(this, rootLayout)

        sands = Sands()
        enemies = Enemies()

        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { _, insets ->
            if (!setupComplete) {
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

                C.statusBarHeight = systemBars.top
                C.navBarHeight = systemBars.bottom

               // tank = Tank()

                tankComposite = TankComposite()
                tankComposite.setCenterXY(C.deviceCenterX, C.deviceHeight - C.navBarHeight - 300)

                allButtons = hashMapOf(
                    GO_LEFT to Button(GO_LEFT),
                    GO_MID to Button(GO_MID),
                    GO_RIGHT to Button(GO_RIGHT)
                )
                setupComplete = true
            }
            insets
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
                            transitionFrames = TRANSITION_FRAMES
                        }
                        GO_MID -> {
                            if (currentState == LEFT) {
                                currentState = LEFT_TO_MID
                            }
                            if (currentState == RIGHT) {
                                currentState = RIGHT_TO_MID
                            }
                            transitionFrames = TRANSITION_FRAMES
                        }
                        GO_RIGHT -> {
                            currentState = MID_TO_RIGHT
                            transitionFrames = TRANSITION_FRAMES
                        }
                    }
                }
                activeButton?.apply { color = Graphics.COLOR_DEFAULT }
            }
        }

        return true
    }

    private fun update() {
        sands.update()
        enemies.update()


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
                if (transitionFrames < TRANSITION_FRAMES / 2) {
                    tankComposite.r -= 2
                } else {
                    tankComposite.r += 2
                }
                if (transitionFrames > 0) {

                    val dx = (C.deviceFiveSixthsX - tankComposite.centerX) / transitionFrames

                    tankComposite.setCenterXY(centerX = tankComposite.centerX + dx)
                } else {
                    tankComposite.r = 0

                    tankComposite.setCenterXY(
                        centerX = C.deviceFiveSixthsX
                    )
                    currentState = RIGHT
                }

            }
            MID_TO_LEFT -> {
                if (transitionFrames < TRANSITION_FRAMES / 2) {
                    tankComposite.r += 2
                } else {
                    tankComposite.r -= 2
                }
                if (transitionFrames > 0) {
                    val dx = (C.deviceSixthX - tankComposite.centerX) / transitionFrames

                    tankComposite.setCenterXY(
                        centerX = tankComposite.centerX + dx
                    )
                } else {
                    tankComposite.r = 0

                    tankComposite.setCenterXY(
                        centerX = C.deviceSixthX
                    )
                    currentState = LEFT
                }
            }
            RIGHT_TO_MID -> {
                if (transitionFrames < TRANSITION_FRAMES / 2) {
                    tankComposite.r += 2
                } else {
                    tankComposite.r -= 2
                }
                if (transitionFrames > 0) {
                    val dx = (C.deviceCenterX - tankComposite.centerX) / transitionFrames

                    tankComposite.setCenterXY(
                        centerX = tankComposite.centerX + dx
                    )
                } else {
                    tankComposite.r = 0

                    tankComposite.setCenterXY(
                        centerX = C.deviceCenterX
                    )
                    currentState = MID
                }
            }
            LEFT_TO_MID -> {
                if (transitionFrames < TRANSITION_FRAMES / 2) {
                    tankComposite.r -= 2
                } else {
                    tankComposite.r += 2
                }
                if (transitionFrames > 0) {
                    val dx = (C.deviceCenterX - tankComposite.centerX) / transitionFrames

                    tankComposite.setCenterXY(
                        centerX = tankComposite.centerX + dx
                    )
                } else {
                    tankComposite.r = 0

                    tankComposite.setCenterXY(
                        centerX = C.deviceCenterX
                    )
                    currentState = MID
                }
            }
        }


        if (transitionFrames > 0) {
            transitionFrames--
        }



        //tank.update()
        tankComposite.update()
    }

    private fun copyToBuffer() {
        buffers.forEach {
            it.clear()
        }
        buffers[0].add(sands)
        buffers[1].add(enemies)
        buffers[1].add(tankComposite)
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