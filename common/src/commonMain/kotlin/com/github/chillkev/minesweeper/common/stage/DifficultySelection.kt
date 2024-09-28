package com.github.chillkev.minesweeper.common.stage

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.chillkev.minesweeper.common.generated.resources.Res
import com.github.chillkev.minesweeper.common.generated.resources.back
import com.github.chillkev.minesweeper.common.model.GameModel
import com.github.chillkev.minesweeper.common.platform.Difficulty
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DifficultySelection(
    gameModel: GameModel,
    modifier: Modifier = Modifier
) {
    SelectionMenu(
        modifier = modifier
    ) {
        Difficulty.entries.forEach { difficulty ->
            Button(
                onClick = {
                    gameModel.setConfig(difficulty)
                    gameModel.setStage(Stage.Game)
                }
            ) {
                Text(difficulty.name)
            }
        }
        Button(
            onClick = {
                gameModel.setStage(Stage.TitleScreen)
            }
        ) {
            Text(stringResource(Res.string.back))
        }
    }
}
