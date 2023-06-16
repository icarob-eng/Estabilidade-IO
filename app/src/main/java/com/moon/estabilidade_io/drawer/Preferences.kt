package com.moon.estabilidade_io.drawer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Preferences {
    /**
     * Base unit: 1 meter = 50 dp, which means: 3m beam would measure, by default, 150 dp
     */
    val baseScale = 50.dp

    // positions the scale label
    val scaleRightMargin = 30.dp
    val scaleBottomMargin = 30.dp

    val textSize = 20.sp
    val textLineSize = 30.sp

    // scale of the hinge (default size is 1 m square)
    val supportSide = 0.25f
    val supportColor1 = Color.Red
    val supportColor2 = Color.DarkGray
    val supportColor3 = Color.Yellow

    val showEdges = true

    val beamColor1 = Color.Gray
    val beamColor2 = Color.DarkGray

    val nodeColor1 = Color.Gray
    val nodeColor2 = Color.DarkGray
}