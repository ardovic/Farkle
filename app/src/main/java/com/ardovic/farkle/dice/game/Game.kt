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

    fun update() {

//        if (lastOilAddedMillis < System.currentTimeMillis() - 5_000) {
//            lastOilAddedMillis = System.currentTimeMillis()
//
//            val oil = Oil()
//            oil.radius = 25
//            oil.x = (random.nextInt(2000) - 1000).toFloat()
//            oil.y = (random.nextInt(2000) - 1000).toFloat()
//            entities.add(oil)
//            println("Oil added to universe!")
//        }

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


                if (entity.tasks.isNotEmpty()) {
                    val currentTask = entity.tasks.first()
                    val subTask = currentTask.getSubTask()
                    when {
                        currentTask.isDone() -> entity.tasks.remove(currentTask)
                        subTask != null -> entity.tasks.addFirst(subTask)
                        else -> entity.commands = currentTask.getCommands()
                    }
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
        planet.x = -300f
        planet.y = -300f
        entities.add(planet)

        player = Spaceship(isPlayer = true)
        player.radius = 200
        entities.add(player)

        val npc = Spaceship(isPlayer = false)
        npc.radius = 150
        npc.x = -300f
        npc.y = -300f
        npc.r = -60
        npc.tasks.offer(CruiseTo(npc, 1000, 1000))
        npc.tasks.offer(CruiseTo(npc, 0, 0))
        entities.add(npc)
    }
}