package com.moon.estabilidade_io.charter

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalTextApi::class)
@Composable
fun MainCanvas(modifier: Modifier) {  // todo: pass structure data as argument
    // gesture handling:
    var scale by remember { mutableStateOf(1f) }
//    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val transformableState = rememberTransformableState { zoomChange, _, _ -> // offsetChange, rotationChange ->
        // update only scale gesture from dual touch input
        // (translation is updated from single touch input)
        scale *= zoomChange
//        rotation += rotationChange
//        offset += offsetChange
    }
    val textMeasurer = rememberTextMeasurer()

    Canvas (modifier =
    modifier
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                offset += dragAmount
            }
        }  // detect translation from single touch input
        .transformable(transformableState)  // detects rotation and dual touch, etc
    ) {
        scale(scale, scale) { translate (offset.x, offset.y) {
            // --- drawing stuff goes here ---
            drawTest(5f) // draw scale test
            // --- drawing stuff ends here ---
        }
            drawScale(textMeasurer)  // remains in the same place
        }

    }
}

@Preview
@Composable
fun MainCanvasPreview() {
    MainCanvas(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray))
}