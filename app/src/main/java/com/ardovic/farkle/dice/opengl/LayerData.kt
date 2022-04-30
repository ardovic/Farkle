package com.ardovic.farkle.dice.opengl

import android.graphics.Rect
import java.util.ArrayList

class LayerData internal constructor(argb: Int) {
    private val vertices: ArrayList<Float>
    private val indices: ArrayList<Short>
    private val coordinates: ArrayList<Float>
    private var textureWidth = 0
    private var textureHeight = 0
    val aRGB: Int
    fun setDimensions(width: Int, height: Int) {
        textureWidth = width
        textureHeight = height
    }

    protected fun addVertices(vertices: FloatArray) {
        val arrayLength = vertices.size
        for (i in 0 until arrayLength) {
            this.vertices.add(vertices[i])
        }
    }

    protected fun addIndices(indices: ShortArray) {
        val arrayLength = indices.size
        for (i in 0 until arrayLength) {
            this.indices.add(indices[i])
        }
    }

    protected fun addCoordinates(coordinates: FloatArray) {
        val arrayLength = coordinates.size
        for (i in 0 until arrayLength) {
            this.coordinates.add(coordinates[i])
        }
    }

    fun addSprite(src: Rect, dst: Rect) {
        vertices.add(dst.left.toFloat())
        vertices.add(dst.top.toFloat())
        vertices.add(0f)
        vertices.add(dst.left.toFloat())
        vertices.add(dst.bottom.toFloat())
        vertices.add(0f)
        vertices.add(dst.right.toFloat())
        vertices.add(dst.bottom.toFloat())
        vertices.add(0f)
        vertices.add(dst.right.toFloat())
        vertices.add(dst.top.toFloat())
        vertices.add(0f)
        val lastValue = if (indices.isEmpty()) -1 else indices[indices.size - 1]
        indices.add((lastValue + 1).toShort())
        indices.add((lastValue + 2).toShort())
        indices.add((lastValue + 3).toShort())
        indices.add((lastValue + 1).toShort())
        indices.add((lastValue + 3).toShort())
        indices.add((lastValue + 4).toShort())
        val srcX = floatArrayOf(
            src.left.toFloat(),
            src.left.toFloat(),
            src.right.toFloat(),
            src.right.toFloat()
        )
        val srcY = floatArrayOf(
            src.top.toFloat(),
            src.bottom.toFloat(),
            src.bottom.toFloat(),
            src.top.toFloat()
        )
        for (i in 0..3) {
            coordinates.add(srcX[i] / textureWidth)
            coordinates.add(srcY[i] / textureHeight)
        }
    }

    fun addSprite(src: Rect, dst: Rect, angle: Int) {
        val cos = Math.cos(angle.toDouble() / 180 * Math.PI)
        val sin = Math.sin(angle.toDouble() / 180 * Math.PI)
        val halfWidth = (dst.right - dst.left) / 2f
        val halfHeight = (dst.top - dst.bottom) / 2f
        val hotX = floatArrayOf(-halfWidth, -halfWidth, halfWidth, halfWidth)
        val hotY = floatArrayOf(halfHeight, -halfHeight, -halfHeight, halfHeight)
        for (i in 0..3) {
            var transformedX = (cos * hotX[i] - sin * hotY[i]).toFloat()
            var transformedY = (sin * hotX[i] + cos * hotY[i]).toFloat()
            transformedX += dst.left + halfWidth
            transformedY += dst.bottom + halfHeight
            vertices.add(transformedX)
            vertices.add(transformedY)
            vertices.add(0f)
        }
        val lastValue = if (indices.isEmpty()) -1 else indices[indices.size - 1]
        indices.add((lastValue + 1).toShort())
        indices.add((lastValue + 2).toShort())
        indices.add((lastValue + 3).toShort())
        indices.add((lastValue + 1).toShort())
        indices.add((lastValue + 3).toShort())
        indices.add((lastValue + 4).toShort())
        val srcX = floatArrayOf(
            src.left.toFloat(),
            src.left.toFloat(),
            src.right.toFloat(),
            src.right.toFloat()
        )
        val srcY = floatArrayOf(
            src.top.toFloat(),
            src.bottom.toFloat(),
            src.bottom.toFloat(),
            src.top.toFloat()
        )
        for (i in 0..3) {
            coordinates.add(srcX[i] / textureWidth)
            coordinates.add(srcY[i] / textureHeight)
        }
    }

    fun addSprite(
        src: Rect,
        drawX: Int,
        drawY: Int,
        hotRect: Rect,
        angle: Int,
        sizeX: Float,
        sizeY: Float
    ) {
        val cos = Math.cos(angle.toDouble() / 180 * Math.PI)
        val sin = Math.sin(angle.toDouble() / 180 * Math.PI)
        val hotX = floatArrayOf(
            hotRect.left.toFloat(),
            hotRect.left.toFloat(),
            hotRect.right.toFloat(),
            hotRect.right.toFloat()
        )
        val hotY = floatArrayOf(
            hotRect.top.toFloat(),
            hotRect.bottom.toFloat(),
            hotRect.bottom.toFloat(),
            hotRect.top.toFloat()
        )
        for (i in 0..3) {
            val x = hotX[i] * sizeX
            val y = hotY[i] * sizeY
            var transformedX = (cos * x - sin * y).toFloat()
            var transformedY = (sin * x + cos * y).toFloat()
            transformedX += drawX.toFloat()
            transformedY += drawY.toFloat()
            vertices.add(transformedX)
            vertices.add(transformedY)
            vertices.add(0f)
        }
        val lastValue = if (indices.isEmpty()) -1 else indices[indices.size - 1]
        indices.add((lastValue + 1).toShort())
        indices.add((lastValue + 2).toShort())
        indices.add((lastValue + 3).toShort())
        indices.add((lastValue + 1).toShort())
        indices.add((lastValue + 3).toShort())
        indices.add((lastValue + 4).toShort())
        val srcX =
            floatArrayOf(src.left + 0.5f, src.left + 0.5f, src.right - 0.5f, src.right - 0.5f)
        val srcY =
            floatArrayOf(src.top + 0.5f, src.bottom - 0.5f, src.bottom - 0.5f, src.top + 0.5f)
        for (i in 0..3) {
            coordinates.add(srcX[i] / textureWidth)
            coordinates.add(srcY[i] / textureHeight)
        }
    }

    fun drawLine(src: Rect, x1: Int, y1: Int, x2: Int, y2: Int, width: Int) {
        val angle = Math.atan2((y2 - y1).toDouble(), (x2 - x1).toDouble())
        val sinAngleOffset = (Math.sin(angle) * width / 2).toInt()
        val cosAngleOffset = (Math.cos(angle) * width / 2).toInt()
        vertices.add((x1 + sinAngleOffset).toFloat())
        vertices.add((y1 - cosAngleOffset).toFloat())
        vertices.add(0f)
        vertices.add((x1 - sinAngleOffset).toFloat())
        vertices.add((y1 + cosAngleOffset).toFloat())
        vertices.add(0f)
        vertices.add((x2 - sinAngleOffset).toFloat())
        vertices.add((y2 + cosAngleOffset).toFloat())
        vertices.add(0f)
        vertices.add((x2 + sinAngleOffset).toFloat())
        vertices.add((y2 - cosAngleOffset).toFloat())
        vertices.add(0f)
        val lastValue = if (indices.isEmpty()) -1 else indices[indices.size - 1]
        indices.add((lastValue + 1).toShort())
        indices.add((lastValue + 2).toShort())
        indices.add((lastValue + 3).toShort())
        indices.add((lastValue + 1).toShort())
        indices.add((lastValue + 3).toShort())
        indices.add((lastValue + 4).toShort())
        val length = Math.sqrt(((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)).toDouble())
            .toInt()
        val textureLength = src.right - src.left
        val nWrap = length / textureLength
        val u = floatArrayOf(0f, 0f, nWrap.toFloat(), nWrap.toFloat())
        val srcY = floatArrayOf(
            src.top.toFloat(),
            src.bottom.toFloat(),
            src.bottom.toFloat(),
            src.top.toFloat()
        )
        for (i in 0..3) {
            coordinates.add(u[i])
            coordinates.add(srcY[i] / textureHeight)
        }
    }

    fun drawTile(dst: Rect, offsetX: Int, offsetY: Int, scale: Float) {
        vertices.add(dst.left.toFloat())
        vertices.add(dst.top.toFloat())
        vertices.add(0f)
        vertices.add(dst.left.toFloat())
        vertices.add(dst.bottom.toFloat())
        vertices.add(0f)
        vertices.add(dst.right.toFloat())
        vertices.add(dst.bottom.toFloat())
        vertices.add(0f)
        vertices.add(dst.right.toFloat())
        vertices.add(dst.top.toFloat())
        vertices.add(0f)
        val lastValue = if (indices.isEmpty()) -1 else indices[indices.size - 1]
        indices.add((lastValue + 1).toShort())
        indices.add((lastValue + 2).toShort())
        indices.add((lastValue + 3).toShort())
        indices.add((lastValue + 1).toShort())
        indices.add((lastValue + 3).toShort())
        indices.add((lastValue + 4).toShort())
        val drawWidth = dst.right - dst.left
        val drawHeight = dst.bottom - dst.top
        val xWrap = drawWidth.toFloat() / textureWidth / scale
        val yWrap = drawHeight.toFloat() / textureHeight / scale
        val offsetU = offsetX.toFloat() / textureWidth
        val offsetV = offsetY.toFloat() / textureHeight
        val u = floatArrayOf(offsetU, offsetU, xWrap + offsetU, xWrap + offsetU)
        val v = floatArrayOf(offsetV, yWrap + offsetV, yWrap + offsetV, offsetV)
        for (i in 0..3) {
            coordinates.add(u[i])
            coordinates.add(v[i])
        }
    }

    fun clear() {
        vertices.clear()
        indices.clear()
        coordinates.clear()
    }

    fun getVertices(): FloatArray? {
        return convertToPrimitive(vertices.toTypedArray())
    }

    fun getIndices(): ShortArray? {
        return convertToPrimitive(indices.toTypedArray())
    }

    val textureCoordinates: FloatArray?
        get() = convertToPrimitive(coordinates.toTypedArray())

    private fun convertToPrimitive(objectArray: Array<Float>?): FloatArray? {
        if (objectArray == null || objectArray.size == 0) {
            return null
        }
        val objectArrayLength = objectArray.size
        val primitiveArray = FloatArray(objectArrayLength)
        for (i in 0 until objectArrayLength) {
            primitiveArray[i] = objectArray[i]
        }
        return primitiveArray
    }

    private fun convertToPrimitive(objectArray: Array<Short>?): ShortArray? {
        if (objectArray == null || objectArray.size == 0) {
            return null
        }
        val objectArrayLength = objectArray.size
        val primitiveArray = ShortArray(objectArrayLength)
        for (i in 0 until objectArrayLength) {
            primitiveArray[i] = objectArray[i]
        }
        return primitiveArray
    }

    init {
        vertices = ArrayList()
        indices = ArrayList()
        coordinates = ArrayList()
        aRGB = argb
    }
}