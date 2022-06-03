package com.ardovic.farkle.dice.game

import android.graphics.Rect
import com.ardovic.farkle.dice.graphics.Image

abstract class Entity {

    open val memoType: Memo = Memo.NOT_INTERESTING

    val rect = Rect()
        get() {
            field.left = x.toInt() - radius
            field.top = y.toInt() - radius
            field.right = x.toInt() + radius
            field.bottom = y.toInt() + radius
            return field
        }

    var image: Image? = null
    var x = 0f
    var y = 0f
    var radius = 0

    var r = 0
        set(value) {
            radians = Math.toRadians((r - 90).toDouble())
            field = value
        }
    var dr = 1 // amount of angle change per update call

    var radians = 0.0
    var speed = 0f
    var acceleration = 0.2f
    var maxSpeed = 5.0f

    abstract fun update()

    init {
        r = 0
    }
}
