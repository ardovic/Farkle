package com.ardovic.farkle.dice

import android.content.res.Configuration

object C {

    const val BUFFER_COUNT = 3

    var isPortraitOrientation: Boolean = true
    var deviceWidth: Int = 0
    var deviceHeight: Int = 0

    var defaultTextSize: Int = 40 // TODO
    var defaultTextVerticalPadding: Int = 10 // TODO


    fun initialize(deviceWidth: Int, deviceHeight: Int, isPortraitOrientation: Boolean) {
        this.deviceWidth = deviceWidth
        this.deviceHeight = deviceHeight
        this.isPortraitOrientation = isPortraitOrientation


    }

}
