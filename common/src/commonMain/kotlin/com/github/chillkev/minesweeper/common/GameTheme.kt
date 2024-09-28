package com.github.chillkev.minesweeper.common

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.github.chillkev.minesweeper.common.generated.resources.Res
import com.github.chillkev.minesweeper.common.generated.resources.call_of_ops_duty_ii
import org.jetbrains.compose.resources.Font

@Composable
internal fun GameTheme(
    content: @Composable () -> Unit
) {
    val typography = MaterialTheme.typography
    val fontFamily = FontFamily(Font(Res.font.call_of_ops_duty_ii))
    val newTypography = typography.copy(
        h1 = typography.h1.copy(fontFamily = fontFamily),
        h2 = typography.h2.copy(fontFamily = fontFamily),
        h3 = typography.h3.copy(fontFamily = fontFamily),
        h4 = typography.h4.copy(fontFamily = fontFamily),
        h5 = typography.h5.copy(fontFamily = fontFamily),
        h6 = typography.h6.copy(fontFamily = fontFamily),
        subtitle1 = typography.subtitle1.copy(fontFamily = fontFamily),
        subtitle2 = typography.subtitle2.copy(fontFamily = fontFamily),
        body1 = typography.body1.copy(fontFamily = fontFamily),
        body2 = typography.body2.copy(fontFamily = fontFamily),
        button = typography.button.copy(fontFamily = fontFamily, fontSize = 20.sp),
        caption = typography.caption.copy(fontFamily = fontFamily),
        overline = typography.overline.copy(fontFamily = fontFamily),
    )
    val colors = MaterialTheme.colors
    val newColors = colors.copy(
        primary = Color(132, 184, 49),
        primaryVariant = Color(53, 111, 7),
        secondary = Color(101, 49, 184),
        secondaryVariant = Color(55, 21, 155),
        background = Color(49, 184, 101, 128),
        surface = Color(49, 184, 101, 128),
    )
    MaterialTheme(
        colors = newColors,
        typography = newTypography,
        content = content
    )
}
