package com.ardovic.farkle.dice.game.tank

import com.ardovic.farkle.dice.game.GameObject
import com.ardovic.farkle.dice.graphics.Graphics

class Gun() : GameObject() {

    init {
        image = Graphics.tank_gun_0
        setWH(60, 180)
    }
}
