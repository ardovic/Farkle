package com.ardovic.farkle.dice

import android.app.Application
import android.content.SharedPreferences

class App : Application() {

    lateinit var prefsPlayer: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        instance = this
        prefsPlayer = getSharedPreferences(PREFS_PLAYER, MODE_PRIVATE)
    }

    companion object {
        lateinit var instance: App
        private const val PREFS_PLAYER = "prefs_player"
    }
}
