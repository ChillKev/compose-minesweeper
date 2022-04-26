package com.github.nian430.minesweeper.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.nian430.minesweeper.common.model.Block
import com.github.nian430.minesweeper.common.model.GameModel
import com.github.nian430.minesweeper.common.model.Progress
import com.github.nian430.minesweeper.common.platform.icClock
import com.github.nian430.minesweeper.common.platform.icFlag
import com.github.nian430.minesweeper.common.platform.icGithub
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun MinesweeperView(
    gameModel: GameModel,
    onGitHubClick: (repositoryUrl: String) -> Unit
) {
    val model = remember { gameModel }
    val config = model.config.collectAsState()
    val progress = model.progress.collectAsState()
    val blocks = model.blocks.collectAsState()

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(74, 117, 44)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DifficultyMenu(
                        config = config.value,
                        modifier = Modifier.weight(1f),
                        onGameConfigSelected = model::setConfig
                    )

                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = icFlag(),
                            contentDescription = null
                        )
                        val flagPlanted =
                            blocks.value.sumOf { it.count { minerBlock -> minerBlock is Block.Flag } }
                        val mineLeft = config.value.mine - flagPlanted
                        Text(
                            text = mineLeft.toString(),
                            fontWeight = FontWeight.W700,
                            color = Color.White
                        )

                        Spacer(Modifier.width(16.dp))

                        Image(
                            painter = icClock(),
                            contentDescription = null
                        )

                        var timeProgressInSeconds by remember { mutableStateOf(0L) }
                        LaunchedEffect(Unit) {
                            while (true) {
                                withFrameMillis {
                                    timeProgressInSeconds = when (val gameProgress = progress.value) {
                                        is Progress.Finish -> gameProgress.timeSpend.inWholeSeconds
                                        is Progress.OnGoing -> (System.currentTimeMillis() - gameProgress.startTimeInMillis).milliseconds.inWholeSeconds
                                        Progress.Pending -> 0
                                    }.coerceAtMost(999)
                                }
                            }
                        }

                        Text(
                            text = timeProgressInSeconds.toString().padStart(3, '0'),
                            fontWeight = FontWeight.W700,
                            color = Color.White
                        )
                    }

                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Image(
                            painter = icGithub(),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    onGitHubClick("https://github.com/nian430/compose-minesweeper")
                                }
                        )
                    }
                }
            }

            GameBoard(
                blocks = blocks.value,
                onBlockAction = model::interact
            )
        }


        when (val gameProgress = progress.value) {
            is Progress.Finish -> GameFinishAlert(gameProgress, model::restart)
            else -> {
                // do nothing
            }
        }
    }

}
