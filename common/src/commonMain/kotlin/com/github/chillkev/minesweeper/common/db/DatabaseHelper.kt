package com.github.chillkev.minesweeper.common.db

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.github.chillkev.minesweeper.MinesweeperDatabase
import com.github.chillkev.minesweeper.data.HighScore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class DatabaseHelper(
    sqlDriver: SqlDriver,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    private val database = MinesweeperDatabase(
        sqlDriver,
        HighScore.Adapter(IntColumnAdapter, IntColumnAdapter, IntColumnAdapter)
    )

    suspend fun getHighScore(
        rowCount: Int,
        columnCount: Int,
        mineCount: Int
    ) = withContext(dispatcher) {
        database.highScoreQueries
            .get(rowCount, columnCount, mineCount)
            .executeAsOneOrNull()
    }

    suspend fun setHighScore(highScore: HighScore) = withContext(dispatcher) {
        database.highScoreQueries.upsert(highScore)
    }

}