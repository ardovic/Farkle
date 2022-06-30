package com.ardovic.farkle.dice.graphics

import com.ardovic.farkle.dice.C
import com.ardovic.farkle.dice.engine.GameObject

class Button(val type: Type) : GameObject() {

    enum class Type {
        MACRO_CONTROLS,
        MICRO_CONTROLS,
        SINGLE_TAP,
        DOUBLE_TAP,
        TURN_CCW,
        TURN_CW,
        ACCELERATE,
        BREAK
    }

    init {

        setWH(C.buttonSize, C.buttonSize)
        val buttonsOffset = C.buttonSize / 2

        when (type) {
            Type.MACRO_CONTROLS -> {
                image = Graphics.bttn_macro_controls
                setXY(buttonsOffset, C.deviceHeight - (buttonsOffset + C.buttonSize))
            }
            Type.MICRO_CONTROLS -> {
                image = Graphics.bttn_micro_controls
                setXY(buttonsOffset, C.deviceHeight - (buttonsOffset + C.buttonSize))
            }
            Type.SINGLE_TAP -> {
                image = Graphics.bttn_single_tap
                setXY(buttonsOffset, C.deviceHeight - (2 * buttonsOffset + 2 * C.buttonSize))
            }
            Type.DOUBLE_TAP -> {
                image = Graphics.bttn_double_tap
                setXY(2 * buttonsOffset + C.buttonSize, C.deviceHeight - (2 * buttonsOffset + 2 * C.buttonSize))
            }
            Type.TURN_CCW -> {
                image = Graphics.bttn_turn_ccw
                setXY(2 * buttonsOffset + C.buttonSize, C.deviceHeight - (2 * buttonsOffset + 2 * C.buttonSize))
            }
            Type.TURN_CW -> {
                image = Graphics.bttn_turn_cw
                setXY(3 * buttonsOffset + 2 * C.buttonSize, C.deviceHeight - (2 * buttonsOffset + 2 * C.buttonSize))
            }
            Type.ACCELERATE -> {
                image = Graphics.bttn_accelerate
                setXY(buttonsOffset, C.deviceHeight - (2 * buttonsOffset + 2 * C.buttonSize))
            }
            Type.BREAK -> {
                image = Graphics.bttn_brake
                setXY(4 * buttonsOffset + 3 * C.buttonSize, C.deviceHeight - (2 * buttonsOffset + 2 * C.buttonSize))
            }
        }
    }

    companion object {

        val microControlButtons: List<Button> by lazy {
            listOf(
                Button(Type.MACRO_CONTROLS),
                Button(Type.BREAK),
                Button(Type.ACCELERATE),
                Button(Type.TURN_CCW),
                Button(Type.TURN_CW)
            )
        }

        val macroControlButtons: List<Button> by lazy {
            listOf(
                Button(Type.MICRO_CONTROLS),
                Button(Type.SINGLE_TAP),
                Button(Type.DOUBLE_TAP)
            )
        }
    }
}
