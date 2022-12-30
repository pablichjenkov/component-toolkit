package com.pablichj.incubator.uistate3

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import com.pablichj.incubator.uistate3.ComposeApp
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        Window("UiState3") {
            Column(modifier = Modifier.fillMaxSize()) {
                ComposeApp()
            }
        }
    }
}

