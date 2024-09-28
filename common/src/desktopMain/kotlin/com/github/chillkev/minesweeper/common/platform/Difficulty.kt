package com.github.chillkev.minesweeper.common.platform

import com.github.chillkev.minesweeper.common.model.Config

internal actual enum class Difficulty(
    override val rows: Int,
    override val columns: Int,
    override val mine: Int
) : Config {
    Easy(8, 10, 10),
    Medium(14, 18, 40),
    Hard(20, 24, 99),
}
