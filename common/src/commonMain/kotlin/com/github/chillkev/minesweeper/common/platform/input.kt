package com.github.chillkev.minesweeper.common.platform

import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.github.chillkev.minesweeper.common.model.Block
import com.github.chillkev.minesweeper.common.model.BlockAction

internal expect fun Modifier.blockPointerInput(
    row: Int,
    column: Int,
    block: Block,
    hoverState: MutableState<Boolean>,
    onBlockAction: (row: Int, column: Int, action: BlockAction) -> Unit
): Modifier
