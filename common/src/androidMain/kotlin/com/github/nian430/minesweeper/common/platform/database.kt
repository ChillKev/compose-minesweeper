package com.github.nian430.minesweeper.common.platform

import android.content.Context
import com.github.nian430.minesweeper.MinesweeperDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DriverFactory(context: Context) {

    private val context = context.applicationContext

    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(MinesweeperDatabase.Schema, context, "minesweeper.db")
    }

}
