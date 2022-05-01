package com.ardovic.farkle.dice.game

import com.ardovic.farkle.dice.C
import com.ardovic.farkle.dice.graphics.Graphics

class Button constructor(val id: Id) : GameObject() {

    enum class Id {
        GO_LEFT,
        GO_MID,
        GO_RIGHT
    }

    init {
        image = Graphics.metal
        w = 100
        h = 100



        y = C.deviceHeight - 200
        x = when (id) {
            Id.GO_LEFT -> {
                C.deviceSixthX - w / 2
            }
            Id.GO_MID -> {
                C.deviceWidth / 2 - w / 2
            }
            Id.GO_RIGHT -> {
                C.deviceFiveSixthsX - w / 2
            }
        }

    }

    fun matches(touchX: Int, touchY: Int): Boolean =
        touchX >= x && touchX <= x + w && touchY >= y && touchY <= y + h
}
