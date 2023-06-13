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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun MainCanvas(modifier: Modifier) {  // todo: pass structure data as argument
    // gesture handling:
    var scale by remember { mutableStateOf(1f) }
//    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        // update only scale from dual touch input (translation updated from single touch input)
        scale *= zoomChange
//        rotation += rotationChange
//        offset += offsetChange
    }

    Canvas (modifier =
        modifier.graphicsLayer (
            scaleX = scale,
            scaleY = scale,
            rotationZ = 0f, // rotation,  // rotation removed
            translationX = offset.x,
            translationY = offset.y
        )  // **applies** the changes to the canvas
            .pointerInput(Unit){
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offset += dragAmount
                }
            }  // allows translation with single touch
            .transformable(state)  // allows rotation and dual touch, etc
    ) {
        scale(10f, 10f) {
            myCircle()
        }
    }
}

// todo: pass all drawing classes to a different module.
// base unit: 1 meter = 10 dp, which means: 3m beam would measure, by default, 30 dp
fun DrawScope.myCircle() {
    val rectSize = 3.dp.toPx()
    drawRect(Color.Magenta,
        Offset(center.x - rectSize/2, center.y - rectSize/2),
        Size(rectSize,rectSize)
    )
}


@Preview
@Composable
fun MainCanvasPreview() {
    MainCanvas(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray))
}