package com.ardovic.farkle.dice.delegates

import com.ardovic.farkle.dice.MainActivity
import android.widget.FrameLayout
import android.view.WindowManager
import com.ardovic.farkle.dice.C
import com.ardovic.farkle.dice.opengl.FontParams
import android.opengl.GLSurfaceView
import android.graphics.PixelFormat
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.ardovic.farkle.dice.R
import com.ardovic.farkle.dice.graphics.Graphics
import com.ardovic.farkle.dice.opengl.Renderer
import java.util.ArrayList

object SystemDelegate {

    fun onCreateBgViewReturn(activity: MainActivity, rootLayout: FrameLayout) {
        activity.window.setBackgroundDrawable(null)
        val window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.navigationBarColor = Color.TRANSPARENT

        val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val point = Point()
        display.getRealSize(point)
        val width = point.x
        val height = point.y
        C.initialize(
            width,
            height,
            activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        )

        val renderer = Renderer(
            activity,
            intArrayOf(
                Graphics.SPRITE_MAP,
                Graphics.SPRITE_MAP_1,
                Graphics.ROBOTO_FONT
            ),
            activity
        )
        renderer.setFontParams(
            Graphics.ROBOTO_FONT,
            FontParams().size(C.defaultTextSize).paddingY(C.defaultTextVerticalPadding)
        )
        val surfaceView = GLSurfaceView(activity)
        surfaceView.setZOrderOnTop(true)
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        surfaceView.holder.setFormat(PixelFormat.RGBA_8888)
        surfaceView.setRenderer(renderer)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        rootLayout.layoutParams = params
        rootLayout.addView(surfaceView, params)
        activity.setContentView(rootLayout)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, rootLayout).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}
