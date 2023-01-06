package com.pablichj.incubator.uistate3.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.pablichj.incubator.uistate3.example.treebuilders.NavBarStateTreeHolder
import com.pablichj.incubator.uistate3.node.*

class NavBarActivity : ComponentActivity() {

    private val activityStateHolder by viewModels<NavBarStateTreeHolder>()
    private lateinit var StateTree: Node

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // It creates a state tree where the root node is a NavBar node
        StateTree = activityStateHolder.getOrCreate().apply {
            context.rootNodeBackPressedDelegate = ForwardBackPressCallback { finish() }
        }

        setContent {
            MaterialTheme {
                CompositionLocalProvider(
                    LocalBackPressedDispatcher provides AndroidBackPressDispatcher(
                        this@NavBarActivity
                    ),
                ) {
                    StateTree.Content(Modifier)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        StateTree.start()
    }

    override fun onStop() {
        super.onStop()
        StateTree.stop()
    }

}