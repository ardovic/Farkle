package com.ardovic.farkle.dice.engine

import android.graphics.Rect
import com.ardovic.farkle.dice.graphics.Graphics
import com.ardovic.farkle.dice.graphics.Image
import com.ardovic.farkle.dice.engine.opengl.Renderer
import kotlin.math.*

abstract class GameObject : Drawable {

    var rect: Rect = Rect()
        private set
    var color: Int = Graphics.COLOR_DEFAULT
    var image: Image = Graphics.sand // TODO -> error image

    var x = 0
        private set
    var y = 0
        private set
    var w = 0
        private set
    var h = 0
        private set

    var r = 0
        set(value) {
            field = value
            updateRenderXY()
        }

    private var renderX = 0
    private var renderY = 0

    private var pivotDX = 0
    private var pivotDY = 0

    var centerX = 0
        private set
    var centerY = 0
        private set

    fun setXY(x: Int = this.x, y: Int = this.y) {
        this.x = x
        this.y = y

        measureCenter()
    }

    fun setWH(w: Int = this.w, h: Int = this.h) {
        this.w = w
        this.h = h

        measureCenter()
    }


    private fun measureCenter() {
        centerX = x + w / 2
        centerY = y + h / 2

        updateRenderXY()
    }


    fun setPivot(pivotX: Int = 0, pivotY: Int = 0) {
        pivotDX = pivotX - centerX
        pivotDY = pivotY - centerY

        updateRenderXY()
    }

    private fun updateRenderXY() {
        if (r == 0) {
            renderX = x
            renderY = y
        } else {

            // TODO optimize calculations with caching -> quick converted from degrees to cosine/sine

            val radians = Math.toRadians((r).toDouble())

            val referenceX = centerX + pivotDX
            val referenceY = centerY + pivotDY

            val newCenterX =
                referenceX - (((pivotDX * cos(radians)).roundToInt() - (pivotDY * sin(radians)).roundToInt()))
            val newCenterY =
                referenceY - (((pivotDX * sin(radians)).roundToInt() + (pivotDY * cos(radians)).roundToInt()))

            renderX = newCenterX - w / 2
            renderY = newCenterY - h / 2
        }
    }

    override fun draw(renderer: Renderer) {

        rect.left = renderX
        rect.top = renderY
        rect.right = renderX + w
        rect.bottom = renderY + h

        when {
            color != Graphics.COLOR_DEFAULT -> renderer.draw(image, rect, r, color)
            r % 360 != 0 -> renderer.draw(image, rect, r)
            else -> renderer.draw(image, rect)
        }
    }
}
