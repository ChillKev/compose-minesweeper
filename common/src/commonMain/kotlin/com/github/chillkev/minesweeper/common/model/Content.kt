package com.github.chillkev.minesweeper.common.model

sealed class Content {
    data object Empty : Content()
    data object Mine : Content()

    sealed class Indicator(val value: Int) : Content() {
        companion object {
            fun fromValue(value: Int): Indicator {
                return when (value) {
                    1 -> One
                    2 -> Two
                    3 -> Three
                    4 -> Four
                    5 -> Five
                    6 -> Six
                    7 -> Seven
                    8 -> Eight
                    else -> error("no Indicator with value: $value found")
                }
            }
        }

        data object One : Indicator(1)
        data object Two : Indicator(2)
        data object Three : Indicator(3)
        data object Four : Indicator(4)
        data object Five : Indicator(5)
        data object Six : Indicator(6)
        data object Seven : Indicator(7)
        data object Eight : Indicator(8)
    }

}
