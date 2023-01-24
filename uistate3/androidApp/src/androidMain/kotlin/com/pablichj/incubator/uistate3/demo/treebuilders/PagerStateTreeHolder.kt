package com.pablichj.incubator.uistate3.demo.treebuilders

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.lifecycle.ViewModel
import com.pablichj.incubator.uistate3.node.*
import com.pablichj.incubator.uistate3.node.navbar.NavBarNode
import example.nodes.TopBarNode

class PagerStateTreeHolder : ViewModel() {

    private lateinit var PagerNode: PagerNode

    fun getOrCreate(): Node {

        if (this@PagerStateTreeHolder::PagerNode.isInitialized) {
            return PagerNode
        }

        PagerNode = PagerNode()

        val NavBarNode1 = NavBarNode()
        val NavBarNode2 = NavBarNode()

        val navbarNavItems1 = mutableListOf(
            NodeItem(
                label = "Current",
                icon = Icons.Filled.Home,
                node = TopBarNode("Orders/ Current") {},
                selected = false
            ),
            NodeItem(
                label = "Past",
                icon = Icons.Filled.Edit,
                node = TopBarNode("Orders / Past") {},
                selected = false
            ),
            NodeItem(
                label = "Claim",
                icon = Icons.Filled.Email,
                node = TopBarNode("Orders / Claim") {},
                selected = false
            )
        )

        val navbarNavItems2 = mutableListOf(
            NodeItem(
                label = "Account",
                icon = Icons.Filled.Home,
                node = TopBarNode("Settings / Account") {},
                selected = false
            ),
            NodeItem(
                label = "Profile",
                icon = Icons.Filled.Edit,
                node = TopBarNode("Settings / Profile") {},
                selected = false
            ),
            NodeItem(
                label = "About Us",
                icon = Icons.Filled.Email,
                node = TopBarNode("Settings / About Us") {},
                selected = false
            )
        )

        val pagerNavItems = mutableListOf(
            NodeItem(
                label = "Home",
                icon = Icons.Filled.Home,
                node = TopBarNode("Home") {},
                selected = false
            ),
            NodeItem(
                label = "Orders",
                icon = Icons.Filled.Edit,
                node = NavBarNode1.also { it.setItems(navbarNavItems1, 0) },
                selected = false
            ),
            NodeItem(
                label = "Settings",
                icon = Icons.Filled.Email,
                node = NavBarNode2.also { it.setItems(navbarNavItems2, 0) },
                selected = false
            )
        )

        return PagerNode.also { it.setItems(pagerNavItems, 0) }
    }

}