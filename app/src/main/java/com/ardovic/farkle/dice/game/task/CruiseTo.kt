package com.ardovic.farkle.dice.game.task

import com.ardovic.farkle.dice.game.*
import kotlin.math.cos
import kotlin.math.sin

class CruiseTo(
    private val ship: Spaceship,
    private val targetX: Int,
    private val targetY: Int
) : Task {

    private var hardAreaPassed = false;

    override fun getSubTask(): Task? {

//        if (!hardAreaPassed) {
//            hardAreaPassed = true
//
//            if (isHardArea(ship, targetX, targetY)) {
//                return CruiseTo(
//                    ship,
//                    (ship.x + cos(modulusAngle(ship.r).toDouble()) * ship.maxSpeed * 100).toInt(),
//                    (ship.y + sin(modulusAngle(ship.r).toDouble()) * ship.maxSpeed * 100).toInt()
//                )
//            }
//        }

        return null
    }

    override fun getCommands(): List<Command> {
        val rotationCommand = getRotationMessage(ship, targetX, targetY)
        return if (rotationCommand == Command.ROTATE_CW || rotationCommand == Command.ROTATE_CCW) {
            listOf(rotationCommand, Command.ACCELERATE)
        } else {
            listOf(rotationCommand)
        }
    }

    override fun isDone(): Boolean {
        return ship.speed == 0f && distance(ship.x.toInt(), ship.y.toInt(), targetX, targetY) < 100
    }
}
