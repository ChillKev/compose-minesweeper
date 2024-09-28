package com.github.chillkev.minesweeper.common.stage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.chillkev.minesweeper.common.model.Block
import com.github.chillkev.minesweeper.common.model.BlockAction
import kotlin.math.min

private const val MAX_BLOCK_SIZE = 40f

@Composable
internal fun GameBoard(
    blocks: List<List<Block>>,
    onBlockAction: (row: Int, column: Int, action: BlockAction) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val rowCount = blocks.size
        val columnCount = blocks.firstOrNull()?.size ?: 1
        val blockHeight = maxHeight.value / rowCount
        val blockWidth = maxWidth.value / columnCount
        val blockSize = min(blockHeight, blockWidth).coerceAtMost(MAX_BLOCK_SIZE).dp

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            blocks.forEachIndexed { row, rows ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    rows.forEachIndexed { column, block ->
                        BlockView(row, column, block, Modifier.size(blockSize), onBlockAction)
                    }
                }
            }
        }
    }
}
