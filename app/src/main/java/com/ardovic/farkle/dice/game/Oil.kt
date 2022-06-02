package com.ardovic.farkle.dice.game

import com.ardovic.farkle.dice.graphics.Graphics.oil

class Oil : Entity() {

    override val memoType: Memo = Memo.ENERGY

    override fun update() {}

    init {
        image = oil
    }
}
