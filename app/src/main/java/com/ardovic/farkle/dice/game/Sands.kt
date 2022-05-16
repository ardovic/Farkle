package com.ardovic.farkle.dice.game

import com.ardovic.farkle.dice.C
import com.ardovic.farkle.dice.engine.GameObject
import com.ardovic.farkle.dice.engine.opengl.Renderer

class Sands : GameObject() {

    private val sands = mutableListOf<Sand>()
    private val size = 100

    init {

        val horizontal = C.deviceWidth / size + 1
        val vertical = C.deviceHeight / size + 1

        for (h in 0..horizontal) {
            for (v in -1..vertical) {
                sands.add(Sand(x = size * h, y = size * v))
            }
        }

    }

    fun update() {

        var topY = 0

        sands.forEach {
            it.setXY(y = it.y + 3)
            topY = minOf(it.y, topY)
        }

        sands.filter { it.y > C.deviceHeight }
            .forEach { it.setXY(y = topY - size) }
    }

    override fun draw(renderer: Renderer) {
        sands.forEach { it.draw(renderer) }
    }

}