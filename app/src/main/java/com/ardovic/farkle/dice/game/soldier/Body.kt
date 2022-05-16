package com.ardovic.farkle.dice.game.soldier

import com.ardovic.farkle.dice.engine.GameObject
import com.ardovic.farkle.dice.graphics.Graphics

class Body: GameObject() {

    init {
        image = Graphics.soldier_state_normal
        setWH(80, 80)
    }
}