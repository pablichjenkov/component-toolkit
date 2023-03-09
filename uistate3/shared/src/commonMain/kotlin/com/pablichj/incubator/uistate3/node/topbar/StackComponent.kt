package com.pablichj.incubator.uistate3.node.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.pablichj.incubator.uistate3.node.*
import com.pablichj.incubator.uistate3.node.StackComponent
import com.pablichj.incubator.uistate3.node.backstack.BackStack
import com.pablichj.incubator.uistate3.node.navigation.DeepLinkResult

abstract class StackComponent(
    val screenIcon: ImageVector? = null,
) : Component(), StackComponent {
    final override val backStack = BackStack<Component>()
    var activeComponent: MutableState<Component?> = mutableStateOf(null)
    private val topBarState = TopBarState {
        handleBackPressed()
    }

    private var lastBackstackEvent: BackStack.Event<Component>? = null

    init {
        this@StackComponent.backStack.eventListener = { event ->
            lastBackstackEvent = event
            val stackTransition = processBackstackEvent(event)
            processBackstackTransition(stackTransition)
        }
    }

    override fun start() {
        super.start()
        if (activeComponent.value != null) {
            println("$clazz::start() with activeNodeState = ${activeComponent.value?.clazz}")
            activeComponent.value?.start()
        }
    }

    override fun stop() {
        println("$clazz::stop()")
        super.stop()
        activeComponent.value?.stop()
        lastBackstackEvent = null
    }

    override fun handleBackPressed() {
        println("$clazz::handleBackPressed, backStack.size = ${backStack.size()}")
        if (backStack.size() > 1) {
            backStack.pop()
        } else {
            // We delegate the back event when the stack has 1 element and not 0. The reason is, if
            // we pop all the way to zero the stack empty view will be show for a fraction of
            // milliseconds and this creates an undesirable effect.
            delegateBackPressedToParent()
        }
    }

    // region: NavigatorItems

    override fun getComponent(): Component {
        return this
    }

    /*override fun updateSelectedNavItem(newTop: Component) {
        val selectedNavItem = getNavItemFromNode(newTop)
        if (backStack.size() > 1) {
            setTitleSectionForBackClick(selectedNavItem)
        } else {
            setTitleSectionForHomeClick(selectedNavItem)
        }
    }*/

    internal fun processBackstackTransition(
        stackTransition: StackTransition<Component>
    ) {
        when (stackTransition) {
            is StackTransition.In -> {
                //updateSelectedNavItem(stackTransition.newTop)
                activeComponent.value = stackTransition.newTop
            }
            is StackTransition.InOut -> {
                //updateSelectedNavItem(stackTransition.newTop)
                activeComponent.value = stackTransition.newTop
            }
            is StackTransition.InvalidPushEqualTop -> {}
            is StackTransition.InvalidPopEmptyStack -> {
                activeComponent.value = null
            }
            is StackTransition.Out -> {
                activeComponent.value = null
            }
        }
    }

    override fun onDestroyChildComponent(component: Component) {
        if (component.lifecycleState == ComponentLifecycleState.Started) {
            component.stop()
            component.destroy()
        } else {
            component.destroy()
        }
    }

    // endregion

    private fun setTitleSectionForHomeClick(navItem: NavItem) {
        topBarState.setTitleSectionState(
            TitleSectionStateHolder(
                title = navItem.label,
                icon1 = resolveFirstIcon(),
                onIcon1Click = {
                    findClosestIDrawerNode()?.open()
                },
                onTitleClick = {
                    findClosestIDrawerNode()?.open()
                }
            )
        )
    }

    private fun setTitleSectionForBackClick(navItem: NavItem) {
        topBarState.setTitleSectionState(
            TitleSectionStateHolder(
                title = navItem.label,
                onTitleClick = {
                    handleBackPressed()
                },
                icon1 = resolveFirstIcon(),
                onIcon1Click = {
                    findClosestIDrawerNode()?.open()
                },
                icon2 = Icons.Filled.ArrowBack,
                onIcon2Click = {
                    handleBackPressed()
                }
            )
        )
    }

    private fun resolveFirstIcon(): ImageVector? {
        val canProvideGlobalNavigation = findClosestIDrawerNode() != null
        return if (canProvideGlobalNavigation) {
            Icons.Filled.Menu
        } else {
            screenIcon
        }
    }

    // region: DeepLink

    override fun getDeepLinkSubscribedList(): List<Component> {
        return childComponents
    }

    override fun onDeepLinkNavigation(matchingComponent: Component): DeepLinkResult {
        println("$clazz.onDeepLinkMatch() matchingNode = ${matchingComponent.clazz}")
        backStack.push(matchingComponent)
        return DeepLinkResult.Success
    }

    // endregion

    @Composable
    override fun Content(modifier: Modifier) {
        println("""
          $clazz::Composing(), backStack.size = ${backStack.size()}
          lastBackstackEvent = $lastBackstackEvent
        """)

        val animationType = when (lastBackstackEvent) {
            is BackStack.Event.Pop -> {
                if (backStack.size() > 0)
                    AnimationType.Reverse
                else AnimationType.Exit
            }
            is BackStack.Event.PopEmptyStack -> {
                AnimationType.Enter
            }
            is BackStack.Event.Push -> {
                if (backStack.size() > 1)
                    AnimationType.Direct
                else AnimationType.Enter
            }
            is BackStack.Event.PushEqualTop -> {
                AnimationType.Enter
            }
            null -> AnimationType.Enter
        }

        val prevComponent = if (backStack.size() > 1){
            backStack.deque[backStack.size()-2]
        } else {
            null
        }

        TopBarScaffold(
            modifier,
            topBarState,
            activeComponent.value,
            prevComponent,
            animationType
        )
    }

}