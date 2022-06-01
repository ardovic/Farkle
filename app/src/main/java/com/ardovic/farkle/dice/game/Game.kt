package com.ardovic.farkle.dice.game

import com.ardovic.farkle.dice.game.task.CruiseTo
import java.util.*

class Game {

    var entities: MutableSet<Entity> = HashSet()
    var player: Spaceship
    private var lastOilAddedMillis: Long = 0
    private val random = Random()

    fun update() {

        if (lastOilAddedMillis < System.currentTimeMillis() - 5000) {
            lastOilAddedMillis = System.currentTimeMillis()

            val oil = Oil()
            oil.radius = 40
            oil.x = (random.nextInt(1000) - 500).toFloat()
            oil.y = (random.nextInt(1000) - 500).toFloat()
            entities.add(oil)
            println("Oil added to universe!")
        }

        for (entity in entities) {
            if (entity is Spaceship) {

                entity.tasks.peek()?.let { currentTask ->
                    if (currentTask.isDone()) {
                        entity.tasks.remove()
                    } else {
                        entity.commands = currentTask.getCommands()
                    }
                }
            }
            entity.update()
        }
    }

    init {
        val planet = Planet()
        planet.radius = 500
        planet.speed = 0.2f
        planet.r = 45
        planet.x = -300f
        planet.y = -300f
        entities.add(planet)

        player = Spaceship()
        player.radius = 200
        entities.add(player)

        val npc = Spaceship()
        npc.radius = 150
        npc.x = -300f
        npc.y = -300f
        npc.r = -60
        npc.tasks.offer(CruiseTo(npc, 1000, 1000))
        npc.tasks.offer(CruiseTo(npc, 0, 0))
        entities.add(npc)
    }
}