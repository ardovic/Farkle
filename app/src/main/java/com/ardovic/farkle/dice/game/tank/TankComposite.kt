package com.ardovic.farkle.dice.game.tank

import com.ardovic.farkle.dice.C
import com.ardovic.farkle.dice.game.Drawable
import com.ardovic.farkle.dice.game.GameObject
import com.ardovic.farkle.dice.graphics.Graphics
import com.ardovic.farkle.dice.opengl.Renderer
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

class TankComposite : Drawable {

    private val objects: List<GameObject>


    private val body: Body
    private val gun: Gun
    private val snailR: Snail
    private val snailL: Snail

    private var frame = 0

    var centerX: Int = 0
        private set
    var centerY: Int = 0
        private set

    init {

        body = Body()
        gun = Gun()
        snailL = Snail()
        snailR = Snail()

        objects = listOf(body, gun, snailL, snailR)

        setCenterXY(C.deviceWidth / 2, C.deviceHeight - C.navBarHeight - 300)
        objects.forEach { it.setPivot(centerX, centerY) }


    }

    var r: Int = 0
        set(value) {
            field = value
            objects.forEach {
                it.r = r
            }
        }

    fun setCenterXY(centerX: Int = this.centerX, centerY: Int = this.centerY) {
        this.centerX = centerX
        this.centerY = centerY

        body.apply {
            setXY(centerX - w / 2, centerY - h / 2)
        }

        gun.apply {
            setXY(centerX - w / 2, centerY - 3 * h / 4)
        }

        snailL.apply {
            setXY(centerX - body.w / 2 - w, centerY - h / 2)
        }

        snailR.apply {
            setXY(centerX + body.w / 2, centerY - h / 2)
        }
    }

    fun update() {
        frame++
        if (frame > 5) {
            frame = 0
        }

        when (frame) {
            0, 1 -> {
                snailL.image = Graphics.tank_snails_0
                snailR.image = Graphics.tank_snails_0
            }
            2, 3 -> {
                snailL.image = Graphics.tank_snails_1
                snailR.image = Graphics.tank_snails_1
            }
            4, 5 -> {
                snailL.image = Graphics.tank_snails_2
                snailR.image = Graphics.tank_snails_2
            }
        }
    }

    override fun draw(renderer: Renderer) {
        body.draw(renderer)
        snailR.draw(renderer)
        snailL.draw(renderer)
        gun.draw(renderer)
    }
}
