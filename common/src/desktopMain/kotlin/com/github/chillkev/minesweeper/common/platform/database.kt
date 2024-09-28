package com.github.chillkev.minesweeper.common.platform

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.github.chillkev.minesweeper.MinesweeperDatabase

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
