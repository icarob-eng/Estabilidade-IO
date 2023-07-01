package com.moon.estabilidade_io.drawer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.moon.kstability.Axis
import com.moon.kstability.Node
import com.moon.kstability.Structure
import com.moon.kstability.Vector
import kotlin.math.absoluteValue
import kotlin.math.sqrt

enum class DiagramType {
    /**
     * Tells `MainCanvas` what to draw along with the given structure.
     * @property NONE Draw only the structure
     * @property LOADS Draw the structure and the applied loads
     * @property REACTIONS Draw the structure, applied loads, and reaction forces
     * @property NORMAL Draw the structure, reaction forces and Normal Stress Diagram
     * @property SHEAR Draw the structure, reaction forces and Shear Stress Diagram
     * @property MOMENT Draw the structure, reaction forces and Bending Moment Diagram
     */
    NONE, LOADS, REACTIONS, NORMAL, SHEAR, MOMENT
}

fun DrawScope.drawStructure(drawArgs: DrawArgs, structure: Structure, diagramType: DiagramType) {
    drawTest(1f) // draw scale test
    val s = Preferences.baseScale.toPx()
    /*
    # Draw order:
    1. Supports
    2. Beams
    3. Nodes
    4. Charts / Loads
     */

    // todo: draw given structure

    val a = center - Offset(0f, s/2)
    val b = center + Offset(0f, s/2)

    drawBeam(a, b)
    drawDistributedLoad(a, b, Vector(-30f, -40f))
    drawPointLoad(center + Offset(s, 0f), Vector(60f,80f), true)
    drawMoment(a, true, isReaction = true)

    drawLabel((a+b)/2f, 50.u("kN/m"), drawArgs, Directions.R)
    drawLabel(a, 50.u("kNm"), drawArgs, Directions.T)
}

/**
 * Return scale that make the axis be limited between -baseScale and baseScale.
 */
fun getYScale(baseScale: Float, axis: Axis): Float {
    val absMax = if (axis.max() >= axis.min().absoluteValue) axis.max() else axis.min().absoluteValue
    return baseScale / if (absMax != 0f) absMax else 1f
}

fun Node.toOffset() = Offset(pos.x, pos.y)
fun Vector.toOffset() = Offset(x, y)
fun Offset.toVector() = Vector(x, y)

fun Number.u(u: String) = "$this $u"