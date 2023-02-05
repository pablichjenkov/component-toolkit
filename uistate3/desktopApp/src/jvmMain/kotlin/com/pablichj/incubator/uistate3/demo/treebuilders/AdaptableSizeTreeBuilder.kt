package com.pablichj.incubator.uistate3.demo.treebuilders

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import com.pablichj.incubator.uistate3.node.NavItem
import com.pablichj.incubator.uistate3.node.adaptable.AdaptableSizeComponent
import com.pablichj.incubator.uistate3.node.adaptable.IWindowSizeInfoProvider
import com.pablichj.incubator.uistate3.node.navbar.NavBarComponent
import com.pablichj.incubator.uistate3.node.navigation.SubPath
import com.pablichj.incubator.uistate3.node.setNavItems
import example.nodes.TopBarComponent

object AdaptableSizeTreeBuilder {

    private lateinit var AdaptableSizeNode: AdaptableSizeComponent
    private lateinit var subTreeNavItems: MutableList<NavItem>

    fun build(
        windowSizeInfoProvider: IWindowSizeInfoProvider
    ): AdaptableSizeComponent {

        if (AdaptableSizeTreeBuilder::AdaptableSizeNode.isInitialized) {
            return AdaptableSizeNode.apply {
                this.windowSizeInfoProvider = windowSizeInfoProvider
            }
        }

        return AdaptableSizeComponent(
            windowSizeInfoProvider
        ).also {
            it.subPath = SubPath("AdaptableWindow")
            AdaptableSizeNode = it
        }

    }

    fun getOrCreateDetachedNavItems(): MutableList<NavItem> {

        if (AdaptableSizeTreeBuilder::subTreeNavItems.isInitialized) {
            return subTreeNavItems
        }

        val NavBarNode = NavBarComponent()
            .apply { subPath = SubPath("Orders") }

        val navbarNavItems = mutableListOf(
            NavItem(
                label = "Current",
                icon = Icons.Filled.Home,
                component = TopBarComponent(
                    "Orders / Current", Icons.Filled.Home, {}
                ).apply { subPath = SubPath("Current") },
                selected = false
            ),
            NavItem(
                label = "Past",
                icon = Icons.Filled.Edit,
                component = TopBarComponent(
                    "Orders / Past", Icons.Filled.Edit, {}
                ).apply { subPath = SubPath("Past") },
                selected = false
            ),
            NavItem(
                label = "Claim",
                icon = Icons.Filled.Email,
                component = TopBarComponent("Orders / Claim", Icons.Filled.Email, {})
                    .apply { subPath = SubPath("Claim") },
                selected = false
            )
        )

        NavBarNode.setNavItems(navbarNavItems, 0)

        val SettingsNode =
            TopBarComponent("Settings", Icons.Filled.Email, {})
                .apply { subPath = SubPath("Settings") }

        val navItems = mutableListOf(
            NavItem(
                label = "Home",
                icon = Icons.Filled.Home,
                component = TopBarComponent(
                    "Home", Icons.Filled.Home, {}
                ).apply { subPath = SubPath("Home") },
                selected = false
            ),
            NavItem(
                label = "Orders",
                icon = Icons.Filled.Refresh,
                component = NavBarNode,
                selected = false
            ),
            NavItem(
                label = "Settings",
                icon = Icons.Filled.Email,
                component = SettingsNode,
                selected = false
            )
        )

        return navItems.also { subTreeNavItems = it }
    }

}