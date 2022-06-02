package com.ardovic.farkle.dice.game

import kotlin.math.*

fun getRotationMessage(entity: Entity, targetX: Int, targetY: Int): Command {

    // Check distance, maybe close enough
    if (distance(entity.x.toInt(), entity.y.toInt(), targetX, targetY) < 100) {
        return Command.DECELERATE
    }

    val isLookingAtTarget = isLookingAtTarget(entity.x.toInt(), entity.y.toInt(), entity.r, targetX, targetY)
    val shipAngleDeg = normalizedShipAngleDeg(entity.r)
    val targetAngleDeg = normalizedTargetAngleDeg(entity.x.toInt(), entity.y.toInt(), targetX, targetY)

    return if (isLookingAtTarget) {
        entity.r = -targetAngleDeg
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
    return abs(normalizedShipAngleDeg(r) - normalizedTargetAngleDeg(x, y, targetX, targetY)) < 10
}

// Returns normalized angle to target from center
private fun normalizedTargetAngleDeg(x: Int, y: Int, targetX: Int, targetY: Int): Int {
    val angle = -Math.toDegrees(atan2((targetY - y).toDouble(), (targetX - x).toDouble()))
    return modulusAngle(angle.roundToInt())
}

// Returns normalized angle of the ship
fun normalizedShipAngleDeg(r: Int): Int {
    return modulusAngle(90 - r)
}

fun modulusAngle(angle: Int): Int {
    var newAngle = angle
    if (newAngle < 0) newAngle += 360
    if (newAngle > 360) newAngle -= 360
    return newAngle
}

fun distance(x1: Int, y1: Int, x2: Int, y2: Int): Double {
    return sqrt(((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)).toDouble())
}

fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Double {
    return sqrt(((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)).toDouble())
}
