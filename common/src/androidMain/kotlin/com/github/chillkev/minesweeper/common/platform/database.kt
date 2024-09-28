package com.github.chillkev.minesweeper.common.platform

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.github.chillkev.minesweeper.MinesweeperDatabase

actual class DriverFactory(context: Context) {

    private val context = context.applicationContext

    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(MinesweeperDatabase.Schema, context, "minesweeper.db")
    }

}
