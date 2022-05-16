package com.ardovic.farkle.dice.engine

import android.graphics.Rect
import com.ardovic.farkle.dice.engine.opengl.Renderer

interface Drawable {

    fun draw(renderer: Renderer)
}
