package com.pablichj.incubator.uistate3.node.topbar

import TopBar
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.pablichj.incubator.uistate3.FloatingBackButton
import com.pablichj.incubator.uistate3.node.Component

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TopBarScaffold(
    modifier: Modifier,
    topBarState: ITopBarState,
    childComponent: Component?,
    animationType: AnimationType
) {
//    var pointerEnabled by remember(topBarState) { mutableStateOf(true) }
//    var startX = remember(topBarState) { mutableStateOf(Float.MAX_VALUE) }
//    var deltaX by remember(topBarState) { mutableStateOf(0f) }
//    var showBackNavigation = derivedStateOf { deltaX > 100 && startX.value <= 50 }

//    val nestedScrollDispatcher = remember { NestedScrollDispatcher() }

    /*val nestedScrollConnection = rememberNestedScrollConnection(
        onOffsetChanged = {
            println("Pablo:: nestedScrollConnection.onOffsetChanged{$it}")
            nestedScrollDispatcher.dispatchPreScroll(
                available = Offset(it, 0f),
                source = NestedScrollSource.Drag
            )
        },
        startX
    )*/

    /*val scrollableState = rememberScrollableState (
        consumeScrollDelta = { delta ->
            println("Pablo:: rememberScrollableState:scrolled{$delta}")
            val consumed = nestedScrollDispatcher.dispatchPreScroll(
                available = Offset(delta, 0f),
                source = NestedScrollSource.Drag
            )
            nestedScrollDispatcher.dispatchPostScroll(
                consumed = Offset(0f, 0f),
                available = Offset(delta *//*- consumed.x*//*, y = 0f),
                source = NestedScrollSource.Drag
            )
            delta
        }
    )*/

    Scaffold(
        modifier = modifier,
        topBar = { TopBar(topBarState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
//                .pointerInteropFilter()
//                .nestedScroll(nestedScrollConnection, nestedScrollDispatcher)
            /*.swipeable(
                state = swipeableState,
                orientation = Orientation.Horizontal,
                anchors = mapOf(
                    0f to 100f,
                    maxHeight to States.COLLAPSED,
                )
            )*/
            /*.scrollable(
                state = scrollableState,
                orientation = Orientation.Horizontal,
            )*/
/*
                .draggable(
                    state = rememberDraggableState {
                        println("Pablo:: rememberDraggableState.{$it}")
                        val consumed = nestedScrollDispatcher.dispatchPreScroll(
                            available = Offset(it, 0f),
                            source = NestedScrollSource.Drag
                        )

                        nestedScrollDispatcher.dispatchPostScroll(
                            consumed = Offset(it - consumed.x, 0f),
                            available = Offset(x = it, y = 0f),
                            source = NestedScrollSource.Drag
                        )
                    },
                    orientation = Orientation.Horizontal,
                )
*/
/*
                .pointerInputEnabler(pointerEnabled, topBarState) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            println("Pablo::onDragStart offset = $offset")
                            if (offset.x > 50) {
                                //pointerEnabled = false
                                val parentsConsumed = nestedScrollDispatcher.dispatchPreScroll(
                                    available = Offset(offset.x, 0f),
                                    source = NestedScrollSource.Drag
                                )
                                nestedScrollDispatcher.dispatchPostScroll(
                                    consumed = Offset(0f, 0f),
                                    available = Offset(0f, 0f),//Offset(offset.x - parentsConsumed.x, y = 0f),
                                    source = NestedScrollSource.Drag
                                )
                            }
                            startX.value = offset.x
                        },
                        onDrag = { inputChange, offset ->
                            println("Pablo::onDrag deltaX = $deltaX, offset = $offset")
                            //inputChange.consume()
                            deltaX += offset.x
                            if (startX.value > 50) {
                                val parentsConsumed = nestedScrollDispatcher.dispatchPreScroll(
                                    available = inputChange.scrollDelta,
                                    source = NestedScrollSource.Drag
                                )
                                nestedScrollDispatcher.dispatchPostScroll(
                                    consumed = parentsConsumed,//Offset(0f, 0f),
                                    available = inputChange.scrollDelta,//Offset(, y = 0f),
                                    source = NestedScrollSource.Drag
                                )
                            }
                        },
                        onDragCancel = {
                            println("Pablo::onDragCancel")
                            startX.value = Float.MAX_VALUE
                            deltaX = 0f
                        },
                        onDragEnd = {
                            println("Pablo::onDragEnd")
                            if (startX.value <= 50 && deltaX > 150) {
                                startX.value = Float.MAX_VALUE
                                deltaX = 0f
                                topBarState.handleBackPress()
                            }
                            startX.value = Float.MAX_VALUE
                            deltaX = 0f
                        }
                    )
                }
 */
        ) {
            if (childComponent != null) {
                AnimatedContent(
                    targetState = childComponent,
                    transitionSpec = { getTransitionByAnimationType(animationType) }
                ) {
                    it.Content(Modifier)
                }

                // "Predictive back" bellow
/*
                if (showBackNavigation.value) {
                    FloatingBackButton(
                        modifier = Modifier.align(Alignment.CenterStart),
                        onClick = {}
                    )
                }
*/
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

@OptIn(ExperimentalAnimationApi::class)
private fun getTransitionByAnimationType(animationType: AnimationType): ContentTransform {
    return when (animationType) {
        AnimationType.Direct -> {
            slideInHorizontally(
                initialOffsetX = { fullWidth ->
                    fullWidth
                },
                animationSpec = tween(
                    durationMillis = 300,
                    delayMillis = 0
                )
            ) with//+ fadeIn(animationSpec = tween())
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth ->
                            -fullWidth
                        },
                        animationSpec = tween(
                            durationMillis = 300,
                            delayMillis = 0
                        )
                    ) //+ fadeOut(animationSpec = tween())
        }
        AnimationType.Reverse -> {
            slideInHorizontally(
                initialOffsetX = { fullWidth ->
                    -fullWidth
                },
                animationSpec = tween()
            ) with//+ fadeIn(animationSpec = tween())
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth ->
                            fullWidth
                        },
                        animationSpec = tween()
                    ) //+ fadeOut(animationSpec = tween())
        }
        AnimationType.Exit,
        AnimationType.Enter -> {
            fadeIn(
                animationSpec = tween()
            ) with fadeOut(
                animationSpec = tween()
            )
        }
    }
}

sealed class AnimationType {
    object Direct : AnimationType()
    object Reverse : AnimationType()
    object Enter : AnimationType()
    object Exit : AnimationType()
}

/*fun Modifier.pointerInputEnabler(
    enable: Boolean,
    key1: Any?,
    block: suspend PointerInputScope.() -> Unit
): Modifier {
    return if (enable) {
        this.pointerInput(key1, block)
    } else {
        this
    }
}*/

/*
@Composable
fun rememberNestedScrollConnection() = remember {

    object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            println("Pablo::onPreScroll() available.x = ${available.x}")
            return super.onPreScroll(available, source)

        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            println("Pablo::onPostScroll()")
            return super.onPostScroll(consumed, available, source)
        }

        override suspend fun onPreFling(available: Velocity): Velocity {
            println("Pablo::onPreFling()")
            return super.onPreFling(available)
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            println("Pablo::onPostFling()")
            return super.onPostFling(consumed, available)
        }

    }

}
*/