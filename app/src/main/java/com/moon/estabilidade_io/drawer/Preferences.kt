package com.moon.estabilidade_io.drawer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// todo: doc this
object Preferences {
    /**
     * Base unit: 1 meter = 50 dp, which means: 3m beam would measure, by default, 150 dp
     */
    val baseScale = 50.dp

    // positions the scale label
    val scaleRightMargin = 30.dp
    val scaleBottomMargin = 30.dp

    // --- text configs ---
    val textSize = 20.sp
    val textLineSize = 30.sp

    // --- support configs ---
    // scale of the hinge (default size is 1 m square)
    const val supportSide = 1/4f
    const val useRollerB = false

    // --- edges and widths configs ---
    const val showEdges = true
    const val beamWidth = 1/20f
    const val edgesWidth = beamWidth/4

    // --- colors ---
    val supportColor1 = Color.LightGray
    val supportColor2 = Color.DarkGray
    val supportColor3 = Color.Gray
    val beamColor1 = Color.Gray
    val beamColor2 = Color.DarkGray
    val nodeColor1 = Color.Gray
    val nodeColor2 = Color.DarkGray
    val loadColor = Color.Blue
    val reactionColor = Color.Red
    val normalDiagramColor = Color.Magenta
    val shearDiagramColor = Color.Yellow
    val momentDiagramColor = Color.Green

    const val chartAbsWidth = 1f
}