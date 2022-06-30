package com.ardovic.farkle.dice.game.task

import com.ardovic.farkle.dice.game.Command

object DecelerateTask : Task() {

    private val commands = listOf(Command.DECELERATE)

    override fun getCommands(): List<Command> = commands

    override fun isDone(): Boolean = false
}
