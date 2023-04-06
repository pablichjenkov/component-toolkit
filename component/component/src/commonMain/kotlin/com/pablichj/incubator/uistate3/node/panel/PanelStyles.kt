package com.pablichj.incubator.uistate3.node.panel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class PanelHeaderStyle(
    var bgColor: Color = Color.Cyan,
    var titleTextSize: TextUnit = 20.sp,
    var descriptionTextSize: TextUnit = 16.sp
)

class PanelItemStyle(
    var unselectedColor: Color = Color.LightGray,
    var selectedColor: Color = Color.Gray,
    var textSize: Dp = 14.dp
)
