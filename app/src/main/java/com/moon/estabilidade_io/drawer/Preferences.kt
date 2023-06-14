package com.moon.estabilidade_io.drawer

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

    val textSize = 12.sp
    val textLineSize = 20.sp
}