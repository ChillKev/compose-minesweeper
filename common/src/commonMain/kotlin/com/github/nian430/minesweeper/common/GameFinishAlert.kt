package com.github.nian430.minesweeper.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.github.nian430.minesweeper.common.model.Progress
import com.github.nian430.minesweeper.common.platform.icClock
import com.github.nian430.minesweeper.common.platform.icRefresh
import com.github.nian430.minesweeper.common.platform.icTrophy

@Composable
fun GameFinishAlert(
    progress: Progress.Finish,
    onResetGame: () -> Unit
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
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(74, 192, 253))
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.size(68.dp),
                        painter = icClock(),
                        contentDescription = null
                    )
                    Text(
                        text = if (isWin) {
                            val timeSpendInSeconds = progress.timeSpend.inWholeSeconds.coerceAtMost(999)
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
                        painter = icTrophy(),
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
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(74, 117, 44))
                    .clickable(onClick = onResetGame)
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = icRefresh(),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Spacer(
                    modifier = Modifier.width(8.dp)
                )
                Text(
                    text = "Try Again",
                    color = Color.White
                )
            }
        }
    }
}
