package com.ardovic.farkle.dice.game.task

import com.ardovic.farkle.dice.game.Command
import com.ardovic.farkle.dice.game.Memo
import com.ardovic.farkle.dice.game.Spaceship

class Refuel(
    private val ship: Spaceship,
    parentTask: Task? = null
) : Task(parentTask) {

    override fun getSubTask(): Task? {
        return ship.useMemoCoordinate(Memo.ENERGY)
            ?.let { coordinate -> CruiseTo(ship, coordinate.x, coordinate.y, this) }
    }

    override fun getCommands(): List<Command> {
        return emptyList()
    }

    override fun isDone(): Boolean {
        return ship.energy > ship.maxEnergy * 0.8f
    }
}
