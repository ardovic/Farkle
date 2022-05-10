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
        setWH(100, 100)


        val sety = C.deviceHeight - h - C.navBarHeight
        val setx = when (id) {
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

        setXY(setx, sety)

    }

    fun matches(touchX: Int, touchY: Int): Boolean =
        touchX >= x && touchX <= x + w && touchY >= y && touchY <= y + h
}
