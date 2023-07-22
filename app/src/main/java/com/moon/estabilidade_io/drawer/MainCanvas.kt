package com.moon.estabilidade_io.drawer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.rememberTextMeasurer
import com.moon.kstability.Structure
import kotlin.math.sqrt


/**
 * Draw a given `Structure`, with some parameters. Also, pre-calculates some properties of the
 * structure and is responsible for gesture handling.
 *
 * @param modifier Regular composable modifier.
 * @param structure The structure that will be drawn.
 * @param diagramType Determines what will be drawn. See enum.
 *
 * @see DiagramType
 */
@OptIn(ExperimentalTextApi::class)
@Composable
fun MainCanvas(modifier: Modifier, structure: Structure, diagramType: DiagramType) {
    if (structure.nodes.size == 0) {
        Box(modifier)
        return
    }
    val properties = StructureProperties(structure, diagramType)
    val textMeasurer = rememberTextMeasurer()

    var reFrameScale = 2f  // arbitrary default
    var undefinedFrame = true  // tells if the framedScale still default
    // gesture handling:
    var scaleValue by remember { mutableStateOf(reFrameScale) }
//    var rotationValue by remember { mutableStateOf(0f) }
    var offsetValue by remember { mutableStateOf(Offset.Zero) }

    val transformableState = rememberTransformableState { zoomChange, offsetChange, _ -> // rotationChange ->
        scaleValue *= zoomChange
        scaleValue = scaleValue.coerceIn(0.75f, 25f)
//        rotationValue += rotationChange
        offsetValue += offsetChange / scaleValue
    }

    Canvas (modifier =
    modifier
        .pointerInput(Unit) {
            detectTapGestures(onDoubleTap = {
                // re-frames the structure
                offsetValue = Offset(0f, 0f)
                scaleValue = reFrameScale
            })
        }
        .transformable(transformableState)  // detects rotation and dual touch, etc
    ) {
        reFrameScale = size.width / (properties.maxSize * Preferences.baseScale.toPx()) * 3/5
        if (undefinedFrame) {
            scaleValue = reFrameScale
            undefinedFrame = false
        }

        offsetValue = Offset(
            offsetValue.x.coerceIn(
                -size.width / sqrt(2 * scaleValue), size.width / sqrt(2 * scaleValue)
            ),
            offsetValue.y.coerceIn(
                -size.height / sqrt(2 * scaleValue), size.height / sqrt(2 * scaleValue)
            )
        )

        scale(scaleValue) { translate (offsetValue.x, offsetValue.y) {
            drawStructure(properties, diagramType, textMeasurer)
        }}
        drawScaleLabel(textMeasurer, scaleValue)  // remains in the same place
    }
}