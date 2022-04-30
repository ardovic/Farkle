package com.ardovic.farkle.dice.opengl

import javax.microedition.khronos.opengles.GL10

interface Drawer {

    fun onDrawFrame(gl: GL10, renderer: Renderer)
}
