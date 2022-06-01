package com.ardovic.farkle.dice.game.task

import com.ardovic.farkle.dice.game.Command
import com.ardovic.farkle.dice.game.Spaceship
import com.ardovic.farkle.dice.game.distance
import com.ardovic.farkle.dice.game.getRotationMessage

class CruiseTo(
    private val ship: Spaceship,
    private val targetX: Int,
    private val targetY: Int
) : Task {

    override fun getCommands(): List<Command> {
        val rotationCommand = getRotationMessage(ship.x.toInt(), ship.y.toInt(), ship.r, targetX, targetY)
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
