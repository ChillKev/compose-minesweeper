package com.github.chillkev.minesweeper.common.model

interface Config {
    val rows: Int
    val columns: Int
    val mine: Int
    val name: String
}
