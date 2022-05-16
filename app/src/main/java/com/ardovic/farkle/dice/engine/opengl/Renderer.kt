package com.ardovic.farkle.dice.engine.opengl

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.util.SparseArray
import com.ardovic.farkle.dice.graphics.Graphics
import com.ardovic.farkle.dice.graphics.Image
import java.lang.ClassCastException
import java.lang.Exception
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.ArrayList
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Renderer(private val context: Context, resourceIds: IntArray, private val frameDrawer: FrameDrawer) :
    GLSurfaceView.Renderer {

    private var lastTime: Long = 0
    private var texturesByResourceId: SparseArray<Texture> = SparseArray<Texture>()
    private var drawOrder: ArrayList<Texture> = ArrayList()

    init {
        setUpTextureObjects(context, resourceIds)
    }

    private fun setUpTextureObjects(context: Context, resourceIds: IntArray) {
        var filePath: String
        var texture: Texture
        val resourceIdsLength = resourceIds.size
        for (i in 0 until resourceIdsLength) {
            val resourceId = resourceIds[i]
            try {
                if (context.resources.getResourceTypeName(resourceId) == DRAWABLE) {
                    texture = BasicTexture(resourceId)
                    texturesByResourceId.put(resourceId, texture)
                    drawOrder.add(texture)
                } else if (context.resources.getResourceTypeName(resourceId) == STRING) {
                    filePath = context.resources.getString(resourceId)
                    if (filePath.substring(0, 5) == "fonts") {
                        var tf: Typeface
                        try {
                            tf = Typeface.createFromAsset(context.resources.assets, filePath)
                            if (tf == null) {
                                throw Exception()
                            }
                        } catch (ignored: Exception) {
                            continue
                        }
                        val fontTexture = FontTexture(tf)
                        texturesByResourceId.put(resourceId, fontTexture)
                        drawOrder.add(fontTexture)
                    }
                }
            } catch (ignored: Resources.NotFoundException) {
            }
        }
    }

    override fun onDrawFrame(gl: GL10) {
        val elapsed = System.currentTimeMillis() - lastTime
        val minElapsed = 1000 / 60
        if (elapsed < minElapsed) {
            try {
                Thread.sleep(minElapsed - elapsed)
            } catch (ignored: InterruptedException) {
            }
        }
        lastTime = System.currentTimeMillis()
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        gl.glLoadIdentity()
        gl.glRotatef(-180f, 1f, 0f, 0f)
        frameDrawer.onDrawFrame(gl, this)
        batchDraw(gl)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        gl.glViewport(0, 0, width, height)
        gl.glMatrixMode(GL10.GL_PROJECTION)
        gl.glLoadIdentity()
        gl.glOrthof(0f, width.toFloat(), -height.toFloat(), 0f, -1f, 8f)
        gl.glMatrixMode(GL10.GL_MODELVIEW)
        gl.glLoadIdentity()
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        gl.glClearColor(0f, 0f, 0f, 0f)
        gl.glEnable(GL10.GL_CULL_FACE)
        gl.glCullFace(GL10.GL_BACK)
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glEnable(GL10.GL_TEXTURE_2D)
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
        gl.glEnable(GL10.GL_BLEND)
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA)
        val textureIds = IntArray(texturesByResourceId.size())
        gl.glGenTextures(textureIds.size, textureIds, 0)
        var currentTexture: Texture
        val texturesByResourceIdSize: Int = texturesByResourceId.size()
        for (i in 0 until texturesByResourceIdSize) {
            currentTexture = texturesByResourceId.valueAt(i)
            currentTexture.textureId = textureIds[i]
            addTexture(gl, context, currentTexture, textureIds[i])
        }
    }

    fun batchDraw(gl: GL10) {
        var currentTexture: Texture
        val drawOrderSize = drawOrder.size
        for (i in 0 until drawOrderSize) {
            currentTexture = drawOrder[i]
            val array: SparseArray<LayerData> = currentTexture.spriteData
            val arraySize: Int = array.size()
            for (j in 0 until arraySize) {
                val currentLayerData: LayerData = array.valueAt(j)
                val vertices = currentLayerData.getVertices()
                val indices = currentLayerData.getIndices()
                val textureCoords = currentLayerData.textureCoordinates
                if (vertices != null && indices != null && textureCoords != null) {
                    val vbb = ByteBuffer.allocateDirect(vertices.size * 4)
                    vbb.order(ByteOrder.nativeOrder())
                    val vertexBuffer = vbb.asFloatBuffer()
                    vertexBuffer.put(vertices)
                    vertexBuffer.position(0)
                    val ibb = ByteBuffer.allocateDirect(indices.size * 2)
                    ibb.order(ByteOrder.nativeOrder())
                    val indexBuffer = ibb.asShortBuffer()
                    indexBuffer.put(indices)
                    indexBuffer.position(0)
                    val tbb = ByteBuffer.allocateDirect(textureCoords.size * 4)
                    tbb.order(ByteOrder.nativeOrder())
                    val textureBuffer = tbb.asFloatBuffer()
                    textureBuffer.put(textureCoords)
                    textureBuffer.position(0)
                    val color = currentLayerData.argb
                    val r = Color.red(color).toFloat() / 255
                    val g = Color.green(color).toFloat() / 255
                    val b = Color.blue(color).toFloat() / 255
                    val a = Color.alpha(color).toFloat() / 255
                    gl.glColor4f(r, g, b, a)
                    gl.glBindTexture(GL10.GL_TEXTURE_2D, currentTexture.textureId)
                    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer)
                    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer)
                    gl.glDrawElements(
                        GL10.GL_TRIANGLES,
                        indices.size,
                        GL10.GL_UNSIGNED_SHORT,
                        indexBuffer
                    )
                    currentLayerData.clear()
                }
            }
        }
    }

    private fun addTexture(gl: GL10, context: Context, texture: Texture, textureId: Int) {
        val bitmap: Bitmap = texture.getBitmap(context)
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId)
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR.toFloat())
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR.toFloat())
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT.toFloat())
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT.toFloat())
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0)
        texture.setDimensions(bitmap.width, bitmap.height)
        bitmap.recycle()
    }

    fun setFontParams(resourceId: Int, params: FontParams) {
        val texture: Texture = texturesByResourceId.get(resourceId)
        if (texture.javaClass == FontTexture::class.java) {
            val fontTexture = texture as FontTexture
            fontTexture.setParams(params)
        }
    }

    private fun draw(resourceId: Int, src: Rect, dst: Rect) {
        val texture: Texture = texturesByResourceId.get(resourceId)
        texture.addSprite(src, dst)
    }

    private fun draw(resourceId: Int, src: Rect, dst: Rect, angle: Int) {
        val texture: Texture = texturesByResourceId.get(resourceId)
        texture.addSprite(src, dst, angle)
    }

    private fun draw(resourceId: Int, src: Rect, dst: Rect, angle: Int, argb: Int) {
        val texture: Texture = texturesByResourceId.get(resourceId)
        texture.addSprite(src, dst, angle, argb)
    }

    fun drawText(resourceId: Int, text: String?, x: Int, y: Int, scale: Float, centered: Boolean) {
        drawText(resourceId, text, x, y, scale, Graphics.COLOR_DEFAULT, centered)
    }

    fun drawText(
        resourceId: Int,
        text: String?,
        x: Int,
        y: Int,
        scale: Float,
        argb: Int,
        centered: Boolean
    ) {
        val texture: Texture = texturesByResourceId.get(resourceId)
        val fontTexture: FontTexture = try {
            texture as FontTexture
        } catch (ignored: ClassCastException) {
            return
        }
        fontTexture.drawText(text!!, x, y, scale, argb, centered)
    }

    fun draw(image: Image, dstR: Rect) {
        draw(image.resID, image.srcRect, dstR)
    }

    fun draw(image: Image, dstR: Rect, d: Int) {
        draw(image.resID, image.srcRect, dstR, d)
    }

    fun draw(image: Image, dstR: Rect, d: Int, argb: Int) {
        draw(image.resID, image.srcRect, dstR, d, argb)
    }

    private companion object {
        const val DRAWABLE = "drawable"
        const val STRING = "string"
    }
}
