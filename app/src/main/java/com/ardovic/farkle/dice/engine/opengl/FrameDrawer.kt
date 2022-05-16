package com.ardovic.farkle.dice.engine.opengl

import javax.microedition.khronos.opengles.GL10

interface FrameDrawer {

    fun onDrawFrame(gl: GL10, renderer: Renderer)
}
