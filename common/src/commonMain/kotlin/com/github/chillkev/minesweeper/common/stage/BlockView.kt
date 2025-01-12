package com.github.chillkev.minesweeper.common.stage

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.chillkev.minesweeper.common.generated.resources.Res
import com.github.chillkev.minesweeper.common.generated.resources.ic_explosion
import com.github.chillkev.minesweeper.common.generated.resources.ic_flag
import com.github.chillkev.minesweeper.common.model.Block
import com.github.chillkev.minesweeper.common.model.BlockAction
import com.github.chillkev.minesweeper.common.model.Content
import com.github.chillkev.minesweeper.common.platform.blockPointerInput
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun BlockView(
    row: Int,
    column: Int,
    block: Block,
    modifier: Modifier = Modifier,
    onBlockAction: (row: Int, column: Int, action: BlockAction) -> Unit,
) {
    val isReveal = block is Block.Reveal
    val isHover = remember { mutableStateOf(false) }
    val showHover = when (block) {
        Block.Hidden, Block.Flag -> true
        is Block.Reveal -> block.content is Content.Indicator
    }
    val isDarkBackground = (row + column) % 2 == 0

    val blockColor = if (showHover && isHover.value) {
        if (isDarkBackground) Color(225, 202, 179) else Color(236, 209, 183)
    } else {
        if (isDarkBackground) Color(215, 184, 153) else Color(229, 194, 159)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.background(blockColor)
            .blockPointerInput(row, column, block, isHover, onBlockAction)
    ) {
        if (block is Block.Reveal) {
            when (val content = block.content) {
                Content.Empty -> {
                    // do nothing
                }

                is Content.Indicator -> {
                    val textColor = when (content) {
                        Content.Indicator.One -> Color(25, 118, 210)
                        Content.Indicator.Two -> Color(56, 142, 60)
                        Content.Indicator.Three -> Color(211, 47, 47)
                        Content.Indicator.Four -> Color(123, 31, 162)
                        Content.Indicator.Five -> Color(255, 144, 1)
                        Content.Indicator.Six -> Color(78, 191, 50)
                        Content.Indicator.Seven -> Color(232, 118, 19)
                        Content.Indicator.Eight -> Color(130, 13, 219)
                    }
                    Text(
                        text = content.value.toString(),
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Content.Mine -> {
                    Image(
                        painter = painterResource(Res.drawable.ic_explosion),
                        contentDescription = null
                    )
                }
            }
        }

        val revealProgress by animateFloatAsState(
            targetValue = if (isReveal) 1f else 0f,
            animationSpec = tween(durationMillis = 1000),
        )
        val coverColor = if (showHover && isHover.value) {
            if (isDarkBackground) Color(185, 221, 119) else Color(191, 225, 125)
        } else {
            if (isDarkBackground) Color(162, 209, 73) else Color(170, 215, 81)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(1 - revealProgress)
                .background(coverColor)
        )

        if (block is Block.Flag) {
            Image(
                painter = painterResource(Res.drawable.ic_flag),
                contentDescription = null,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
