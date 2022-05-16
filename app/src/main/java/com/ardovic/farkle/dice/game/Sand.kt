package com.ardovic.farkle.dice.game

import com.ardovic.farkle.dice.engine.GameObject

class Sand(x: Int, y: Int): GameObject() {

    init {
        setWH(100, 100)
        setXY(x, y)
    }
}