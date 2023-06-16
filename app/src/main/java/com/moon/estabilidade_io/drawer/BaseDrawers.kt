@file:OptIn(ExperimentalTextApi::class)

package com.moon.estabilidade_io.drawer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp


fun DrawScope.drawScaleLabel(drawArgs: DrawArgs) {
    val s = Preferences.baseScale.toPx()

    val baseOffset = Offset(
        Preferences.scaleRightMargin.toPx(),
        (size.height - Preferences.scaleBottomMargin.toPx())
    )
    val endOffset = baseOffset +
            Offset(s * drawArgs.scaleValue, 0f)

    drawLine(color = Color.Black, start = baseOffset, end = endOffset, strokeWidth = 5.dp.toPx())

    drawText(
        drawArgs.textMeasurer,
        "1m",
        baseOffset + Offset(0f, - Preferences.textLineSize.toPx()),
        TextStyle(color = Color.Black, fontSize = Preferences.textSize)
    )
}

private fun DrawScope.drawTriangle(topPoint: Offset) {
    val s = Preferences.baseScale.toPx() * Preferences.supportSide

    val path = Path()
    path.moveTo(topPoint.x, topPoint.y)
    path.lineTo(topPoint.x + s / 2, topPoint.y + s * 4/5)
    path.lineTo(topPoint.x - s / 2, topPoint.y + s * 4/5)
    path.close()
    drawPath(
        path = path,
        color = Preferences.supportColor1,
    )
    if (Preferences.showEdges)
        drawPath(
            path = path,
            color = Preferences.supportColor3,
            style = Stroke(s / 20)
        )
}

private fun DrawScope.drawCenteredHatches(middlePoint: Offset, nHatches: Int = 5) {
    val s = Preferences.baseScale.toPx() * Preferences.supportSide

    val endOffset = middlePoint + Offset(s/2, 0f)
    drawLine(
        color = Preferences.supportColor2,
        start = middlePoint + Offset(-s/2 - s/5, 0f),
        end = endOffset,
        strokeWidth = s/15
    )

    val h = s/8
    for (i in 0 downTo -nHatches) {
        drawLine(
            color = Preferences.supportColor2,
            start = endOffset + Offset(h * i, 0f),
            end = endOffset + Offset(h * (i - 1), h),
            strokeWidth = s/25
        )
    }
}

fun DrawScope.drawRoller(appliedNodeOffset: Offset) {
    val s = Preferences.baseScale.toPx() * Preferences.supportSide

    drawCenteredHatches(appliedNodeOffset + Offset(0f, s * 4 / 5))
    drawCircle(
        color = Preferences.supportColor1,
        radius = s / 2 * 4 / 5,
        center = appliedNodeOffset + Offset(0f, s / 2 * 4 / 5)
    )
    if (Preferences.showEdges)
        drawCircle(
            color = Preferences.supportColor3,
            radius = s / 2 * 4 / 5,
            center = appliedNodeOffset + Offset(0f, s / 2 * 4 / 5),
            style = Stroke(s / 25)
        )
}

fun DrawScope.drawRollerB(appliedNodeOffset: Offset) {
    val s = Preferences.baseScale.toPx() * Preferences.supportSide

    drawTriangle(appliedNodeOffset)
    drawLine(
        color = Preferences.supportColor2,
        start = appliedNodeOffset + Offset(-s / 2 - s / 5, s * 9 / 10),
        end = appliedNodeOffset + Offset(s / 2, s * 9 / 10),
        strokeWidth = s / 15
    )  // maybe put the hatches instead of this line
}

fun DrawScope.drawHinge(appliedNodeOffset: Offset) {
    val s = Preferences.baseScale.toPx() * Preferences.supportSide

    drawCenteredHatches(appliedNodeOffset + Offset(0f, s * 4 / 5))
    drawTriangle(appliedNodeOffset)
}

fun DrawScope.drawFixed(appliedNodeOffset: Offset) {
//    val s = Preferences.baseScale.toPx()
    rotate(90f) {scale(Preferences.supportSide, Preferences.supportSide) {
        drawCenteredHatches(appliedNodeOffset, 7)
    }}
}

fun DrawScope.drawBeam(offset1: Offset, offset2: Offset) {
    val s = Preferences.baseScale.toPx()

    drawLine(
        color = Preferences.beamColor1,
        start = offset1,
        end = offset2,
        strokeWidth = s/15,
    )
}

fun DrawScope.drawNode(appliedNodeOffset: Offset) {
    val s = Preferences.baseScale.toPx()

    drawCircle(
        color = Preferences.nodeColor1,
        center = appliedNodeOffset,
        radius = s/30
    )
    drawCircle(
        color = Preferences.nodeColor2,
        center = appliedNodeOffset,
        radius = s/25,
        style = Stroke(s/50)
    )
}

fun DrawScope.drawTest(length: Float){
    val s = Preferences.baseScale.toPx()

    drawLine(
        Color.Blue,
        Offset(center.x-(length * s)/2, center.y),
        Offset(center.x+(length * s)/2, center.y)
    )
    drawLine(
        Color.Blue,
        Offset(center.x, center.y-(length * s)/2),
        Offset(center.x, center.y+(length * s)/2)
    )
}
