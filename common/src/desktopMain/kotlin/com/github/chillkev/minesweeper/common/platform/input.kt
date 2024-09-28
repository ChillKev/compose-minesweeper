package com.github.chillkev.minesweeper.common.platform

import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import com.github.chillkev.minesweeper.common.model.Block
import com.github.chillkev.minesweeper.common.model.BlockAction
import com.github.chillkev.minesweeper.common.model.Content

internal actual fun Modifier.blockPointerInput(
    row: Int,
    column: Int,
    block: Block,
    hoverState: MutableState<Boolean>,
    onBlockAction: (row: Int, column: Int, action: BlockAction) -> Unit
): Modifier {
    return this.pointerInput(block) {
        awaitPointerEventScope {
            // use these two flags to determined which button is release
            var isPrimaryPressed = false
            var isSecondaryPressed = false

            while (true) {
                val event = awaitPointerEvent()
                when (event.type) {
                    PointerEventType.Press -> {
                        val buttons = event.buttons
                        isPrimaryPressed = buttons.isPrimaryPressed
                        isSecondaryPressed = buttons.isSecondaryPressed
                    }
                    PointerEventType.Release -> {
                        val buttons = event.buttons
                        val isPrimaryTap = isPrimaryPressed != buttons.isPrimaryPressed
                        val isSecondaryTap = isSecondaryPressed != buttons.isSecondaryPressed
                        if (hoverState.value) {
                            when (block) {
                                Block.Hidden -> {
                                    if (isPrimaryTap && !isSecondaryPressed) {
                                        onBlockAction(row, column, BlockAction.Dig)
                                    } else if (!isPrimaryPressed && isSecondaryTap) {
                                        onBlockAction(row, column, BlockAction.Flag)
                                    }
                                }
                                Block.Flag -> {
                                    if (!isPrimaryPressed && isSecondaryTap) {
                                        onBlockAction(row, column, BlockAction.Flag)
                                    }
                                }
                                is Block.Reveal -> {
                                    when (block.content) {
                                        is Content.Indicator -> {
                                            if (isPrimaryTap && isSecondaryPressed) {
                                                onBlockAction(row, column, BlockAction.DigAround)
                                            }
                                        }
                                        else -> {
                                            // do nothing
                                        }
                                    }
                                }
                            }
                        }
                        isPrimaryPressed = buttons.isPrimaryPressed
                        isSecondaryPressed = buttons.isSecondaryPressed
                    }
                    PointerEventType.Move -> {
                        // do nothing
                    }
                    PointerEventType.Enter -> {
                        hoverState.value = true
                    }
                    PointerEventType.Exit -> {
                        hoverState.value = false
                    }
                }
            }
        }
    }
}