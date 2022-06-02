package com.ardovic.farkle.dice.game.util

import java.util.*

class EvictingQueue<T>(var maxSize: Int) : LinkedList<T>() {

    override fun add(element: T): Boolean {
        if (size == maxSize) {
            removeLast()
        }
        super.addFirst(element)
        return true
    }
}
