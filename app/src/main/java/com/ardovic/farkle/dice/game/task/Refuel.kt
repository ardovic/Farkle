package com.ardovic.farkle.dice.game.task

import com.ardovic.farkle.dice.game.Command
import com.ardovic.farkle.dice.game.Spaceship

class Refuel(
    private val ship: Spaceship
) : Task {

    override fun getCommands(): List<Command> {
       return emptyList()
    }

    override fun isDone(): Boolean {
        return ship.energy > ship.maxEnergy * 0.8f
    }
}
