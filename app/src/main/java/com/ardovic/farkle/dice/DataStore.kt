package com.ardovic.farkle.dice

import androidx.annotation.DrawableRes

object DataStore {

    private const val BG_IMAGE_CODE = "bg_image_code"
    private const val DEFAULT_BG_IMAGE_CODE = 0

    var bgImageCode: Int = App.instance.prefsPlayer.getInt(BG_IMAGE_CODE, DEFAULT_BG_IMAGE_CODE)
}
