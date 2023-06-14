@file:OptIn(ExperimentalTextApi::class)

package com.moon.estabilidade_io.drawer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp


fun DrawScope.drawStructure(drawArgs: DrawArgs) {  // todo: pass structure data as argument
    drawTest(1f) // draw scale test
    drawHinge1(drawArgs, center)
}


fun DrawScope.drawScaleLabel(drawArgs: DrawArgs) {
    val baseOffset = Offset(
        Preferences.scaleRightMargin.toPx(),
        (size.height - Preferences.scaleBottomMargin.toPx())
    )
    val endOffset = baseOffset +
            Offset(Preferences.baseScale.toPx() * drawArgs.scaleValue, 0f)

    drawLine(color = Color.Black, start = baseOffset, end = endOffset, strokeWidth = 5.dp.toPx())

    drawText(
        drawArgs.textMeasurer,
        "1m",
        baseOffset + Offset(0f, - Preferences.textLineSize.toPx()),
        TextStyle(color = Color.Black, fontSize = Preferences.textSize)
    )
}

fun DrawScope.drawHinge1(drawArgs: DrawArgs, appliedNodeOffset: Offset) {
    scale(Preferences.supportSide, Preferences.supportSide) {
//        drawRect(Color.Black,
//            appliedNodeOffset + Offset(- Preferences.baseScale.toPx()/2, 0f),
//            Size(Preferences.baseScale.toPx(), Preferences.baseScale.toPx()),
//            style = Stroke(5f)
//        )  // todo: remove this reference debug box
        drawCircle(
            color = Preferences.supportColor1,
            radius = Preferences.baseScale.toPx()/2 * 4/5,
            center = appliedNodeOffset + Offset(0f, Preferences.baseScale.toPx()/2 * 4/5)
        )
        drawCenteredHatches(drawArgs, appliedNodeOffset + Offset(0f,Preferences.baseScale.toPx() * 4/5))
    }
}

private fun DrawScope.drawCenteredHatches(drawArgs: DrawArgs, middlePoint: Offset) {
    val endOffset = middlePoint + Offset(Preferences.baseScale.toPx()/2, 0f)
    drawLine(
        color = Preferences.supportColor2,
        start = middlePoint + Offset(-Preferences.baseScale.toPx()/2, 0f),
        end = endOffset,
        strokeWidth = Preferences.baseScale.toPx()/25
    )

    val h = Preferences.baseScale.toPx()/8
    for (n in 0 downTo -5) {
        drawLine(
            color = Preferences.supportColor2,
            start = endOffset + Offset(h * n, 0f),
            end = endOffset + Offset(h * (n - 1), h),
            strokeWidth = Preferences.baseScale.toPx()/25
        )
    }
}

fun DrawScope.drawTest(length: Float){
    // todo: remove this
    drawLine(Color.Blue,
        Offset(center.x-(length * Preferences.baseScale.toPx())/2, center.y),
        Offset(center.x+(length * Preferences.baseScale.toPx())/2, center.y)
    )
}