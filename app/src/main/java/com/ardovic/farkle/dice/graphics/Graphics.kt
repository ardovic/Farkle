package com.ardovic.farkle.dice.graphics

import android.graphics.Color
import com.ardovic.farkle.dice.R

object Graphics {

    val SPRITE_MAP_01: Int = R.drawable.sprite_map_01
    val SPRITE_MAP_02: Int = R.drawable.sprite_map_02
    val SPRITE_MAP_03: Int = R.drawable.sprite_map_03

    val ROBOTO_FONT: Int = R.string.roboto_medium

    val COLOR_ORIGINAL = Color.argb(255, 255, 255, 255)
    val COLOR_SEMI_TRANSPARENT = Color.argb(200, 150, 150, 150)


    val four = Image(0, 0, 100, 100, SPRITE_MAP_02)
    val three = Image(0, 800, 100, 100, SPRITE_MAP_02)

    var matrix: Array<Array<Image>> = Array(16) { x ->
        Array(9) { y ->
            when (y) {
                0 -> four
                8 -> three
                else -> Image(x * 100, y * 100, 100, 100, SPRITE_MAP_02)
            }
        }
    }

    val m_four = 0 to 0
    val m_three = 0 to 8
    val m_one = 0 to 4
    val m_two = 4 to 4
    val m_six = 8 to 4
    val m_five = 12 to 4


    val stableStates = listOf(m_one, m_two, m_three, m_four, m_five, m_six)

}