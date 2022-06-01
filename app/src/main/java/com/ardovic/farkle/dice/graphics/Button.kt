package com.ardovic.farkle.dice.graphics

import com.ardovic.farkle.dice.C
import com.ardovic.farkle.dice.engine.GameObject

class Button(val type: Type) : GameObject() {

    enum class Type {
        CCW, CW, GAS, BREAK
    }

    init {

        when (type) {
            Type.CCW -> {
                image = Graphics.button_ccw
                setXY(50, C.deviceHeight - 300)
            }
            Type.CW -> {
                image = Graphics.button_cw
                setXY(200, C.deviceHeight - 300)
            }
            Type.GAS -> {
                image = Graphics.button_gas
                setXY(350, C.deviceHeight - 300)
            }
            Type.BREAK -> {
                image = Graphics.button_stop
                setXY(500, C.deviceHeight - 300)
            }
        }

        setWH(100, 100)
    }

}