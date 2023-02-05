package com.pablichj.incubator.uistate3.demo

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import com.pablichj.incubator.uistate3.AndroidComponentRender
import com.pablichj.incubator.uistate3.node.Component
import com.pablichj.incubator.uistate3.node.NavItem
import com.pablichj.incubator.uistate3.node.PagerComponent
import com.pablichj.incubator.uistate3.node.drawer.DrawerComponent
import com.pablichj.incubator.uistate3.node.navbar.NavBarComponent
import com.pablichj.incubator.uistate3.node.setItems
import example.nodes.AppCoordinatorComponent
import example.nodes.TopBarComponent

class HandleConfigChangesActivity : ComponentActivity() {

    private lateinit var StateTree: Component

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StateTree = AppCoordinatorComponent().also {
            //it.context.rootNodeBackPressedDelegate = ForwardBackPressCallback { finish() }
            it.homeComponent = buildHomeNode(it)
        }

        setContent {
            AndroidComponentRender(
                rootComponent = StateTree,
                onBackPressEvent = { finish() }
            )
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildHomeNode(parentComponent: Component): Component {

        val DrawerNode = DrawerComponent()

        val TopBarNode = TopBarComponent(
            "Home",
            Icons.Filled.Home
        ) {}
        val NavBarNode = NavBarComponent()
        val PagerNode = PagerComponent()

        val navbarNavItems = mutableListOf(
            NavItem(
                label = "Current",
                icon = Icons.Filled.Home,
                component = TopBarComponent("Orders / Current", Icons.Filled.Home) {},
                selected = false
            ),
            NavItem(
                label = "Past",
                icon = Icons.Filled.AccountCircle,
                component = TopBarComponent("Orders / Past", Icons.Filled.AccountCircle) {},
                selected = false
            ),
            NavItem(
                label = "Claim",
                icon = Icons.Filled.Email,
                component = TopBarComponent("Orders / Claim", Icons.Filled.Email) {},
                selected = false
            )
        )

        val pagerNavItems = mutableListOf(
            NavItem(
                label = "Account",
                icon = Icons.Filled.Home,
                component = TopBarComponent("Settings / Account", Icons.Filled.Home) {},
                selected = false
            ),
            NavItem(
                label = "Profile",
                icon = Icons.Filled.Edit,
                component = TopBarComponent("Settings / Profile", Icons.Filled.Edit) {},
                selected = false
            ),
            NavItem(
                label = "About Us",
                icon = Icons.Filled.Email,
                component = TopBarComponent("Settings / About Us", Icons.Filled.Email) {},
                selected = false
            )
        )

        val drawerNavItems = mutableListOf(
            NavItem(
                label = "Home",
                icon = Icons.Filled.Home,
                component = TopBarNode,
                selected = false
            ),
            NavItem(
                label = "Orders",
                icon = Icons.Filled.Edit,
                component = NavBarNode.also { it.setItems(navbarNavItems, 0) },
                selected = false
            ),
            NavItem(
                label = "Settings",
                icon = Icons.Filled.Email,
                component = PagerNode.also { it.setItems(pagerNavItems, 0) },
                selected = false
            )
        )

        return DrawerNode.also {
            it.attachToParent(parentComponent)
            it.setItems(drawerNavItems, 0)
        }
    }

}