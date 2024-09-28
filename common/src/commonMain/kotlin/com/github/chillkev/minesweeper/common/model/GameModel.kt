package com.github.chillkev.minesweeper.common.model

import app.cash.sqldelight.db.SqlDriver
import com.github.chillkev.minesweeper.common.db.DatabaseHelper
import com.github.chillkev.minesweeper.common.platform.Difficulty
import com.github.chillkev.minesweeper.common.stage.Stage
import com.github.chillkev.minesweeper.data.HighScore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class GameModel(
    initialConfig: Config = Difficulty.entries.first(),
    sqlDriver: SqlDriver
) {

    private val _config = MutableStateFlow(initialConfig)
    val config get() = _config.asStateFlow()

    private val _progress = MutableStateFlow<GameProgress>(GameProgress.Pending)
    val progress get() = _progress.asStateFlow()

    private val _blocks = MutableStateFlow(emptyList<List<Block>>())
    val blocks get() = _blocks.asStateFlow()

    private val _stage = MutableStateFlow(Stage.TitleScreen)
    val stage get() = _stage.asStateFlow()

    private var contents = emptyList<List<Content>>()

    private val databaseHelper = DatabaseHelper(sqlDriver)
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val mutex = Mutex()

    init {
        setConfig(initialConfig)
    }

    fun setStage(stage: Stage) {
        queue {
            _stage.value = stage
        }
    }

    fun setConfig(config: Config) {
        queue {
            _config.value = config
            initGame()
        }
    }

    fun restart() {
        queue {
            initGame()
        }
    }

    fun interact(row: Int, column: Int, action: BlockAction) {
        queue {
            if (_progress.value is GameProgress.Finish) {
                return@queue
            }

            val isFirstDig = contents.isEmpty() && action === BlockAction.Dig
            if (isFirstDig) {
                contents = generateBlocks(row, column, _config.value)
                _progress.value = GameProgress.OnGoing(System.currentTimeMillis())
            }

            when (action) {
                BlockAction.Flag -> {
                    flag(row, column)
                }

                BlockAction.Dig -> {
                    dig(row, column, false)
                    updateProgress()
                }

                BlockAction.DigAround -> {
                    if (canDigAround(row, column)) {
                        digAround(row, column, false)
                        updateProgress()
                    }
                }
            }
        }
    }

    private fun canDigAround(row: Int, column: Int): Boolean {
        val content = contents[row][column]
        return if (content is Content.Indicator) {
            val blocks = _blocks.value

            var flagNear = 0
            for (i in (row - 1)..(row + 1)) {
                for (j in (column - 1)..(column + 1)) {
                    val minerBlock = blocks.getOrNull(i)?.getOrNull(j)
                    if (minerBlock is Block.Flag) {
                        flagNear++
                    }
                }
            }

            flagNear == content.value
        } else {
            false
        }
    }

    private suspend fun updateProgress() {
        val currentBlocks = _blocks.value
        var isLose = false
        var blockUnRevealCount = 0

        run gameOverBreak@{
            currentBlocks.forEach { rows ->
                rows.forEach { minerBlock ->
                    when (minerBlock) {
                        Block.Hidden -> blockUnRevealCount++
                        Block.Flag -> blockUnRevealCount++
                        is Block.Reveal -> {
                            if (minerBlock.content is Content.Mine) {
                                isLose = true
                                return@gameOverBreak
                            }
                        }
                    }
                }
            }
        }

        val currentConfig = _config.value
        val isWin = !isLose && blockUnRevealCount == currentConfig.mine
        val isComplete = isWin || isLose

        if (isComplete) {
            _progress.update { progress ->
                when (progress) {
                    is GameProgress.OnGoing -> {
                        val startTimeInMillis = progress.startTimeInMillis
                        val endTimeInMillis = System.currentTimeMillis()
                        val timeSpend = (endTimeInMillis - startTimeInMillis).milliseconds
                        var isNewHighScore = false
                        if (isWin) {
                            val previousHighScore = databaseHelper.getHighScore(
                                rowCount = currentConfig.rows,
                                columnCount = currentConfig.columns,
                                mineCount = currentConfig.mine
                            )

                            val previousTimeSpend =
                                previousHighScore?.timeInMillis?.milliseconds ?: Duration.INFINITE
                            if (timeSpend < previousTimeSpend) {
                                isNewHighScore = true
                                databaseHelper.setHighScore(
                                    HighScore(
                                        rowCount = currentConfig.rows,
                                        columnCount = currentConfig.columns,
                                        mineCount = currentConfig.mine,
                                        timeInMillis = timeSpend.inWholeMilliseconds
                                    )
                                )
                            }
                        }
                        val highScoreTimeSpend = databaseHelper.getHighScore(
                            rowCount = currentConfig.rows,
                            columnCount = currentConfig.columns,
                            mineCount = currentConfig.mine
                        )?.timeInMillis?.milliseconds
                        GameProgress.Finish(
                            isWin,
                            timeSpend,
                            highScoreTimeSpend ?: Duration.ZERO,
                            isNewHighScore
                        )
                    }

                    else -> progress
                }
            }
        }
    }

    private fun dig(targetRow: Int, targetColumn: Int, revealFlag: Boolean) {
        val targetBlock = _blocks.value.getOrNull(targetRow)?.getOrNull(targetColumn)
        if (targetBlock == null || targetBlock is Block.Reveal) {
            return
        }

        val realBlock = contents[targetRow][targetColumn]
        _blocks.update { blocks ->
            blocks.mapIndexed { row, rows ->
                rows.mapIndexed { column, minerBlock ->
                    if (row == targetRow && column == targetColumn) {
                        when (minerBlock) {
                            Block.Flag -> {
                                if (revealFlag) {
                                    Block.Reveal(realBlock)
                                } else {
                                    Block.Flag
                                }
                            }

                            Block.Hidden -> Block.Reveal(realBlock)
                            is Block.Reveal -> minerBlock
                        }
                    } else {
                        minerBlock
                    }
                }
            }
        }

        if (realBlock is Content.Empty) {
            // reveal near blocks
            digAround(targetRow, targetColumn, true)
        }
    }

    private fun digAround(targetRow: Int, targetColumn: Int, revealFlag: Boolean) {
        for (i in (targetRow - 1)..(targetRow + 1)) {
            for (j in (targetColumn - 1)..(targetColumn + 1)) {
                dig(i, j, revealFlag)
            }
        }
    }

    private fun flag(targetRow: Int, targetColumn: Int) {
        _blocks.update { blocks ->
            blocks.mapIndexed { row, rows ->
                rows.mapIndexed { column, minerBlock ->
                    if (row == targetRow && column == targetColumn) {
                        when (minerBlock) {
                            Block.Hidden -> Block.Flag
                            Block.Flag -> Block.Hidden
                            is Block.Reveal -> minerBlock
                        }
                    } else {
                        minerBlock
                    }
                }
            }
        }
    }

    private fun generateBlocks(
        firstRevealRow: Int,
        firstRevealColumn: Int,
        config: Config
    ): List<List<Content>> {
        val totalBlockCount = config.rows * config.columns
        // the blocks near first reveal should be empty
        val emptyBlockCount = let {
            val rowEdgeCount = when {
                config.rows == 1 -> 2
                firstRevealRow == 0 || firstRevealRow == config.rows - 1 -> 1
                else -> 0
            }

            val columnEdgeCount = when {
                config.columns == 1 -> 2
                firstRevealColumn == 0 || firstRevealColumn == config.columns - 1 -> 1
                else -> 0
            }

            // empty block count = 9 - 3x - 3y + xy
            9 - 3 * rowEdgeCount - 3 * columnEdgeCount + rowEdgeCount * columnEdgeCount
        }
        val generateBlockCount = totalBlockCount - emptyBlockCount

        val contentLists = (0 until generateBlockCount).map { index ->
            if (index < config.mine) {
                Content.Mine
            } else {
                Content.Empty
            }
        }
            .shuffled()

        val firstRevealRowRange = (firstRevealRow - 1)..(firstRevealRow + 1)
        val firstRevealColumnRange = (firstRevealColumn - 1)..(firstRevealColumn + 1)
        val isNearFistRevealBlock = { row: Int, column: Int ->
            val isNearFirstRevealRow = row in firstRevealRowRange
            val isNearFirstRevealColumn = column in firstRevealColumnRange
            isNearFirstRevealRow && isNearFirstRevealColumn
        }

        var nextIndex = 0
        val tempContents = (0 until config.rows).map { row ->
            (0 until config.columns).map { column ->
                if (isNearFistRevealBlock(row, column)) {
                    Content.Empty
                } else {
                    contentLists[nextIndex++]
                }
            }
                .toMutableList()
        }

        // calculate Indicator
        tempContents.forEachIndexed { row, rows ->
            rows.forEachIndexed { column, content ->
                if (content is Content.Mine) {
                    for (i in (row - 1)..(row + 1)) {
                        for (j in (column - 1)..(column + 1)) {
                            when (val nearBlock = tempContents.getOrNull(i)?.getOrNull(j)) {
                                Content.Empty -> Content.Indicator.fromValue(1)
                                is Content.Indicator -> Content.Indicator.fromValue(nearBlock.value + 1)
                                else -> null
                            }?.let { indicator ->
                                tempContents[i][j] = indicator
                            }
                        }
                    }
                }
            }
        }

        return tempContents.map { it.toList() }
    }

    private fun initGame() {
        val currentConfig = _config.value
        _progress.value = GameProgress.Pending
        _blocks.value = buildList {
            repeat(currentConfig.rows) {
                add(
                    buildList {
                        repeat(currentConfig.columns) {
                            add(Block.Hidden)
                        }
                    }
                )
            }
        }
        contents = emptyList()
    }

    private fun queue(block: suspend () -> Unit) {
        coroutineScope.launch {
            mutex.withLock {
                block()
            }
        }
    }

}
