package com.ardovic.farkle.dice.opengl

import android.content.Context
import android.graphics.*
import android.util.SparseArray
import android.util.SparseIntArray
import com.ardovic.farkle.dice.graphics.Graphics
import java.util.ArrayList
import kotlin.math.abs
import kotlin.math.ceil

class FontTexture(private val typeface: Typeface) : Texture() {

    private val charWidths: SparseIntArray = SparseIntArray()
    private val charRects: SparseArray<Rect> = SparseArray()
    private var charStart = ArrayList<Int>()
    private var charEnd = ArrayList<Int>()
    private var cellWidth = 0
    private var cellHeight = 0
    private var size = 24
    private var charUnknown = 32
    private var paddingX = 2
    private var paddingY = 4

    init {
        charStart.add(32)
        charEnd.add(126)
    }

    fun setParams(params: FontParams) {
        size = params.size
        charStart = params.charStart
        charEnd = params.charEnd
        charUnknown = params.charUnknown
        paddingX = params.paddingX
        paddingY = params.paddingY
    }

    fun drawText(text: String, refX: Int, refY: Int, scale: Float, argb: Int, centered: Boolean) {
        var x = refX
        var y = refY
        var textWidth = 0
        var charWidth = 0
        val textLength = text.length
        for (i in 0 until textLength) {
            charWidth = charWidths.get(text[i].code)
            textWidth += charWidth
        }
        val scaledCellWidth = (scale * cellWidth).toInt()
        val scaledCellHeight = (scale * cellHeight).toInt()
        val scaledCellSpacing = ((scale - 1) * charWidth).toInt()
        if (centered) {
            x -= paddingX + textWidth / 2 + text.length * scaledCellSpacing / 2
        }
        y -= scaledCellHeight / 2
        var src: Rect
        val dst = Rect()
        var charNumber: Int
        for (element in text) {
            charNumber = element.code
            src = charRects.get(charNumber)
            charWidth = charWidths.get(charNumber)
            if (src == null) {
                src = charRects.get(text[charUnknown].code)
                charWidth = charWidths.get(text[charUnknown].code)
            }
            dst[x, y, x + scaledCellWidth] = y + scaledCellHeight
            getARGBSpriteData(argb).addSprite(src, dst)
            x += charWidth + scaledCellSpacing
        }
    }

    override fun getBitmap(context: Context): Bitmap {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.typeface = typeface
        paint.textSize = size.toFloat()
        paint.color = Graphics.COLOR_ORIGINAL
        paint.textAlign = Paint.Align.LEFT
        val fm = paint.fontMetrics
        val fontHeight = ceil((abs(fm.bottom) + abs(fm.top)).toDouble()).toInt()
        charWidths.clear()
        val s = CharArray(2)
        val w = FloatArray(2)
        var charWidthMax = 0
        var charWidth: Int
        val charStartListSize = charStart.size
        for (i in 0 until charStartListSize) {
            val startChar = charStart[i].toChar()
            val endChar = charEnd[i].toChar()
            var c = startChar
            while (c <= endChar) {
                s[0] = c
                paint.getTextWidths(s, 0, 1, w)
                charWidth = ceil(w[0].toDouble()).toInt()
                charWidths.put(c.code, charWidth)
                if (charWidth > charWidthMax) {
                    charWidthMax = charWidth
                }
                c++
            }
        }
        cellWidth = charWidthMax + 2 * paddingX
        cellHeight = fontHeight + paddingY
        val maxSize = cellWidth.coerceAtLeast(cellHeight)
        val textureSize: Int = when {
            maxSize <= 24 -> 256
            maxSize <= 40 -> 512
            maxSize <= 80 -> 1024
            maxSize <= 160 -> 2048
            else -> 4096
        }
        val bitmap: Bitmap = Bitmap.createBitmap(textureSize, textureSize, Bitmap.Config.ALPHA_8)
        val canvas = Canvas(bitmap)
        bitmap.eraseColor(COLOR_NONE)
        charRects.clear()
        var x = 0
        var y = 0
        for (i in 0 until charStartListSize) {
            val startChar = charStart[i].toChar()
            val endChar = charEnd[i].toChar()
            var c = startChar
            while (c <= endChar) {
                s[0] = c
                canvas.drawText(
                    s,
                    0,
                    1,
                    (x + paddingX).toFloat(),
                    (y + cellHeight - paddingY).toFloat(),
                    paint
                )
                charRects.put(c.code, Rect(x, y, x + cellWidth, y + cellHeight))
                x += cellWidth
                if (x + cellWidth > textureSize) {
                    x = 0
                    y += cellHeight
                }
                c++
            }
        }
        return bitmap
    }

    private companion object {
        val COLOR_NONE = Color.argb(0, 0, 0, 0)
    }
}
