package com.pablichj.incubator.uistate3.demo.treebuilders

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import com.pablichj.incubator.uistate3.node.NavItem
import com.pablichj.incubator.uistate3.node.navbar.NavBarComponent
import com.pablichj.incubator.uistate3.node.setNavItems
import example.nodes.CustomTopBarComponent

object NavBarTreeBuilder {

    private lateinit var NavBarNode: NavBarComponent

    fun build(): NavBarComponent {

        if (NavBarTreeBuilder::NavBarNode.isInitialized) {
            return NavBarNode
        }

        val NavBarNode = NavBarComponent()

        val navbarNavItems = mutableListOf(
            NavItem(
                label = "Home",
                icon = Icons.Filled.Home,
                component = CustomTopBarComponent("Home", Icons.Filled.Home) {},

            ),
            NavItem(
                label = "Orders",
                icon = Icons.Filled.Settings,
                component = CustomTopBarComponent("Orders", Icons.Filled.Settings) {},

            ),
            NavItem(
                label = "Settings",
                icon = Icons.Filled.Add,
                component = CustomTopBarComponent("Settings", Icons.Filled.Add) {},

            )
        )

        return NavBarNode.also { it.setNavItems(navbarNavItems, 0) }
    }

}