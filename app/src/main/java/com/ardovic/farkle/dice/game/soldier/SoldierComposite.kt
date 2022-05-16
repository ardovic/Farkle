package com.ardovic.farkle.dice.game.soldier

import com.ardovic.farkle.dice.C
import com.ardovic.farkle.dice.engine.GameObject

class SoldierComposite {

    private val objects: List<GameObject>

    private val body: Body
    private val rightLeg: Leg
    private val leftLeg: Leg

    private var frame = 0

    var centerX: Int = 0
        private set
    var centerY: Int = 0
        private set

    init {

        body = Body()
        rightLeg = Leg(true)
        leftLeg = Leg(false)

        objects = listOf(body, rightLeg, leftLeg)

        setCenterXY(C.deviceWidth / 2, C.deviceHeight - C.navBarHeight - 300)
        objects.forEach { it.setPivot(centerX, centerY) }


    }

    fun setCenterXY(centerX: Int = this.centerX, centerY: Int = this.centerY) {
        this.centerX = centerX
        this.centerY = centerY

        body.apply {
            setXY(centerX - w / 2, centerY - h / 2)
        }

        leftLeg.apply {
            setXY(centerX - w, centerY - h)
        }

        rightLeg.apply {
            setXY(centerX - w, centerY - h)
        }
    }
}
