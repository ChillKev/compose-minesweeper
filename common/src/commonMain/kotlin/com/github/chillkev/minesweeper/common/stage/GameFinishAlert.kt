package com.github.chillkev.minesweeper.common.stage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.chillkev.minesweeper.common.generated.resources.Res
import com.github.chillkev.minesweeper.common.generated.resources.back_to_title
import com.github.chillkev.minesweeper.common.generated.resources.game_over
import com.github.chillkev.minesweeper.common.generated.resources.ic_clock
import com.github.chillkev.minesweeper.common.generated.resources.ic_refresh
import com.github.chillkev.minesweeper.common.generated.resources.ic_trophy
import com.github.chillkev.minesweeper.common.generated.resources.new_high_score
import com.github.chillkev.minesweeper.common.generated.resources.select_difficulty
import com.github.chillkev.minesweeper.common.generated.resources.stage_cleared
import com.github.chillkev.minesweeper.common.generated.resources.try_again
import com.github.chillkev.minesweeper.common.model.GameProgress
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun GameFinishAlert(
    progress: GameProgress.Finish,
    onResetGame: () -> Unit,
    onSelectDifficulty: () -> Unit,
    onBackToTitle: () -> Unit
) {
    val isWin = progress.isWin
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0, 0, 0, 192))
    ) {
        Column(
            modifier = Modifier
                .width(360.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(74, 192, 253))
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(if (isWin) Res.string.stage_cleared else Res.string.game_over),
                    fontSize = 32.sp,
                    color = Color.White
                )
                if (progress.isNewHighScore) {
                    Text(
                        text = stringResource(Res.string.new_high_score),
                        fontSize = 28.sp,
                        color = Color.Yellow
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier.size(68.dp),
                            painter = painterResource(Res.drawable.ic_clock),
                            contentDescription = null
                        )
                        Text(
                            text = if (isWin) {
                                val timeSpendInSeconds =
                                    progress.timeSpend.inWholeSeconds.coerceAtMost(999)
                                timeSpendInSeconds.toString().padStart(3, '0')
                            } else {
                                "-"
                            },
                            fontWeight = FontWeight.W700,
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier.size(68.dp),
                            painter = painterResource(Res.drawable.ic_trophy),
                            contentDescription = null
                        )
                        val highScoreInSeconds = progress.highScore.inWholeSeconds.coerceAtMost(999)
                        Text(
                            text = highScoreInSeconds.toString().padStart(3, '0'),
                            fontWeight = FontWeight.W700,
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onResetGame
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(Res.drawable.ic_refresh),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )
                    Text(
                        text = stringResource(Res.string.try_again),
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSelectDifficulty
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.select_difficulty),
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onBackToTitle
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.back_to_title),
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}
