package com.ardovic.farkle.dice.graphics.tank

import com.ardovic.farkle.dice.engine.GameObject
import com.ardovic.farkle.dice.graphics.Graphics

class Body() : GameObject() {

    init {
        image = Graphics.tank_body_0
        setWH(80, 150)
    }
}