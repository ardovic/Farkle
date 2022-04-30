package com.ardovic.farkle.dice.graphics

import android.graphics.Rect

class Image constructor(leftX: Int, topY: Int, width: Int, height: Int, resID: Int) {

    var srcRect: Rect
    var resID: Int

    init {
        srcRect = Rect(leftX, topY, leftX + width, topY + height)
        this.resID = resID
    }

    private fun mirrorHorizontal(): Image {
        val temp = srcRect.right
        srcRect.right = srcRect.left
        srcRect.left = temp
        return this
    }

    private fun mirrorVertical(): Image {
        val temp = srcRect.bottom
        srcRect.bottom = srcRect.top
        srcRect.top = temp
        return this
    }
}
