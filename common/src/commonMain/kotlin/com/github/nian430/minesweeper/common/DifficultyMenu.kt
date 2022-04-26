package com.github.nian430.minesweeper.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.nian430.minesweeper.common.model.Config
import com.github.nian430.minesweeper.common.platform.*

@Composable
internal fun DifficultyMenu(
    config: Config,
    onGameConfigSelected: (Config) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxHeight()
    ) {
        OutlinedButton(
            onClick = {
                expanded = true
            }
        ) {
            Text(text = config.name)
            Image(
                painter = icTriangleDown(),
                contentDescription = null
            )
        }

        val items = Difficulty.values()
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onGameConfigSelected(item)
                        expanded = false
                    }
                ) {
                    val sizeModifier = Modifier.width(24.dp)

                    if (item == config) {
                        Image(
                            modifier = sizeModifier
                                .padding(4.dp),
                            painter = icCheck(),
                            contentDescription = null
                        )
                    } else {
                        Spacer(modifier = sizeModifier)
                    }

                    Text(text = item.name)
                }
            }
        }
    }
}
