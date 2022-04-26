package com.github.nian430.minesweeper.common.db

import com.github.nian430.minesweeper.MinesweeperDatabase
import com.github.nian430.minesweeper.data.HighScore
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class DatabaseHelper(
    sqlDriver: SqlDriver,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    private val database = MinesweeperDatabase(sqlDriver)

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