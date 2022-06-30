package com.ardovic.farkle.dice

sealed class GameState {

    sealed class RoamState : GameState() {

        object MicroControlsState : RoamState()

        object MacroControlsState : RoamState()

    }
}