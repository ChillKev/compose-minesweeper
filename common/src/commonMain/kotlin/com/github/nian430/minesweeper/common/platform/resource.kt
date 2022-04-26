package com.github.nian430.minesweeper.common.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
internal expect fun icFlag(): Painter

@Composable
internal expect fun icExplosion(): Painter

@Composable
internal expect fun icGithub(): Painter

@Composable
internal expect fun icClock(): Painter

@Composable
internal expect fun icTrophy(): Painter

@Composable
internal expect fun icCheck(): Painter

@Composable
internal expect fun icTriangleDown(): Painter

@Composable
internal expect fun icRefresh(): Painter

@Composable
internal expect fun boardBackground(): Painter
