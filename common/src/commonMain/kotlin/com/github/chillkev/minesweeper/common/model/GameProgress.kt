package com.github.chillkev.minesweeper.common.model

import kotlin.time.Duration

sealed class GameProgress {
    data object Pending : GameProgress()

    data class OnGoing(
        val startTimeInMillis: Long
    ) : GameProgress()

    data class Finish(
        val isWin: Boolean,
        val timeSpend: Duration,
        val highScore: Duration,
        val isNewHighScore: Boolean
    ) : GameProgress()
}
