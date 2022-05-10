package com.ardovic.farkle.dice.game.tank

import com.ardovic.farkle.dice.game.GameObject
import com.ardovic.farkle.dice.graphics.Graphics

class Snail : GameObject() {

    private var frame = 0;

    init {
        setWH(50, 200)

        image = Graphics.tank_snails_0
    }

    fun update() {
        frame++
        if (frame > 2) {
            frame = 0
        }
        image = when (frame) {
            0 -> Graphics.tank_snails_0
            1 -> Graphics.tank_snails_1
            else -> Graphics.tank_snails_2
        }
    }
}
