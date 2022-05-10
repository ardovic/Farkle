package com.ardovic.farkle.dice.game

import com.ardovic.farkle.dice.graphics.Graphics

class Wall(): GameObject() {

    init {
        image = Graphics.wall
        setXY(100, 100)
        setXY(y = -h)
    }
}