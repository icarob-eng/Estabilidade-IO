package com.moon.estabilidade_io.drawer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope


fun DrawScope.drawStructure(drawArgs: DrawArgs) {  // todo: pass structure data as argument
    drawTest(1f) // draw scale test
    // scale and rotate elements here

    val s = Preferences.baseScale.toPx()

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
}