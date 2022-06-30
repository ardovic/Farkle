package com.ardovic.farkle.dice.game.task

import com.ardovic.farkle.dice.game.Command

object TurnCWWTask : Task() {

    private val commands = listOf(Command.ROTATE_CCW)

    override fun getCommands(): List<Command> = commands

    override fun isDone(): Boolean = false
}
