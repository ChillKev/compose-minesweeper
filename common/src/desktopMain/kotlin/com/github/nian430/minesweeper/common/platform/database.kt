package com.github.nian430.minesweeper.common.platform

import com.github.nian430.minesweeper.MinesweeperDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

actual class DriverFactory(private val name: String) {
    actual fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(
            if (name.isNotEmpty()) {
                "jdbc:sqlite:$name.db"
            } else {
                JdbcSqliteDriver.IN_MEMORY
            }
        ).apply {
            MinesweeperDatabase.Schema.create(this)
        }
    }
}
