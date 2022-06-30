package com.ardovic.farkle.dice.game.task

import com.ardovic.farkle.dice.game.Command

object AccelerateTask : Task() {

    private val commands = listOf(Command.ACCELERATE)

    override fun getCommands(): List<Command> = commands

    override fun isDone(): Boolean = false
}
