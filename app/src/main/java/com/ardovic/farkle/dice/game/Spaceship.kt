package com.ardovic.farkle.dice.game

import com.ardovic.farkle.dice.game.task.Refuel
import com.ardovic.farkle.dice.game.task.Task
import com.ardovic.farkle.dice.game.util.EvictingQueue
import com.ardovic.farkle.dice.graphics.Graphics.spaceship
import com.ardovic.farkle.dice.graphics.Graphics.spaceship_1
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class Spaceship(private val isPlayer: Boolean) : Entity() {

    // TODO move trigonometric stuff to an abstract class

    var rotationRadius: Int = 0
    var leftCenterX: Int = 0
    var leftCenterY: Int = 0
    var rightCenterX: Int = 0
    var rightCenterY: Int = 0

    var energy: Int = 500
    var maxEnergy: Int = 1500

    var visionRadius: Int = 700
    var maxMemory: Int = 3

    var memory: MutableMap<Memo, EvictingQueue<Coordinate>>
    var commands: List<Command>? = null // Commands are applied every frame
    var tasks: LinkedList<Task> = LinkedList() // Task is a long lasting complex operation


    init {
        image = if(isPlayer) {
            spaceship
        } else {
            spaceship_1
        }
        memory = EnumMap(Memo::class.java)
        memory[Memo.ENERGY] = EvictingQueue(maxMemory)
    }

    fun useMemoCoordinate(type: Memo): Coordinate? {
        return memory[type]?.removeLastOrNull()
    }

    override fun update() {
        energy--

        rotationRadius = (maxSpeed * 58 / dr).toInt() // TODO why?
        val modulusAngle = normalizedShipAngleDeg(r).toDouble()

        leftCenterX = (x - sin(Math.toRadians(modulusAngle)) * rotationRadius).toInt()
        leftCenterY = (y - cos(Math.toRadians(modulusAngle)) * rotationRadius).toInt()
        rightCenterX = (x + sin(Math.toRadians(modulusAngle)) * rotationRadius).toInt()
        rightCenterY = (y + cos(Math.toRadians(modulusAngle)) * rotationRadius).toInt()

        if (!isPlayer && energy < 0.2 * maxEnergy && tasks.none { it is Refuel }) {
            tasks.addFirst(Refuel(this))
        }

        commands?.forEach { nextMessage ->
            when (nextMessage) {
                Command.ROTATE_CW -> r += dr
                Command.ROTATE_CCW -> r -= dr
                Command.ACCELERATE -> {
                    speed += acceleration
                    if (speed > maxSpeed) {
                        speed = maxSpeed
                    }
                }
                Command.DECELERATE -> {
                    speed -= acceleration
                    if (speed < 0) {
                        speed = 0f
                    }
                }
            }
        }
        commands = null

        if (speed > 0) {
            renderX += cos(radians) * speed
            renderY += sin(radians) * speed
        }
    }
}
