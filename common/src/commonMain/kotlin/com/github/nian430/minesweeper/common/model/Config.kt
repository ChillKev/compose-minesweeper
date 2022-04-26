package com.github.nian430.minesweeper.common.model

interface Config {
    val rows: Int
    val columns: Int
    val mine: Int
    val name: String
}
