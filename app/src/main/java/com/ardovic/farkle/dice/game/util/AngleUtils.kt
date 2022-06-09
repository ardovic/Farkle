package com.ardovic.farkle.dice.game

import kotlin.math.*

fun getRotationMessage(entity: Entity, targetX: Int, targetY: Int): Command {

    // Check distance, maybe close enough
    if (distance(entity.x.toInt(), entity.y, targetX, targetY) < 100) {
        return Command.DECELERATE
    }

    val isLookingAtTarget = isLookingAtTarget(entity.x, entity.y, entity.r, targetX, targetY)
    val shipAngleDeg = normalizedShipAngleDeg(entity.r)
    val targetAngleDeg = normalizedTargetAngleDeg(entity.x, entity.y, targetX, targetY)

    return if (isLookingAtTarget) {
        entity.r = denormalizeAngle(-targetAngleDeg)
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

fun travelStraightBeforeTurn(ship: Spaceship, targetX: Int, targetY: Int): Int {
    val leftCenterTargetDistance = distance(targetX, targetY, ship.leftCenterX, ship.leftCenterY)
    val rightCenterTargetDistance = distance(targetX, targetY, ship.rightCenterX, ship.rightCenterY)
    val safeRotationRadius = ship.rotationRadius * 1.1F

    return if (leftCenterTargetDistance < safeRotationRadius || rightCenterTargetDistance < safeRotationRadius) {
        (safeRotationRadius * 2).toInt()
    } else {
        0
    }
}

private fun isLookingAtTarget(x: Int, y: Int, r: Int, targetX: Int, targetY: Int): Boolean {
    return abs(normalizedShipAngleDeg(r) - normalizedTargetAngleDeg(x, y, targetX, targetY)) < 3
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

fun denormalizeAngle(normalizedAngle: Int): Int {
    return modulusAngle(normalizedAngle + 90)
}

fun modulusAngle(angle: Int): Int {
    var newAngle = angle % 360
    if (newAngle < 0) newAngle += 360
    if (newAngle > 360) newAngle -= 360
    return newAngle
}

fun distance(x1: Int, y1: Int, x2: Int, y2: Int): Double {
    return sqrt(((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)).toDouble())
}
