@file:OptIn(ExperimentalTextApi::class)

package com.moon.estabilidade_io.drawer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp


fun DrawScope.drawStructure(drawArgs: DrawArgs) {
    drawTest(1f) // draw scale test
}


fun DrawScope.drawScaleLabel(drawArgs: DrawArgs) {
    val baseOffset = Offset(
        Preferences.scaleRightMargin.toPx(),
        (size.height - Preferences.scaleBottomMargin.toPx())
    )
    val endOffset = baseOffset +
            Offset(Preferences.baseScale.toPx() * drawArgs.scaleValue, 0f)

    drawLine(Color.Black, start = baseOffset, end = endOffset, strokeWidth = 5.dp.toPx())

    drawText(
        drawArgs.textMeasurer,
        "1m",
        baseOffset + Offset(0f, - Preferences.textLineSize.toPx()),
        TextStyle(color=Color.Black, fontSize = Preferences.textSize)
    )
}

fun DrawScope.drawTest(length: Float){
    // todo: remove this
    drawLine(Color.Blue,
        Offset(center.x-(length * Preferences.baseScale.toPx())/2, center.y),
        Offset(center.x+(length * Preferences.baseScale.toPx())/2, center.y)
    )
}