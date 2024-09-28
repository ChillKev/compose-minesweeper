package com.github.chillkev.minesweeper.common.stage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.chillkev.minesweeper.common.generated.resources.Res
import com.github.chillkev.minesweeper.common.generated.resources.credits
import com.github.chillkev.minesweeper.common.generated.resources.exit
import com.github.chillkev.minesweeper.common.generated.resources.github
import com.github.chillkev.minesweeper.common.generated.resources.start_game
import com.github.chillkev.minesweeper.common.generated.resources.title
import com.github.chillkev.minesweeper.common.model.GameModel
import org.jetbrains.compose.resources.stringResource
import kotlin.system.exitProcess

@Composable
internal fun TitleScreen(
    gameModel: GameModel,
    onUrlClick: (url: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.title),
            color = Color.White,
            fontSize = 56.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        SelectionMenu {
            Button(
                onClick = { gameModel.setStage(Stage.DifficultySelection) }
            ) {
                Text(stringResource(Res.string.start_game))
            }
            Button(
                onClick = { onUrlClick("https://github.com/ChillKev/compose-minesweeper/blob/main/CREDITS.md") }
            ) {
                Text(stringResource(Res.string.credits))
            }
            Button(
                onClick = { onUrlClick("https://github.com/ChillKev/compose-minesweeper") }
            ) {
                Text(stringResource(Res.string.github))
            }
            Button(
                onClick = { exitProcess(0) }
            ) {
                Text(stringResource(Res.string.exit))
            }
        }
    }
}
