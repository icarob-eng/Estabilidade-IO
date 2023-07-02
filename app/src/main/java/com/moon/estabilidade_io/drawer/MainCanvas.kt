package com.moon.estabilidade_io.drawer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import com.moon.kstability.Structure
import kotlin.math.sqrt


data class DrawArgs @OptIn(ExperimentalTextApi::class) constructor(
    var scaleValue: Float,
    var offsetValue: Offset,
    var rotationValue: Float,
    val textMeasurer: TextMeasurer
)

@OptIn(ExperimentalTextApi::class)
@Composable
fun MainCanvas(modifier: Modifier, structure: Structure, diagramType: DiagramType) {
    // gesture handling:
    var scaleValue by remember { mutableStateOf(2f) }
//    var rotationValue by remember { mutableStateOf(0f) }
    var offsetValue by remember { mutableStateOf(Offset.Zero) }
    val transformableState = rememberTransformableState { zoomChange, _, _ -> // offsetChange, rotationChange ->
        // update only scale gesture from dual touch input
        // (translation is updated from single touch input)
        scaleValue *= zoomChange
        scaleValue = scaleValue.coerceIn(0.75f, 25f)
//        rotationValue += rotationChange
//        offsetValue += offsetChange
    }
    val textMeasurer = rememberTextMeasurer()

    Canvas (modifier =
    modifier
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                offsetValue += dragAmount * sqrt(scaleValue)/2f
                offsetValue = Offset(
                    offsetValue.x.coerceIn(
                        -size.width * sqrt(scaleValue), size.width * sqrt(scaleValue)),
                    offsetValue.y.coerceIn(
                        -size.height * sqrt(scaleValue), size.height * sqrt(scaleValue))
                )
            } // detect translation from single touch input
        }
        .pointerInput(Unit) {
            detectTapGestures(onDoubleTap = {
                offsetValue = Offset(0f, 0f)
                scaleValue = 2f
            })
        }
        .transformable(transformableState)  // detects rotation and dual touch, etc
    ) {

        val dA= DrawArgs(scaleValue, offsetValue, 0f, textMeasurer)

        translate (offsetValue.x, offsetValue.y) { scale(scaleValue) {
            drawStructure(dA, structure, diagramType)
        }}
        drawScaleLabel(dA)  // remains in the same place
    }
}