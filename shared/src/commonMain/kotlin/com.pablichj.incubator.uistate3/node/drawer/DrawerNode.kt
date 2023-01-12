package com.pablichj.incubator.uistate3.node.drawer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DrawerValue
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.pablichj.incubator.uistate3.node.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DrawerNode(
    // TODO: Ask for the Header Info to render the Drawer header
) : BackStackNode<Node>(), ContainerNode, INavigationProvider {

    private val nodeCoroutineScope = CoroutineScope(Dispatchers.Main)// TODO: Use DispatchersBin
    private var activeNodeState: MutableState<Node?> = mutableStateOf(null)
    private var startingIndex = 0
    private var selectedIndex = 0
    private var navItems: MutableList<NodeItem> = mutableListOf()
    private var childNodes: MutableList<Node> = mutableListOf()
    private val navDrawerState = NavigationDrawerState(emptyList())

    init {
        nodeCoroutineScope.launch {
            navDrawerState.navItemClickFlow.collect { navItemClick ->
                pushNode(navItemClick.node)
            }
        }

        context.setNavigationProvider(this)
    }

    override fun start() {
        super.start()
        val childNodesCopy = childNodes
        if (activeNodeState.value == null && childNodesCopy.isNotEmpty()) {
            pushNode(childNodesCopy[startingIndex])
        } else {
            activeNodeState.value?.start()
        }
    }

    override fun stop() {
        super.stop()
        activeNodeState.value?.stop()
    }

    // region BackStackNode override

    override fun onStackPush(oldTop: Node?, newTop: Node) {
        println("DrawerNode::onStackPush(), oldTop: ${oldTop?.let { it::class.simpleName }}," +
                " newTop: ${newTop::class.simpleName}")

        activeNodeState.value = newTop
        newTop.start()
        oldTop?.stop()

        updateSelectedNavItem(newTop)
    }

    override fun onStackPop(oldTop: Node, newTop: Node?) {
        println("DrawerNode::onStackPop(), oldTop: ${oldTop::class.simpleName}," +
                " newTop: ${newTop?.let { it::class.simpleName }}")

        activeNodeState.value = newTop
        newTop?.start()
        oldTop.stop()

        newTop?.let { updateSelectedNavItem(it) }
    }

    private fun updateSelectedNavItem(newTop: Node) {
        getNavItemFromNode(newTop)?.let {
            println("DrawerNode::updateSelectedNavItem(), selectedIndex = $it")
            navDrawerState.selectNavItem(it)
            selectedIndex = childNodes.indexOf(newTop)
        }
    }

    private fun getNavItemFromNode(node: Node): NodeItem? {
        return navDrawerState.navItems.firstOrNull { it.node == node }
    }

    // endregion

    // region: INavigationProvider

    override fun open() {
        println("DrawerNode::open")
        navDrawerState.setDrawerState(DrawerValue.Open)
    }

    override fun close() {
        println("DrawerNode::close")
        navDrawerState.setDrawerState(DrawerValue.Closed)
    }

    // endregion

    // region: NavigatorNode TODO: This code is repeated pretty much in all NavigatorNode classes
    // Take it to a common place

    override fun getNode(): Node {
        return this
    }

    override fun getSelectedItemIndex(): Int {
        return selectedIndex
    }

    override fun setItems(
        navItemsList: MutableList<NodeItem>,
        startingIndex: Int
    ) {
        this.startingIndex = startingIndex
        this.selectedIndex = startingIndex

        navItems = navItemsList.map { it }.toMutableList()

        this.childNodes = navItems.map { navItem ->
            navItem.node.also {
                it.context.attachToParent(context)
                if (it.context.lifecycleState == LifecycleState.Started) {
                    activeNodeState.value = it
                }
            }
        }.toMutableList()

        navDrawerState.navItems = navItems
        navDrawerState.selectNavItem(navItems[startingIndex])

        if (context.lifecycleState == LifecycleState.Started) {
            pushNode(childNodes[startingIndex])
        }
    }

    override fun getItems(): MutableList<NodeItem> {
        return navItems
    }

    override fun addItem(nodeItem: NodeItem, index: Int) {
        navItems.add(index, nodeItem)
        childNodes.add(index, nodeItem.node)
        // The call to toMutableList() should return a new stack variable that triggers
        // recomposition in navDrawerState.
        navDrawerState.navItems = navItems.toMutableList()
    }

    override fun removeItem(index: Int) {
        navItems.removeAt(index)
        childNodes.removeAt(index)
        // The call to toMutableList() should return a new stack variable that triggers
        // recomposition in navDrawerState.
        navDrawerState.navItems = navItems.toMutableList()
    }

    override fun clearItems() {
        navItems.clear()
        childNodes.clear()
        stack.clear()
    }

    // endregion

    // region: DeepLink

    override fun getDeepLinkNodes(): List<Node>  {
        return childNodes
    }

    override fun onDeepLinkMatchingNode(matchingNode: Node) {
        println("DrawerNode.onDeepLinkMatchingNode() matchingNode = ${matchingNode.context.subPath}")
        pushNode(matchingNode)
    }

    // endregion

    @Composable
    override fun Content(modifier: Modifier) {
        println(
            """DrawerNode.Composing() stack.size = ${stack.size}
                |lifecycleState = ${context.lifecycleState}
            """.trimMargin()
        )

        NavigationDrawer(
            modifier = modifier,
            navDrawerState = navDrawerState
        ) {
            Box {
                val activeNodeUpdate = activeNodeState.value
                if (activeNodeUpdate != null && stack.size > 0) {
                    activeNodeUpdate.Content(Modifier)
                } else {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        text = "Empty Stack, Please add some children",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

}