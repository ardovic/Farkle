package com.ardovic.farkle.dice.game

import android.graphics.Rect
import com.ardovic.farkle.dice.graphics.Graphics
import com.ardovic.farkle.dice.graphics.Image
import com.ardovic.farkle.dice.opengl.Renderer

sealed class GameObject {

    var rect: Rect = Rect()
    var color: Int = Graphics.COLOR_ORIGINAL
    var image: Image = Graphics.sand // TODO -> error image

    var x = 0
    var y = 0
    var w = 0
    var h = 0
    var r = 0

    open fun draw(renderer: Renderer) {

        rect.left = x
        rect.top = y
        rect.right = x + w
        rect.bottom = y + h

        when {
            color != Graphics.COLOR_ORIGINAL -> renderer.draw(image, rect, r, color)
            r % 360 != 0 -> renderer.draw(image, rect, r)
            else -> renderer.draw(image, rect)
        }
    }
}
