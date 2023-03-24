package com.pablichj.incubator.uistate3.node.split

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pablichj.incubator.uistate3.node.Component

open class SplitComponent(
    private val config: Config = Config()
) : Component() {

    private var topComponent: Component? = null
    private var bottomComponent: Component? = null

    fun setTopNode(topComponent: Component) {
        this.topComponent = topComponent.apply {
            setParent(this@SplitComponent)
        }
    }

    fun setBottomNode(bottomComponent: Component) {
        this.bottomComponent = bottomComponent.apply {
            setParent(this@SplitComponent)
        }
    }

    override fun start() {
        super.start()
        println("$clazz::start")
        topComponent?.start()
        bottomComponent?.start()
    }

    override fun stop() {
        super.stop()
        println("$clazz::stop")
        topComponent?.stop()
        bottomComponent?.stop()
    }

    @Composable
    override fun Content(modifier: Modifier) {
        println("SplitNavNode::Composing()")
        Column(modifier = Modifier.fillMaxSize()) {
            val TopNodeCopy = topComponent
            if (TopNodeCopy != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5F)
                        .padding(start = 40.dp, top = 40.dp, end = 40.dp, bottom = 20.dp)
                ) {
                    TopNodeCopy.Content(Modifier)
                }
            }

            val BottomNodeCopy = bottomComponent
            if (BottomNodeCopy != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(start = 40.dp, top = 20.dp, end = 40.dp, bottom = 40.dp)
                ) {
                    BottomNodeCopy.Content(Modifier)
                }
            }
        }
    }

    class Config(
        var splitStyle: SplitStyle = SplitStyle()
    )

}