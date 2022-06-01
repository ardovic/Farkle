package com.ardovic.farkle.dice.game

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun getRotationMessage(x: Int, y: Int, r: Int, targetX: Int, targetY: Int): Command {

    // Check distance, maybe close enough
    if (distance(x, y, targetX, targetY) < 100) {
        return Command.DECELERATE
    }

    val isLookingAtTarget = isLookingAtTarget(x, y, r, targetX, targetY)
    val shipAngleDeg = shipAngleDeg(r)
    val targetAngleDeg = targetAngleDeg(x, y, targetX, targetY)

    return if (isLookingAtTarget) {
        Command.ACCELERATE
    } else {

        val cww = if (shipAngleDeg <= 180)
            targetAngleDeg > shipAngleDeg && targetAngleDeg < (shipAngleDeg + 180)
        else
            targetAngleDeg > shipAngleDeg || targetAngleDeg < (shipAngleDeg - 180)

        if (cww) {
            Command.ROTATE_CCW
        } else {
            Command.ROTATE_CW
        }
    }
}

private fun isLookingAtTarget(x: Int, y: Int, r: Int, targetX: Int, targetY: Int): Boolean {
    return abs(shipAngleDeg(r) - targetAngleDeg(x, y, targetX, targetY)) < 10
}

private fun targetAngleDeg(x: Int, y: Int, targetX: Int, targetY: Int): Int {
    val angle = -Math.toDegrees(atan2((targetY - y).toDouble(), (targetX - x).toDouble()))
    return normalizeAngle(angle.roundToInt().toInt())
}

private fun shipAngleDeg(r: Int): Int {
    return normalizeAngle(90 - r)
}

private fun normalizeAngle(angle: Int): Int {
    var newAngle = angle
    if (newAngle < 0) newAngle += 360
    if (newAngle > 360) newAngle -= 360
    return newAngle
}

fun distance(x1: Int, y1: Int, x2: Int, y2: Int): Double {
    return sqrt(((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)).toDouble())
}
