package com.github.nian430.minesweeper.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.github.nian430.minesweeper.common.model.BlockAction
import com.github.nian430.minesweeper.common.model.Block
import com.github.nian430.minesweeper.common.platform.boardBackground
import kotlin.math.min

private const val MAX_BLOCK_SIZE = 40f

@Composable
internal fun GameBoard(
    blocks: List<List<Block>>,
    onBlockAction: (row: Int, column: Int, action: BlockAction) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = boardBackground(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(Color.Black.copy(alpha = .5f), BlendMode.Darken)
        )

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
