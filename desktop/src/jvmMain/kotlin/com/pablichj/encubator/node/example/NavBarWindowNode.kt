package com.pablichj.encubator.node.example

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import com.pablichj.encubator.node.BackPressedCallback
import com.pablichj.encubator.node.JvmBackPressDispatcher
import com.pablichj.encubator.node.Node
import com.pablichj.encubator.node.NodeContext
import com.pablichj.encubator.node.example.statetrees.NavBarTreeBuilder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NavBarWindowNode(
    parentContext: NodeContext,
    val onCloseClick: () -> Unit
) : Node(parentContext), WindowNode {
    private val windowState = WindowState()

    private var NavBarNode: Node = NavBarTreeBuilder.build(
        JvmBackPressDispatcher(),
        backPressedCallback = object : BackPressedCallback() {
            override fun onBackPressed() {}
        }
    )

    @Composable
    override fun Content(modifier: Modifier) {
        Window(
            state = windowState,
            onCloseRequest = { onCloseClick() }
        ) {
            NavBarNode.Content(Modifier)
        }

        LaunchedEffect(windowState) {
            launch {
                snapshotFlow { windowState.isMinimized }
                    .onEach {
                        onWindowMinimized(NavBarNode, it)
                    }
                    .launchIn(this)
            }
        }
    }

    private fun onWindowMinimized(RootNode: Node, minimized: Boolean) {
        if (minimized) {
            RootNode.stop()
        } else {
            RootNode.start()
        }
    }

}