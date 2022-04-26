package com.github.nian430.minesweeper.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.github.nian430.minesweeper.common.model.GameModel
import com.github.nian430.minesweeper.common.platform.DriverFactory
import java.awt.Desktop
import java.net.URI

@Preview
@Composable
private fun Preview() {
    MinesweeperView(
        GameModel(sqlDriver = DriverFactory("minesweeper").createDriver())
    ) { repositoryUrl ->
        Desktop.getDesktop().browse(URI(repositoryUrl))
    }
}
