package com.ardovic.farkle.dice.graphics

import android.graphics.Color
import com.ardovic.farkle.dice.R

object Graphics {

    val SPRITE_MAP: Int = R.drawable.sprite_map

    val ROBOTO_FONT: Int = R.string.roboto_medium

    val COLOR_ORIGINAL = Color.argb(255, 255, 255, 255)
    val COLOR_SEMI_TRANSPARENT = Color.argb(200, 150, 150, 150)


    private val size = 32

    val sand = Image(1, 1, 30, 30, SPRITE_MAP)
    val metal = Image(2 * size, 2 * size, 32, 32, SPRITE_MAP)
    val green_tank_0 = Image(size, 0, 32, 32, SPRITE_MAP)
    val green_tank_1 = Image(2 * size, 0, 32, 32, SPRITE_MAP)
    val green_tank_2 = Image(3 * size, 0, 32, 32, SPRITE_MAP)
    val green_tank_3 = Image(4 * size, 0, 32, 32, SPRITE_MAP)
    val green_tank_4 = Image(5 * size, 0, 32, 32, SPRITE_MAP)
    val green_tank_5 = Image(6 * size, 0, 32, 32, SPRITE_MAP)
    val green_tank_6 = Image(7 * size, 0, 32, 32, SPRITE_MAP)

    val greenTankArray: Array<Image> =
        arrayOf(
            green_tank_0,
            green_tank_1,
            green_tank_2,
            green_tank_3,
            green_tank_4,
            green_tank_5,
            green_tank_6
        ).reversedArray()

}