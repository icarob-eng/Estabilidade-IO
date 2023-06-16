@file:OptIn(ExperimentalTextApi::class)

package com.moon.estabilidade_io.drawer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
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
    if (Preferences.showEdges) {
        val beam = (offset2 - offset1)/(offset2 - offset1).getDistance() * s/25f
        translate(- beam.y, beam.x) {
            drawLine(
                color = Preferences.beamColor2,
                start = offset1,
                end = offset2,
                strokeWidth = s / 50,
            )
        }
        translate(+ beam.y,- beam.x) {
            drawLine(
                color = Preferences.beamColor2,
                start = offset1,
                end = offset2,
                strokeWidth = s / 50,
            )
        }
    }
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

/**
 * Plot the chart of the given `Axes` on the specified locations. The plot direction, origin and
 * vertical scale is specified by the arguments.
 *
 * @param axes Axes to be plotted. The x-axis (in length units) will be distributed along the origin
 * to xLength vector, while the y-axis will be plotted perpendicularly to said vector.
 * @param color
 * @param origin Offset where the plot will begin, the "0" point of the plot.
 * @param xLength Offset representing where the x-axis will end.
 * @param yScale Float that the termines the relation between the plot y-axis scale and the Canvas
 * pixel count. yScale = 1 means that each unit in the y-axis will represent 1 pixel.
 */
fun DrawScope.chart(axes: Axes, color: Color, origin: Offset, xLength: Offset, yScale: Float) {
    val iHat = (xLength - origin)
    val jHat = Offset(-iHat.y, iHat.x)/iHat.getDistance() * yScale  // todo: test if this is accepted

    val path = Path()
    path.moveTo(origin.x, origin.y)
    for (i in 1 until axes.first.size) {
        val k = axes.first[i] / axes.first.last()
        val xVec = iHat * k
        val p = xVec + jHat * axes.second[i]
        path.lineTo(p.x, p.y)
    }
    drawPath(
        path = path,
        color = color,
        style = Stroke(Preferences.chartWidth)
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
