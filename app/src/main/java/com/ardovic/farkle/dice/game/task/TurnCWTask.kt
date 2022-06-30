package com.ardovic.farkle.dice.game.task

import com.ardovic.farkle.dice.game.Command

object TurnCWTask : Task() {

    private val commands = listOf(Command.ROTATE_CW)

    override fun getCommands(): List<Command> = commands

    override fun isDone(): Boolean = false
}
