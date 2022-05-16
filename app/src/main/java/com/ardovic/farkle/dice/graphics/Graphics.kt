package com.ardovic.farkle.dice.graphics

import android.graphics.Color
import com.ardovic.farkle.dice.R

object Graphics {

    val SPRITE_MAP: Int = R.drawable.sprite_map
    val SPRITE_MAP_1: Int = R.drawable.sprite_map_1

    val ROBOTO_FONT: Int = R.string.roboto_medium

    val COLOR_DEFAULT = Color.argb(255, 255, 255, 255)
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

    val tank_snails_0 = Image(0, 0, 50, 200, SPRITE_MAP_1)
    val tank_snails_1 = Image(50, 0, 50, 200, SPRITE_MAP_1)
    val tank_snails_2 = Image(100, 0, 50, 200, SPRITE_MAP_1)
    val tank_body_0 = Image(150, 0, 80, 150, SPRITE_MAP_1)
    val tank_gun_0 = Image(230, 0, 60, 180, SPRITE_MAP_1)

    val soldier_state_normal = Image(290, 0, 80, 80, SPRITE_MAP_1).mirrorVertical()
    val soldier_state_aiming = Image(370, 0, 80, 80, SPRITE_MAP_1).mirrorVertical()
    val soldier_leg_right = Image(450, 60, 24, 60, SPRITE_MAP_1)
    val soldier_leg_left = Image(450, 60, 24, 60, SPRITE_MAP_1).mirrorHorizontal()
    val barrel_side = Image(450, 0, 80, 60, SPRITE_MAP_1)
    val barrel_top = Image(530, 0, 60, 60, SPRITE_MAP_1)
    val barrel_flammable_sign = Image(590, 0, 40, 40, SPRITE_MAP_1)

    val wall = Image(0, 2 * size, 32, 32, SPRITE_MAP)

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