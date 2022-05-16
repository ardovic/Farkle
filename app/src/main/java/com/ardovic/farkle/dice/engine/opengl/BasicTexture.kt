package com.ardovic.farkle.dice.engine.opengl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class BasicTexture constructor(private val bitmapResId: Int) : Texture() {

    override fun getBitmap(context: Context): Bitmap =
        BitmapFactory.decodeResource(context.resources, bitmapResId)
}
