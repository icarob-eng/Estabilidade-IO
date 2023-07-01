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


//    val a = center - Offset(s/2, s/2)
//    val b = center
//    val a = center - Offset(0f, s/2)
//    val b = center + Offset(0f, s/2)
//
//    drawBeam(a, b)
//    drawMoment(a, true, isReaction = true)
//    drawDistributedLoad(a, b, Offset(30f, 40f))
//    drawPointLoad(center + Offset(s, 0f), Offset(60f,80f), true)

    drawCircle(Color.Magenta, s/2, center, style = Stroke())  // unit circle
    val a = 1/2f
    val b = sqrt(2f)/2
    val c = sqrt(3f)/2

    drawPointLoad(center, Vector(1f, 0f) * s/2f, isReaction = true)
    drawPointLoad(center, Vector(c, a) * s/2f)
    drawPointLoad(center, Vector(b, b) * s/2f)
    drawPointLoad(center, Vector(a, c) * s/2f)
    drawPointLoad(center, Vector(0f, 1f) * s/2f, isReaction = true)
    drawPointLoad(center, Vector(-a, c) * s/2f)
    drawPointLoad(center, Vector(-b, b) * s/2f)
    drawPointLoad(center, Vector(-c, a) * s/2f)
    drawPointLoad(center, Vector(-1f, 0f) * s/2f, isReaction = true)
    drawPointLoad(center, Vector(-c, -a) * s/2f)
    drawPointLoad(center, Vector(-b, -b) * s/2f)
    drawPointLoad(center, Vector(-a, -c) * s/2f)
    drawPointLoad(center,  Vector(0f, -1f) * s/2f, isReaction = true)
    drawPointLoad(center, Vector(a, -c) * s/2f)
    drawPointLoad(center, Vector(b, -b) * s/2f)
    drawPointLoad(center, Vector(c, -a) * s/2f)



//    val axes = generatePoints()
//
//    chart(
//        axes = axes,
//        color = Color.Magenta,
//        origin = a,
//        xEnd = b,
//        yScale = getYScale(s, axes.second)
//    )
}

fun generatePoints(): Pair<List<Float>, List<Float>> {
    val points = 10
    val xLenght = 4
    // structure for generating x and y values
    val x = mutableListOf(0f)
    val y = mutableListOf(f(0f))
    var i = 0f
    while (i < points * xLenght){
        i += 1f
        x.add(i/points)
        y.add(f(i/points))
    }

    return Pair(x, y)
    // Size(1080.0, 2132.0)
}
fun f(x: Float) = (x - 1) * (x - 1) - 1  // sample function, todo: remove after use

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