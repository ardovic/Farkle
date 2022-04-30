package com.ardovic.farkle.dice.game

import com.ardovic.farkle.dice.C
import com.ardovic.farkle.dice.graphics.Graphics
import com.ardovic.farkle.dice.graphics.Image

class Cup: GameObject(Image(0,0, 200, 300, Graphics.SPRITE_MAP_01)) {

    init {

        w = 500
        h = 750


        x = C.deviceWidth - w
        y = C.deviceHeight - h

        r = -30

        stopMovement()
    }

}