package com.pablichj.incubator.uistate3.node.adaptable

import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState

class JvmWindowSizeInfoProvider(
    val windowState:  WindowState,
) : IWindowSizeInfoProvider() {

    @Composable
    override fun windowSizeInfo(): State<WindowSizeInfo> {
        return remember {
            derivedStateOf {
                when(windowState.size.width) {
                    in (0.dp..600.dp)  -> WindowSizeInfo.Compact
                    in (600.dp..840.dp) -> WindowSizeInfo.Medium
                    else -> WindowSizeInfo.Expanded
                }
            }
        }
    }

}