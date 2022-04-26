package com.github.nian430.minesweeper.common.model

sealed class Content {
    object Empty : Content()
    object Mine : Content()

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
        object One : Indicator(1)
        object Two : Indicator(2)
        object Three : Indicator(3)
        object Four : Indicator(4)
        object Five : Indicator(5)
        object Six : Indicator(6)
        object Seven : Indicator(7)
        object Eight : Indicator(8)
    }

}
