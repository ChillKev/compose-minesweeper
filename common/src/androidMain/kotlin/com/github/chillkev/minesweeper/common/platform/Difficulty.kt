package com.github.chillkev.minesweeper.common.platform

import com.github.chillkev.minesweeper.common.model.Config

internal actual enum class Difficulty(
    override val rows: Int,
    override val columns: Int,
    override val mine: Int
) : Config {
    Easy(10, 8, 10),
    Medium(13, 9, 20),
    Hard(16, 10, 35),
}
