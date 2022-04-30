package com.ardovic.farkle.dice.game

import android.graphics.Rect
import com.ardovic.farkle.dice.C
import com.ardovic.farkle.dice.graphics.Graphics
import com.ardovic.farkle.dice.graphics.Image
import com.ardovic.farkle.dice.opengl.Renderer
import javax.microedition.khronos.opengles.GL10
import kotlin.math.absoluteValue

sealed class GameObject(var image: Image) {

    var rect: Rect = Rect()
    var color: Int = Graphics.COLOR_ORIGINAL

    var x = 0
    var y = 0
    var w = 0
    var h = 0
    var r = 0

    var dx = 0
    var dy = 0
    var dr = 0

    var destX = 0
    var destY = 0
    var destW = 0
    var destH = 0
    var destR = 0

    var delayF = 0
    var remF = 0

    open fun realisticMovement() {
        if (dx.absoluteValue > 5 || dy.absoluteValue > 5) {
            x += dx
            y += dy
            r += dr

            if (x < 0) {
                x = 0
                dx = -dx
            }

            if (y < 0) {
                y = 0
                dy = -dy
            }

            if (x > C.deviceWidth - w) {
                x = C.deviceWidth - w
                dx = -dx
            }

            if (y > C.deviceHeight - h) {
                y = C.deviceHeight - h
                dy = -dy
            }

            if (dx > 0) {
                dx--
            } else if (dx < 0) {
                dx++
            }

            if (dy > 0) {
                dy--
            } else if (dy < 0) {
                dy++
            }

            if (dr > 0) {
                dr--
            } else if (dr < 0) {
                dr++
            }

        } else {
            stopMovement()
        }
    }

    fun linearMovementToDestination() {
        if (remF > 1) {
            if (delayF > 0) {
                delayF--
            } else {
                if (x != destX) x += (destX - x) / remF
                if (y != destY) y += (destY - y) / remF
                if (w != destW) w += (destW - w) / remF
                if (h != destH) h += (destH - h) / remF
                if (r != destR) r += (destR - r) / remF
                remF--
            }
        } else {
            completeMovement()
        }
    }

    fun stopMovement() {
        destX = x
        destY = y
        destH = h
        destW = w

        dx = 0
        dy = 0
        dr = 0

        delayF = 0
        remF = 0
    }


    fun completeMovement() {
        x = destX
        y = destY
        w = destW
        h = destH
        r = destR

        dx = 0
        dy = 0

        delayF = 0
        remF = 0
    }

    fun draw(gl: GL10, renderer: Renderer) {

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
