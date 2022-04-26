package com.github.nian430.minesweeper.common.platform

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.github.nian430.minesweeper.common.model.Content
import com.github.nian430.minesweeper.common.model.BlockAction
import com.github.nian430.minesweeper.common.model.Block

internal actual fun Modifier.blockPointerInput(
    row: Int,
    column: Int,
    block: Block,
    hoverState: MutableState<Boolean>,
    onBlockAction: (row: Int, column: Int, action: BlockAction) -> Unit
): Modifier {
    return this.pointerInput(block) {
        detectTapGestures(
            onLongPress = {
                when (block) {
                    Block.Hidden -> {
                        onBlockAction(row, column, BlockAction.Flag)
                    }
                    Block.Flag -> {
                        onBlockAction(row, column, BlockAction.Flag)
                    }
                    is Block.Reveal -> {
                        when (block.content) {
                            is Content.Indicator -> {
                                onBlockAction(row, column, BlockAction.DigAround)
                            }
                            else -> {
                                // do nothing
                            }
                        }
                    }
                }
            }, onTap = {
                when (block) {
                    Block.Hidden -> {
                        onBlockAction(row, column, BlockAction.Dig)
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
        )
    }
}
