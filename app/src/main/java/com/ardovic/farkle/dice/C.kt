package com.ardovic.farkle.dice

object C {

    const val BUFFER_COUNT = 3

    var isPortraitOrientation: Boolean = true
    var deviceWidth: Int = 0
    var deviceHeight: Int = 0

    var defaultTextSize: Int = 40 // TODO
    var defaultTextVerticalPadding: Int = 10 // TODO

    var deviceCenterX: Int = 0
    var deviceCenterY: Int = 0

    var deviceThirdX: Int = 0
    var deviceTwoThirdsX: Int = 0

    var deviceSixthX: Int = 0
    var deviceFiveSixthsX: Int = 0

    var navBarHeight: Int = 0
    var statusBarHeight: Int = 0

    fun initialize(deviceWidth: Int, deviceHeight: Int, isPortraitOrientation: Boolean) {
        this.deviceWidth = deviceWidth
        this.deviceHeight = deviceHeight
        this.isPortraitOrientation = isPortraitOrientation

        deviceCenterX = deviceWidth / 2
        deviceCenterY = deviceHeight / 2

        deviceThirdX = deviceWidth / 3
        deviceTwoThirdsX = deviceWidth - deviceThirdX

        deviceSixthX = deviceWidth / 6
        deviceFiveSixthsX = deviceWidth - deviceSixthX

    }
}
