package com.github.nian430.minesweeper.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import com.github.nian430.minesweeper.common.MinesweeperView
import com.github.nian430.minesweeper.common.model.GameModel
import com.github.nian430.minesweeper.common.platform.DriverFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MinesweeperView(
                GameModel(sqlDriver = DriverFactory(LocalContext.current).createDriver())
            ) { repositoryUrl ->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(repositoryUrl)))
            }
        }
    }
}
