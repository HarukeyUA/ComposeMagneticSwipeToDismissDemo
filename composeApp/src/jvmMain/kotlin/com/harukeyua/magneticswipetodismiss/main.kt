package com.harukeyua.magneticswipetodismiss

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Wade Distortion Demo", alwaysOnTop = true) {
        App()
    }
}
