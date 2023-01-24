package com.pablichj.incubator.uistate3.node.adaptable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.pablichj.incubator.uistate3.node.ContainerNode
import com.pablichj.incubator.uistate3.node.NodeItem
import com.pablichj.incubator.uistate3.node.Node
import com.pablichj.incubator.uistate3.node.NodeContext
import com.pablichj.incubator.uistate3.node.navigation.DeepLinkResult
import com.pablichj.incubator.uistate3.node.navigation.Path

/**
 * This node is basically a proxy, it transfer request and events to its active child node
 * */
class AdaptableSizeNode(
    var windowSizeInfoProvider: IWindowSizeInfoProvider
) : Node() {

    private var navItems: MutableList<NodeItem> = mutableListOf()
    private var startingPosition: Int = 0
    private var CompactNavigator: ContainerNode? = null
    private var MediumNavigator: ContainerNode? = null
    private var ExpandedNavigator: ContainerNode? = null
    private var currentContainerNode =
        mutableStateOf<ContainerNode?>(null) // todo: This should be a reactive state

    fun setNavItems(navItems: MutableList<NodeItem>, startingPosition: Int) {
        this.navItems = navItems
        this.startingPosition = startingPosition
        currentContainerNode.value?.setItems(navItems, startingPosition)
    }

    fun setCompactContainer(containerNode: ContainerNode) {
        CompactNavigator = containerNode
        containerNode.getNode().context.parentContext = this.context
    }

    fun setMediumContainer(containerNode: ContainerNode) {
        MediumNavigator = containerNode
        containerNode.getNode().context.parentContext = this.context
    }

    fun setExpandedContainer(containerNode: ContainerNode) {
        ExpandedNavigator = containerNode
        containerNode.getNode().context.parentContext = this.context
    }

    override fun start() {
        super.start()
        currentContainerNode.value?.getNode()?.start()
    }

    override fun stop() {
        super.stop()
        currentContainerNode.value?.getNode()?.stop()
    }

    // region: DeepLink

    override fun getDeepLinkNodes(): List<Node> {
        return listOfNotNull(
            CompactNavigator?.getNode(),
            MediumNavigator?.getNode(),
            ExpandedNavigator?.getNode()
        )
    }

    override fun onCheckChildMatchHandler(advancedPath: Path, matchingNode: Node): DeepLinkResult {
        val interceptingNode = currentContainerNode.value?.getNode() ?: matchingNode
        interceptingNode.context.subPath = matchingNode.context.subPath.copy()
        return interceptingNode.checkDeepLinkMatch(advancedPath)
    }

    override fun onNavigateChildMatchHandler(
        advancedPath: Path,
        matchingNode: Node
    ): DeepLinkResult {
        val interceptingNode = currentContainerNode.value?.getNode() ?: matchingNode
        interceptingNode.context.subPath = matchingNode.context.subPath.copy()
        onDeepLinkMatchingNode(interceptingNode)
        return interceptingNode.navigateUpToDeepLink(advancedPath)
    }

    override fun onDeepLinkMatchingNode(matchingNode: Node) {
        println("AdaptableWindowNode.onDeepLinkMatchingNode() matchingNode = ${matchingNode.context.subPath}")
    }

    // endregion

    @Composable
    override fun Content(modifier: Modifier) {
        println("AdaptableWindowNode.Composing() lifecycleState = ${context.lifecycleState}")

        val windowSizeInfo by windowSizeInfoProvider.windowSizeInfo()

        currentContainerNode.value = when (windowSizeInfo) {
            WindowSizeInfo.Compact -> {
                tryTransfer(currentContainerNode.value, CompactNavigator)
            }
            WindowSizeInfo.Medium -> {
                tryTransfer(currentContainerNode.value, MediumNavigator)
            }
            WindowSizeInfo.Expanded -> {
                tryTransfer(currentContainerNode.value, ExpandedNavigator)
            }
        }

        val CurrentNode = currentContainerNode.value?.getNode()

        if (CurrentNode != null) {
            CurrentNode.Content(modifier)
        } else {
            Box {
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

    private fun tryTransfer(
        donorContainerNode: ContainerNode?,
        adoptingContainerNode: ContainerNode?
    ): ContainerNode? {

        if (adoptingContainerNode == donorContainerNode) {
            return adoptingContainerNode
        }

        val adoptingNavigatorCopy = adoptingContainerNode ?: return donorContainerNode

        return if (donorContainerNode == null) { // The first time when no node has been setup yet
            adoptingContainerNode.setItems(navItems, startingPosition)
            adoptingContainerNode.getNode().start()
            adoptingContainerNode
        } else { // do the real transfer here
            adoptingNavigatorCopy.transferFrom(donorContainerNode)
            donorContainerNode.getNode().stop()
            adoptingContainerNode.getNode().start()
            adoptingContainerNode
        }
    }

}