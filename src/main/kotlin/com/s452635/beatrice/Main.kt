package com.s452635.beatrice

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.s452635.beatrice.frame.AppContent

fun main()
{
    // TODO : init here

    singleWindowApplication (
        title = "Beatrice the Traffic Controller",
        resizable = false,
        state = WindowState (
            width = Dp.Unspecified,
            height = Dp.Unspecified
            ),
        content = { AppContent() }
        )
}



