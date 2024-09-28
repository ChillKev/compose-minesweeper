package com.github.chillkev.minesweeper.common.model

sealed class Block {
    data object Hidden : Block()
    data object Flag : Block()
    data class Reveal(val content: Content) : Block()
}
