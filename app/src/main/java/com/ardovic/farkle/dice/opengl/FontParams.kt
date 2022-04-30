package com.ardovic.farkle.dice.opengl

import java.util.ArrayList

class FontParams {

    var size = 24
        private set
    val charStart = ArrayList<Int>()
    val charEnd = ArrayList<Int>()
    var charUnknown = 32
        private set
    var paddingX = 2
        private set
    var paddingY = 2
        private set

    init {
        charStart.add(32)
        charStart.add(210)
        charStart.add(1040)
        charEnd.add(126)
        charEnd.add(220)
        charEnd.add(1080)
    }

    fun size(size: Int): FontParams {
        this.size = size
        return this
    }

    fun charStart(charStart: Int): FontParams {
        this.charStart.add(charStart)
        return this
    }

    fun charEnd(charEnd: Int): FontParams {
        this.charEnd.add(charEnd)
        return this
    }

    fun charUnknown(charUnknown: Int): FontParams {
        this.charUnknown = charUnknown
        return this
    }

    fun paddingX(paddingX: Int): FontParams {
        this.paddingX = paddingX
        return this
    }

    fun paddingY(paddingY: Int): FontParams {
        this.paddingY = paddingY
        return this
    }
}
