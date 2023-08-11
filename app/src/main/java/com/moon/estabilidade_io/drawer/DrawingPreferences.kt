package com.moon.estabilidade_io.drawer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.estabilidade_io.ui.viewModels.SettingsActivityState

// todo: doc this
object DrawingPreferences {
    /**
     * Base unit: 1 meter = 50 dp, which means: 3m beam would measure, by default, 150 dp
     */
    var baseScale = 50.dp  // configurable
        private set

    // positions the scale label
    var scaleRightMargin = 30.dp
        private set
    var scaleBottomMargin = 30.dp
        private set

    // --- text configs ---
    var textSize = 20.sp  // configurable
        private set
    var textLineSize = 30.sp
        private set

    // --- support configs ---
    // scale of the hinge (default size is 1 m square)
    var supportSide = 1/4f
        private set
    var useRollerB = false  // configurable
        private set

    // --- edges and widths configs ---
    var showEdges = true  // configurable
        private set
    var beamWidth = 1/20f
        private set
    var edgesWidth = beamWidth/4
        private set

    // --- colors ---  // use hex
    var supportColor1 = Color.LightGray
        private set
    var supportColor2 = Color.DarkGray
        private set
    var supportColor3 = Color.Gray
        private set
    var beamColor1 = Color.Gray
        private set
    var beamColor2 = Color.DarkGray
        private set
    var nodeColor1 = Color.Gray
        private set
    var nodeColor2 = Color.DarkGray
        private set
    var loadColor = Color.Blue
        private set
    var reactionColor = Color.Red
        private set
    var normalDiagramColor = Color.Magenta
        private set
    var shearDiagramColor = Color.Yellow
        private set
    var momentDiagramColor = Color.Green
        private set

    var chartAbsWidth = 1f  // configurable
        private set

    fun updateFromSettingsState(state: SettingsActivityState) {
        useRollerB = state.useRollerB
        showEdges = state.showEdges
    }
}