package com.github.nian430.minesweeper.common.model

sealed class Block {
    object Hidden : Block()
    object Flag : Block()
    data class Reveal(val content: Content) : Block()
}
