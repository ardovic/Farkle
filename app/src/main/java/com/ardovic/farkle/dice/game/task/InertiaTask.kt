package com.ardovic.farkle.dice.game.task

import com.ardovic.farkle.dice.game.Command

object InertiaTask: Task() {

    override fun getCommands(): List<Command> = emptyList()

    override fun isDone(): Boolean = false
}