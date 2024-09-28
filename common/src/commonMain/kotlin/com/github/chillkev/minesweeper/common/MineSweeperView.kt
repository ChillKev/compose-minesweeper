package com.github.chillkev.minesweeper.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import com.github.chillkev.minesweeper.common.generated.resources.Res
import com.github.chillkev.minesweeper.common.generated.resources.board_background
import com.github.chillkev.minesweeper.common.model.GameModel
import com.github.chillkev.minesweeper.common.model.GameProgress
import com.github.chillkev.minesweeper.common.stage.DifficultySelection
import com.github.chillkev.minesweeper.common.stage.Game
import com.github.chillkev.minesweeper.common.stage.GameFinishAlert
import com.github.chillkev.minesweeper.common.stage.Stage
import com.github.chillkev.minesweeper.common.stage.TitleScreen
import org.jetbrains.compose.resources.painterResource

@Composable
fun MinesweeperView(
    gameModel: GameModel,
    onUrlClick: (url: String) -> Unit
) {
    val model = remember { gameModel }
    val progress = model.progress.collectAsState()
    val stage = model.stage.collectAsState()

    GameTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            BackgroundImage(modifier = Modifier.fillMaxSize())

            AnimatedContent(
                targetState = stage.value,
                modifier = Modifier.fillMaxSize()
            ) { stage ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when (stage) {
                        Stage.TitleScreen -> {
                            TitleScreen(gameModel = model, onUrlClick = onUrlClick)
                        }

                        Stage.DifficultySelection -> {
                            DifficultySelection(gameModel = model)
                        }

                        Stage.Game -> {
                            Game(
                                gameModel = gameModel,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            when (val gameProgress = progress.value) {
                is GameProgress.Finish -> {
                    GameFinishAlert(
                        gameProgress,
                        onResetGame = {
                            model.restart()
                        },
                        onSelectDifficulty = {
                            model.restart()
                            model.setStage(Stage.DifficultySelection)
                        },
                        onBackToTitle = {
                            model.restart()
                            model.setStage(Stage.TitleScreen)
                        }
                    )
                }

                else -> {
                    // do nothing
                }
            }
        }
    }
}

@Composable
private fun BackgroundImage(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(Res.drawable.board_background),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        colorFilter = ColorFilter.tint(Color.Black.copy(alpha = .5f), BlendMode.Darken)
    )
}
