package com.ardovic.farkle.dice.game.task

import com.ardovic.farkle.dice.game.*
import kotlin.math.cos
import kotlin.math.sin

class CruiseTo(
    private val ship: Spaceship,
    private val targetX: Int,
    private val targetY: Int
) : Task {

    private var hardAreaChecked = false

    override fun getSubTask(): Task? {

        if (!hardAreaChecked) {
            hardAreaChecked = true

            val travelStraightBeforeTurn = travelStraightBeforeTurn(ship, targetX, targetY)

            if (travelStraightBeforeTurn > 0) {
                return CruiseTo(
                    ship,
                    (ship.x + cos(ship.radians) * travelStraightBeforeTurn).toInt(),
                    (ship.y + sin(ship.radians) * travelStraightBeforeTurn).toInt()
                )
            }
        }

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

    override fun isDone(): Boolean = ship.speed == 0f && distance(ship.x, ship.y, targetX, targetY) < ship.radius
}
