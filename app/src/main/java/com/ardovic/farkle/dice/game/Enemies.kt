package com.ardovic.farkle.dice.game

import com.ardovic.farkle.dice.C
import com.ardovic.farkle.dice.GameDesign
import com.ardovic.farkle.dice.opengl.Renderer

class Enemies : GameObject() {

    private val enemies = mutableListOf<Wall>()
    private val size = 100
    private var step = 0

    init {


    }

    fun update() {

        var topY = 1000

        enemies.forEach {
            it.setXY(y = it.y + 3)
            topY = minOf(it.y, topY)
        }

        println(topY)

        if (step < GameDesign.level1.size && (enemies.isEmpty() || topY > 900)) {

            GameDesign.level1[step].mapIndexed { idx, id ->
                if (id != 0) {
                    enemies.add(
                        Wall().apply {
                            when (idx) {
                                0 -> setXY(C.deviceSixthX - w / 2)
                                1 -> setXY(C.deviceCenterX - w / 2)
                                2 -> setXY(C.deviceFiveSixthsX - w / 2)
                            }
                        }
                    )
                }
            }

            step++
        }

        enemies.removeAll { it.y > C.deviceHeight }
    }

    override fun draw(renderer: Renderer) {
        enemies.forEach { it.draw(renderer) }
    }
}