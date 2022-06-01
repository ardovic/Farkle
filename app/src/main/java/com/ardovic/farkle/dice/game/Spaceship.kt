package com.ardovic.farkle.dice.game

import com.ardovic.farkle.dice.game.task.Task
import com.ardovic.farkle.dice.graphics.Graphics.spaceship
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class Spaceship : Entity() {

    var energy = 320
    var maxEnergy = 500
    var visionRadius = 700
    var maxMemory = 3

    var memory: MutableMap<Memo, Coordinate>
    var commands: List<Command> = emptyList() // Commands are applied every frame
    var tasks: Queue<Task> = LinkedList() // Task is a long lasting complex operation

    init {
        image = spaceship
        memory = HashMap()
        memory[Memo.ENERGY] = Coordinate(-200, 200)
    }

    override fun update() {
        energy--

        commands.forEach { nextMessage ->
            when (nextMessage) {
                Command.ROTATE_CW -> r++
                Command.ROTATE_CCW -> r--
                Command.ACCELERATE -> {
                    speed += 0.2f
                    if (speed > maxSpeed) {
                        speed = maxSpeed
                    }
                }
                Command.DECELERATE -> {
                    speed -= 0.2f
                    if (speed < 0) {
                        speed = 0f
                    }
                }
            }
        }

        if (speed > 0) {
            x += (cos(radians) * speed).toFloat()
            y += (sin(radians) * speed).toFloat()
        }
    }
}