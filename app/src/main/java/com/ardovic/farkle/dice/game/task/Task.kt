package com.ardovic.farkle.dice.game.task

import com.ardovic.farkle.dice.game.Command

interface Task {

    fun getCommands(): List<Command>

    fun isDone(): Boolean
}
