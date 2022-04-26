package com.github.nian430.minesweeper.common.model

import kotlin.time.Duration

sealed class Progress {
    object Pending : Progress()
    data class OnGoing(val startTimeInMillis: Long) : Progress()
    data class Finish(val isWin: Boolean, val timeSpend: Duration, val highScore: Duration): Progress()
}
