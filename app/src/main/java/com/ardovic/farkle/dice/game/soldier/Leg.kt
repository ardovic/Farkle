package com.ardovic.farkle.dice.game.soldier

import com.ardovic.farkle.dice.engine.GameObject
import com.ardovic.farkle.dice.graphics.Graphics

class Leg(isRight: Boolean) : GameObject() {

    init {
        image = if (isRight) Graphics.soldier_leg_right else Graphics.soldier_leg_left
        setWH(24, 60)
    }

    val state = 


}