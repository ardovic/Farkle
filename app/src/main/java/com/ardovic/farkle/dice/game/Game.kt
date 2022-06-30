package com.ardovic.farkle.dice.game

import com.ardovic.farkle.dice.game.task.CruiseTo
import com.ardovic.farkle.dice.game.task.Refuel
import com.ardovic.farkle.dice.game.util.EvictingQueue
import java.util.*
import kotlin.collections.ArrayList

class Game {

    var entities: MutableList<Entity> = ArrayList()
    var player: Spaceship
    private var lastOilAddedMillis: Long = 0
    private val random = Random()

    var snaphotCooldown = 10

    var entitiesToRemove: MutableSet<Entity> = mutableSetOf()

    fun update(touchCoordinate: Coordinate?) {


        touchCoordinate?.let {
            player.tasks.add(CruiseTo(player, it.x, it.y))
            entities.add(Oil().apply {
                radius = 30
                x = touchCoordinate.x
                y = touchCoordinate.y
            })
        }


        snaphotCooldown--

        entities.forEach { entity ->
            if (entity is Spaceship) {

                if (snaphotCooldown == 0) {
                    entities.forEach { other ->
                        if (other.memoType != Memo.NOT_INTERESTING && distance(entity.x, entity.y, other.x, other.y) <= entity.visionRadius) {

                            val coordinate = Coordinate(other.x, other.y)

                            if (entity.memory[other.memoType] == null) {
                                entity.memory[other.memoType] = EvictingQueue(entity.maxMemory)
                            }
                            if (!entity.memory[other.memoType]!!.contains(coordinate)) {
                                entity.memory[other.memoType]!!.add(coordinate)
                            }

                        }
                    }
                }

                if (entity.tasks.any { it is Refuel }) {
                    entities.forEach { other ->
                        if (other is Oil && other.rect.intersect(entity.rect)) {
                            entity.energy = 1500;
                            entitiesToRemove.add(other);
                        }
                    }
                }

                /**
                 * At last we want to update tasks and commands for all entities
                 */
                if (entity.tasks.isNotEmpty()) {
                    val currentTask = entity.tasks.first()
                    val subTask = currentTask.getSubTask()
                    when {
                        currentTask.isParentTaskDone() || currentTask.isDone() -> entity.tasks.remove(currentTask)
                        subTask != null -> entity.tasks.addFirst(subTask)
                        else -> entity.commands = currentTask.getCommands()
                    }
                } else {
                    entity.commands = listOf(Command.DECELERATE)
                }
            }
            entity.update()
        }

        if (snaphotCooldown == 0) {
            snaphotCooldown = 10
        }

        entities.removeAll(entitiesToRemove)
    }

    init {
        val planet = Planet()
        planet.radius = 500
        planet.speed = 0.2f
        planet.r = 45
        planet.x = -300
        planet.y = -300
        entities.add(planet)

        player = Spaceship(isPlayer = true)
        player.radius = 100
        entities.add(player)

        val npc = Spaceship(isPlayer = false)
        npc.radius = 60
        npc.x = -300
        npc.y = -300
        npc.r = -60
        npc.tasks.offer(CruiseTo(npc, 1000, 1000))
        npc.tasks.offer(CruiseTo(npc, 0, 0))
        npc.tasks.offer(CruiseTo(npc, -100, 200))
        npc.tasks.offer(CruiseTo(npc, 400, -600))
        npc.tasks.offer(CruiseTo(npc, 0, 0))
        entities.add(npc)
    }
}