@file:OptIn(ExperimentalTextApi::class)

package com.moon.estabilidade_io.charter

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp

/**
 * Base unit: 1 meter = 50 dp, which means: 3m beam would measure, by default, 150 dp
*/
fun DrawScope.drawScale(textMeasurer: TextMeasurer) {
    val baseLength = 50.dp
    val baseOffset = Offset(30.dp.toPx(), (center.x + 100f).dp.toPx())
    val endOffset = baseOffset + Offset(baseLength.toPx(), 0f)
    drawLine(Color.Black, start = baseOffset, end = endOffset, strokeWidth = 5.dp.toPx())

    drawText(
        textMeasurer,
        "1m",
        baseOffset + Offset(baseLength.toPx()/2, - 20.dp.toPx()),
//        size = Size(12.sp.toPx(), baseLength.toPx())
    )
}

fun DrawScope.drawTest(length: Float){
    // todo: remove this
    drawLine(Color.Blue,
        Offset(center.x-(length * 50.dp.toPx())/2, center.y),
        Offset(center.x+(length * 50.dp.toPx())/2, center.y)
    )
}