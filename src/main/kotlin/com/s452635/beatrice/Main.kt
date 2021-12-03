package com.s452635.beatrice

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.s452635.beatrice.frame.AppContent

fun main()
{
    val graphicController = GraphicController()
    application {
        Window (
            onCloseRequest = {
                exitApplication()
                graphicController.isRunning.value = false
                },
            title = "Beatrice the Traffic Controller",
            resizable = false,
            state = WindowState (
                width = Dp.Unspecified,
                height = Dp.Unspecified
                ),
            content = { AppContent( graphicController ) }
        )
        graphicController.simulation.start()
    }
}



