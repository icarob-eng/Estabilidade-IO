@file:OptIn(ExperimentalTextApi::class)

package com.moon.estabilidade_io.drawer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.rotateRad
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.moon.kstability.Axes
import com.moon.kstability.Vector
import kotlin.math.absoluteValue
import kotlin.math.atan
import kotlin.math.sign

fun DrawScope.drawScaleLabel(textMeasurer: TextMeasurer, scaleValue: Float) {
    val s = Preferences.baseScale.toPx()

    val baseOffset = Offset(
        Preferences.scaleRightMargin.toPx(),
        (size.height - Preferences.scaleBottomMargin.toPx())
    )
    val endOffset = baseOffset +
            Offset(s * scaleValue, 0f)

    drawLine(color = Color.Black, start = baseOffset, end = endOffset, strokeWidth = 5.dp.toPx())

    drawText(
        textMeasurer,
        "1 m",
        baseOffset + Offset(0f, - Preferences.textLineSize.toPx()),
        TextStyle(color = Color.Black, fontSize = Preferences.textSize)
    )
}

private fun DrawScope.drawTriangle(
    topPoint: Offset,
    s: Float = Preferences.baseScale.toPx() * Preferences.supportSide,
    showEdges: Boolean = Preferences.showEdges,
    color: Color = Preferences.supportColor1
) {
    val path = Path()
    path.moveTo(topPoint.x, topPoint.y)
    path.lineTo(topPoint.x + s / 2, topPoint.y + s * 4/5)
    path.lineTo(topPoint.x - s / 2, topPoint.y + s * 4/5)
    path.close()
    drawPath(
        path = path,
        color = color,
    )
    if (showEdges)
        drawPath(
            path = path,
            color = Preferences.supportColor3,
            style = Stroke(s * Preferences.edgesWidth)
        )
}

private fun DrawScope.drawCenteredHatches(
    middlePoint: Offset,
    nHatches: Int = 5,
    s: Float = Preferences.baseScale.toPx() * Preferences.supportSide
) {

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

fun DrawScope.drawRoller(
    appliedNodeOffset: Offset,
    s: Float = Preferences.baseScale.toPx() * Preferences.supportSide
) {

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
            style = Stroke(s * Preferences.edgesWidth)
        )
}

fun DrawScope.drawRollerB(
    appliedNodeOffset: Offset,
    s: Float = Preferences.baseScale.toPx() * Preferences.supportSide
) {
    drawTriangle(appliedNodeOffset)
    drawLine(
        color = Preferences.supportColor2,
        start = appliedNodeOffset + Offset(-s / 2 - s / 5, s * 9 / 10),
        end = appliedNodeOffset + Offset(s / 2, s * 9 / 10),
        strokeWidth = s / 15
    )  // maybe put the hatches instead of this line
}

fun DrawScope.drawHinge(
    appliedNodeOffset: Offset,
    s: Float = Preferences.baseScale.toPx() * Preferences.supportSide
) {
    drawCenteredHatches(appliedNodeOffset + Offset(0f, s * 4 / 5))
    drawTriangle(appliedNodeOffset)
}

fun DrawScope.drawFixed(
    appliedNodeOffset: Offset,
    s: Float = Preferences.baseScale.toPx() * Preferences.supportSide
) {
    rotate(90f, appliedNodeOffset) {
        scale(s, appliedNodeOffset) {
        drawCenteredHatches(appliedNodeOffset, 7)
    }}
}

fun DrawScope.drawBeam(
    offset1: Offset, offset2: Offset,
    s: Float = Preferences.baseScale.toPx()
) {
    drawLine(
        color = Preferences.beamColor1,
        start = offset1,
        end = offset2,
        strokeWidth = s * Preferences.beamWidth,
    )
    if (Preferences.showEdges) {
        val edgeOffset = (offset2 - offset1)/(offset2 - offset1).getDistance() *
                s * (Preferences.beamWidth/2 - Preferences.edgesWidth/4)
        translate(- edgeOffset.y, edgeOffset.x) {
            drawLine(
                color = Preferences.beamColor2,
                start = offset1,
                end = offset2,
                strokeWidth = s * Preferences.edgesWidth
            )
        }
        translate(+ edgeOffset.y,- edgeOffset.x) {
            drawLine(
                color = Preferences.beamColor2,
                start = offset1,
                end = offset2,
                strokeWidth = s * Preferences.edgesWidth
            )
        }
    }
}

fun DrawScope.drawNode(
    appliedNodeOffset: Offset,
    s: Float = Preferences.baseScale.toPx()
) {
    drawCircle(
        color = Preferences.nodeColor1,
        center = appliedNodeOffset,
        radius = s * Preferences.beamWidth / 2
    )
    drawCircle(
        color = Preferences.nodeColor2,
        center = appliedNodeOffset,
        radius = s * (Preferences.beamWidth/2 - Preferences.edgesWidth/4),
        style = Stroke(s * Preferences.edgesWidth)
    )
}

fun DrawScope.drawPointLoad(
    appliedNodeOffset: Offset,
    loadVector: Vector,
    isReaction: Boolean = false,
    s: Float = Preferences.baseScale.toPx()
) {
    val color = if (isReaction) Preferences.reactionColor else Preferences.loadColor

    val pi = Math.PI.toFloat()

    var argument = atan(loadVector.y/loadVector.x)
    if (loadVector.x.sign < 0)
        argument += pi

    scale((loadVector.length()).absoluteValue, appliedNodeOffset) {
        rotateRad(- argument + pi/2, appliedNodeOffset) {
            // transforms the argument to the expected direction
        drawLine(
            color,
            appliedNodeOffset + Offset(0f, s / 10),
            appliedNodeOffset + Offset(0f, s),
            strokeWidth = s / 10
        )
        drawTriangle(appliedNodeOffset, s * 4 / 10f, false, color)
        }}
}

fun DrawScope.drawDistributedLoad(
    offset1: Offset, offset2: Offset,
    loadVector: Vector,
    s: Float= Preferences.baseScale.toPx()
) {
    val path = Path()
    path.moveTo(offset1.x, offset1.y)
    path.lineTo((offset1 - loadVector.toOffset()).x, (offset1 + loadVector.toOffset()).y)
    path.lineTo((offset2 - loadVector.toOffset()).x, (offset2 + loadVector.toOffset()).y)
    path.lineTo(offset2.x, offset2.y)
    drawPath(path, Preferences.loadColor, style = Stroke(s/128))

    val vectorNumber = 3
    val iHat = (offset2 - offset1)/(vectorNumber + 1f)

    for (n in 0 until vectorNumber) {
        drawPointLoad(offset1 + iHat * (n + 1f), loadVector, false, s)
    }
}

fun DrawScope.drawMoment(
    appliedNodeOffset: Offset,
    clockWise: Boolean, isReaction: Boolean = false,
    s: Float = Preferences.baseScale.toPx() * Preferences.supportSide
) {
    val color = if (isReaction) Preferences.reactionColor else Preferences.loadColor

    scale( if (clockWise) 1f else -1f, 1f, appliedNodeOffset) {
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = 210f,
            useCenter = false,
            topLeft = appliedNodeOffset - Offset(s / 2, s / 2) / 2f,
            size = Size(s, s) / 2f,
            style = Stroke(s / 20)
        )
        rotate(-90f, appliedNodeOffset) {
            drawTriangle(
                appliedNodeOffset + Offset(s / 4, - s / 10),
                s * 4 / 20f, false, color
            )
        }
    }

}

/**
 * Plot the chart of the given `Axes` on the specified locations. The plot direction, origin and
 * vertical scale is specified by the arguments.
 *
 * @param axes Axes to be plotted. The x-axis (in length units) will be distributed along the origin
 * to xEnd vector, while the y-axis will be plotted perpendicularly to said vector.
 * @param color
 * @param origin Offset where the plot will begin, the "0" point of the plot.
 * @param xEnd Offset representing where the x-axis will end.
 * @param yScale Float that the termines the relation between the plot y-axis scale and the Canvas
 * pixel count. yScale = 1 means that each unit in the y-axis will represent 1 pixel.
 */
fun DrawScope.chart(
    axes: Axes, color: Color,
    origin: Offset, xEnd: Offset,
    yScale: Float
) {
    val iHat = (xEnd - origin)
    val jHat = iHat.toVector().orthogonal().normalize().toOffset() * yScale / 2f

    val path = Path()
    path.moveTo(origin.x, origin.y)
    for (i in 1 until axes.first.size) {
        val n = axes.first[i] / axes.first.last() // normalization factor
        val xComp = iHat * n
        val p = origin + xComp + jHat * axes.second[i]
        path.lineTo(p.x, p.y)
    }
    drawPath(
        path = path,
        color = color,
        style = Stroke(Preferences.chartAbsWidth)
    )
}

object Directions {
    val C = Offset(0f, 0f)
//    val R = Offset(1f, 0f)
    val L = Offset(-1f, 0f)
    val T = Offset(0f, -1f)
    val B = Offset(0f, 1f)
}


fun DrawScope.drawLabel(
    appliedNodeOffset: Offset, string: String,
    textMeasurer: TextMeasurer,
    position: Offset,
    baseDistance: Float = Preferences.baseScale.toPx() * Preferences.supportSide) {

    drawText(
        textMeasurer,
        string,
        appliedNodeOffset +
                // reference point
                position * baseDistance -
                // string top left displacement
                Offset(0f, Preferences.textLineSize.toPx()/6) * position.y.sign -
                // text height discount
                Offset( Preferences.textSize.toPx()/12, 0f) * string.length.toFloat()/2f,
                // horizontal centering
        TextStyle(
            color = Color.Black,
            fontSize = Preferences.textSize / 6,
            textAlign = TextAlign.Center)
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
