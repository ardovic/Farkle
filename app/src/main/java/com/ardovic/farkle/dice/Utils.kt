package com.ardovic.farkle.dice

import kotlin.random.Random

const val UPDATE_FRAME_COUNT = 60
const val LOGGING_ENABLED = true

fun generateUpdateFrame() = Random.Default.nextInt(UPDATE_FRAME_COUNT)
fun generateId() = Random.Default.nextInt()

fun logger(text: String) {
    if (LOGGING_ENABLED) {
        println("HEX: $text")
    }
}
