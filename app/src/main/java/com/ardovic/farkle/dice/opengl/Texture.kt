package com.ardovic.farkle.dice.opengl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.SparseArray
import com.ardovic.farkle.dice.graphics.Graphics

abstract class Texture {

    val spriteData: SparseArray<LayerData> = SparseArray<LayerData>()
    var textureId = 0
    private var width = 0
    private var height = 0

    fun setDimensions(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun addSprite(src: Rect, dst: Rect) {
        defaultSpriteData().addSprite(src, dst)
    }

    fun addSprite(src: Rect, dst: Rect, angle: Int) {
        defaultSpriteData().addSprite(src, dst, angle)
    }

    fun addSprite(src: Rect, dst: Rect, angle: Int, argb: Int) {
        getARGBSpriteData(argb).addSprite(src, dst, angle)
    }

    protected fun addSprite(
        src: Rect,
        drawX: Int,
        drawY: Int,
        hotRect: Rect,
        angle: Int,
        sizeX: Float,
        sizeY: Float
    ) {
        defaultSpriteData().addSprite(src, drawX, drawY, hotRect, angle, sizeX, sizeY)
    }

    protected fun addSprite(
        src: Rect?,
        drawX: Int,
        drawY: Int,
        hotRect: Rect?,
        angle: Int,
        sizeX: Float,
        sizeY: Float,
        argb: Int
    ) {
        getARGBSpriteData(argb).addSprite(src!!, drawX, drawY, hotRect!!, angle, sizeX, sizeY)
    }

    protected fun drawLine(src: Rect?, x1: Int, y1: Int, x2: Int, y2: Int, width: Int) {
        defaultSpriteData().drawLine(src!!, x1, y1, x2, y2, width)
    }

    protected fun drawTile(dst: Rect?, offsetX: Int, offsetY: Int, scale: Float) {
        defaultSpriteData().drawTile(dst!!, offsetX, offsetY, scale)
    }

    private fun defaultSpriteData(): LayerData {
        var layerData = spriteData[Graphics.COLOR_ORIGINAL]
        if (layerData == null) {
            layerData = LayerData(Graphics.COLOR_ORIGINAL)
            spriteData.put(Graphics.COLOR_ORIGINAL, layerData)
            layerData.setDimensions(width, height)
        }
        return layerData
    }

    fun getARGBSpriteData(argb: Int): LayerData {
        var layerData = spriteData[argb]
        if (layerData == null) {
            layerData = LayerData(argb)
            spriteData.put(argb, layerData)
            layerData.setDimensions(width, height)
        }
        return layerData
    }

    abstract fun getBitmap(context: Context): Bitmap
}
