package com.ardovic.farkle.dice.game

import android.graphics.Rect
import com.ardovic.farkle.dice.game.task.Refuel
import com.ardovic.farkle.dice.game.task.Task
import com.ardovic.farkle.dice.game.util.EvictingQueue
import com.ardovic.farkle.dice.graphics.Graphics.spaceship
import java.util.*
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sin

class Spaceship(private val isPlayer: Boolean) : Entity() {

    // TODO move trigonometric stuff to an abstract class

    private var rotationRadius = 0f
    var leftCenterX = 0F
    var leftCenterY = 0F
    var rightCenterX = 0F
    var rightCenterY = 0F

    var energy = 500
    var maxEnergy = 1500

    var visionRadius = 700
    var maxMemory = 3

    var memory: MutableMap<Memo, EvictingQueue<Coordinate>>
    var commands: List<Command> = emptyList() // Commands are applied every frame
    var tasks: LinkedList<Task> = LinkedList() // Task is a long lasting complex operation


    init {
        image = spaceship
        memory = EnumMap(Memo::class.java)
        memory[Memo.ENERGY] = EvictingQueue(maxMemory)
    }

    fun useMemoCoordinate(type: Memo): Coordinate? {
        return memory[type]?.removeLastOrNull()
    }

    override fun update() {
        energy--

        rotationRadius = maxSpeed * 60 // TODO why?
        val modulusAngle = normalizedShipAngleDeg(r).toDouble()

        leftCenterX = (x - sin(Math.toRadians(modulusAngle)) * rotationRadius).toFloat()
        leftCenterY = (y - cos(Math.toRadians(modulusAngle)) * rotationRadius).toFloat()
        rightCenterX = (x + sin(Math.toRadians(modulusAngle)) * rotationRadius).toFloat()
        rightCenterY = (y + cos(Math.toRadians(modulusAngle)) * rotationRadius).toFloat()

        if (!isPlayer && energy < 0.2 * maxEnergy && tasks.none { it is Refuel }) {
            tasks.addFirst(Refuel(this))
        }

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
