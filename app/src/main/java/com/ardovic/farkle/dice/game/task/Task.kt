package com.ardovic.farkle.dice.game.task

import com.ardovic.farkle.dice.game.Command

sealed class Task(private val parentTask: Task? = null) {

    fun isParentTaskDone(): Boolean = parentTask?.isDone() ?: false

    open fun getSubTask(): Task? = null

    abstract fun getCommands(): List<Command>

    abstract fun isDone(): Boolean
}
