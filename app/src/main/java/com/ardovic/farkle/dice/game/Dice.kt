package com.ardovic.farkle.dice.game

import com.ardovic.farkle.dice.R
import com.ardovic.farkle.dice.graphics.Graphics
import com.ardovic.farkle.dice.graphics.Image

class Dice : GameObject(Image(0, 0, 100, 100, R.drawable.sprite_map_02)) {

    var state = Graphics.stableStates.random()

    init {
        x = 100
        y = 100
        w = 200
        h = 200

        image = Graphics.matrix[state.first][state.second]

        stopMovement()
    }

    var finishState = state

    override fun realisticMovement() {

        if (dx != 0 || dy != 0) {

            var diceX = state.first - 1
            var diceY = state.second + 1

            if (diceX > 15) {
                diceX = 0
            }

            if (diceX < 0) {
                diceX = 15
            }

            if (diceY > 8) {
                diceY = 0
            }

            if (diceY < 0) {
                diceY = 8
            }


            state = diceX to diceY

        } else if (dx == 0 && dy == 0) {
            state = finishState
        }
        image = Graphics.matrix[state.first][state.second]

//        if(dx.absoluteValue > 4) {
//            finishVector =
//        }


        super.realisticMovement()
    }

    fun collidesWith(dice: Dice): Boolean {

        return dice.rect.intersect(rect)
    }
}
