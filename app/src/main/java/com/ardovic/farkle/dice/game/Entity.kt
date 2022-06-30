package com.ardovic.farkle.dice.game

import android.graphics.Rect
import com.ardovic.farkle.dice.graphics.Image
import kotlin.math.roundToInt

abstract class Entity {

    open val memoType: Memo = Memo.NOT_INTERESTING

    val rect = Rect()
        get() {
            field.left = x - radius
            field.top = y - radius
            field.right = x + radius
            field.bottom = y + radius
            return field
        }

    var image: Image? = null
    var x: Int = 0
    var y: Int = 0
    var renderX: Double = 0.0
        set(value) {
            field = value
            x = value.roundToInt()
        }

    var renderY: Double = 0.0
        set(value) {
            field = value
            y = value.roundToInt()
        }

    var radius: Int = 0

    var radians: Double = 0.0
    var r = 0
        set(value) {
            radians = Math.toRadians((r - 90).toDouble())
            field = value
        }
    var dr = 2 // amount of angle change per update call

    var speed: Float = 0f
    var acceleration: Float = 0.2f
    var maxSpeed: Float = 5f

    abstract fun update()

    init {
        r = 0
    }
}
