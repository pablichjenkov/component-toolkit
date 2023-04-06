package com.pablichj.incubator.uistate3.node.panel

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pablichj.incubator.uistate3.node.NavItemDeco

@Composable
fun NavigationPanel(
    modifier: Modifier = Modifier,
    panelState: IPanelState,
    Content: @Composable () -> Unit
) {
    val navItems by panelState.navItemsFlow.collectAsState(emptyList())

    Row(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.width(280.dp)
        ) {
            PanelHeader(panelHeaderState = panelState.panelHeaderState)
            PanelContentList(
                navItems = navItems,
                onNavItemClick = { navItem -> panelState.navItemClick(navItem) }
            )
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Content()
        }
    }

}

@Composable
private fun PanelHeader(
    modifier: Modifier = Modifier,
    panelHeaderState: PanelHeaderState
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color.LightGray)
            .padding(all = 16.dp),
    ) {
        Column(modifier = modifier) {
            Text(
                text = panelHeaderState.title,
                fontSize = panelHeaderState.style.titleTextSize
            )
            Text(
                text = panelHeaderState.description,
                fontSize = panelHeaderState.style.descriptionTextSize
            )
        }
    }
}

@Composable
private fun PanelContentList(
    modifier: Modifier = Modifier,
    navItems: List<NavItemDeco>,
    onNavItemClick: (NavItemDeco) -> Unit
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (navItem in navItems) {
            PanelDrawerItem(
                label = { Text(navItem.label) },
                icon = { Icon(navItem.icon, null) },
                selected = navItem.selected,
                onClick = { onNavItemClick(navItem) }
            )
        }
    }
}

@Composable
private fun PanelDrawerItem(
    label: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit
) {
    val modifier = if (selected) {
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(width = 1.dp, color = Color.Black)
            .background(Color.LightGray)
            .padding(8.dp)
            .clickable {
                onClick()
            }
    } else {
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(8.dp)
            .clickable {
                onClick()
            }
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        icon()
        Spacer(Modifier.width(8.dp))
        label()
    }
}
