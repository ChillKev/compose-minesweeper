package com.github.nian430.minesweeper.common.platform

import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.github.nian430.minesweeper.common.model.BlockAction
import com.github.nian430.minesweeper.common.model.Block

internal expect fun Modifier.blockPointerInput(
    row: Int,
    column: Int,
    block: Block,
    hoverState: MutableState<Boolean>,
    onBlockAction: (row: Int, column: Int, action: BlockAction) -> Unit
): Modifier
