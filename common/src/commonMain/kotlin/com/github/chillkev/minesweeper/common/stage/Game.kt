package com.github.chillkev.minesweeper.common.stage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.chillkev.minesweeper.common.generated.resources.Res
import com.github.chillkev.minesweeper.common.generated.resources.ic_clock
import com.github.chillkev.minesweeper.common.generated.resources.ic_flag
import com.github.chillkev.minesweeper.common.model.Block
import com.github.chillkev.minesweeper.common.model.GameModel
import com.github.chillkev.minesweeper.common.model.GameProgress
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun Game(
    gameModel: GameModel,
    modifier: Modifier = Modifier
) {
    val config = gameModel.config.collectAsState()
    val progress = gameModel.progress.collectAsState()
    val blocks = gameModel.blocks.collectAsState()

    Column(
        modifier = modifier
    ) {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color(74, 117, 44)
        ) {
            Row(
                modifier = Modifier.fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_flag),
                    contentDescription = null
                )
                Spacer(Modifier.width(4.dp))
                val flagPlanted =
                    blocks.value.sumOf { it.count { minerBlock -> minerBlock is Block.Flag } }
                val mineLeft = config.value.mine - flagPlanted
                Text(
                    text = mineLeft.toString(),
                    fontWeight = FontWeight.W700,
                    color = Color.White,
                    fontSize = 20.sp
                )

                Spacer(Modifier.width(16.dp))

                Image(
                    painter = painterResource(Res.drawable.ic_clock),
                    contentDescription = null
                )
                Spacer(Modifier.width(4.dp))
                var timeProgressInSeconds by remember { mutableStateOf(0L) }
                LaunchedEffect(Unit) {
                    while (true) {
                        withFrameMillis {
                            timeProgressInSeconds =
                                when (val gameProgress = progress.value) {
                                    is GameProgress.Finish -> gameProgress.timeSpend.inWholeSeconds
                                    is GameProgress.OnGoing -> (System.currentTimeMillis() - gameProgress.startTimeInMillis).milliseconds.inWholeSeconds
                                    GameProgress.Pending -> 0
                                }.coerceAtMost(999)
                        }
                    }
                }

                Text(
                    text = timeProgressInSeconds.toString()
                        .padStart(3, '0'),
                    fontWeight = FontWeight.W700,
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }

        GameBoard(
            blocks = blocks.value,
            onBlockAction = gameModel::interact,
            modifier = Modifier.fillMaxSize()
        )
    }
}
