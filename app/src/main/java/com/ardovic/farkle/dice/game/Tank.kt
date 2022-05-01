package com.ardovic.farkle.dice.game

import com.ardovic.farkle.dice.C
import com.ardovic.farkle.dice.graphics.Graphics

class Tank : GameObject() {

    private var frame = 0;

    init {
        w = 100
        h = 100


        x = (C.deviceWidth - w) / 2
        y = C.deviceHeight - h - 100

        image = Graphics.greenTankArray[frame]
    }

    fun update() {
        frame++
        if (frame > 6) {
            frame = 0
        }

        image = Graphics.greenTankArray[frame]
    }

}
