package com.moon.estabilidade_io.drawer

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

// todo: use kstability typealias
typealias Axis = List<Float>
typealias Axes = Pair<Axis, Axis>

fun DrawScope.drawStructure(drawArgs: DrawArgs) {  // todo: pass structure data as argument
    drawTest(1f) // draw scale test
    // scale and rotate elements here

    val s = Preferences.baseScale.toPx()

    /*
    # Draw order:
    1. Supports
    2. Beams
    3. Nodes
    4. Charts / Loads
     */

    val nodeA = Offset(center.x - s, center.y)
    val nodeB = Offset(center.x, center.y)
    val nodeC = Offset(center.x + s, center.y)
    val nodeD = Offset(center.x - s/2, center.y - s/2)
    val nodeE = Offset(center.x + s/2, center.y - s/2)

    drawRoller(nodeA)
    drawHinge(nodeC)

    drawBeam(nodeA, nodeB)
    drawBeam(nodeB, nodeC)

    drawBeam(nodeA, nodeD)
    drawBeam(nodeB, nodeD)
    drawBeam(nodeB, nodeE)
    drawBeam(nodeC, nodeE)

    drawBeam(nodeD, nodeE)


    drawNode(nodeA)
    drawNode(nodeB)
    drawNode(nodeC)
    drawNode(nodeD)
    drawNode(nodeE)

    var x = mutableListOf(0f)
    var y = mutableListOf(f(0f))
    var i = 0f
    while (i <= 1000){
        i += 1f
        x.add(i)
        y.add(f(i))
    }

    x = x.map{ it }.toMutableList()
    y = y.map{ it }.toMutableList()

    val axes = Pair(x, y)
    // Size(1080.0, 2132.0)

    chart(
        axes = axes,
        color = Color.Magenta,
        origin = center,
        xLength = center + Offset(3 *s, 0f),
        yScale = s
    )
}

fun f(x: Float) = 2 * x