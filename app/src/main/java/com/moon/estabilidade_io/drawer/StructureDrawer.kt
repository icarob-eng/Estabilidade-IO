package com.moon.estabilidade_io.drawer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotateRad
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import com.moon.kstability.Node
import com.moon.kstability.Support
import com.moon.kstability.Vector
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.sign

/**
 * Draws an entire `Structure`, with preloaded `DiagramData`.
 *
 * @param data Contains all data to be drawn, including the structure and its properties.
 * @param textMeasurer Necessary for printing labels.
 * @param nodeLabels Determines if each node name will be printed.
 * @param loadLabels Determines if load values and polynomials will be printed.
 *
 * @see DiagramType
 * @see DiagramData
 */
@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawStructure(
    data: DiagramData,
    textMeasurer: TextMeasurer,
    nodeLabels: Boolean = false,
    loadLabels: Boolean = true
) {
//    drawTest(1f) // draw scale test
    val structure = data.structure
    if (structure.nodes.size == 0) return
    val b = Basis(data.meanPoint.toOffset(), Preferences.baseScale.toPx(), center)
    // independent of drawScale
    val s = Preferences.baseScale.toPx() * data.maxSize / 2f
    val ss = s * Preferences.supportSide

    /*
    # Draw order:
    1. Supports
    2. Beams
    3. Nodes
    4. Loads
    5. Charts
     */
    // --- supports ---
    structure.getSupports().map {
        rotateRad(it.direction.rotationArg(), it.node.toOffset(b)) {
            when (it.gender) {
                Support.Gender.FIRST -> if (Preferences.useRollerB) drawRollerB(it.node.toOffset(b), ss)
                else drawRoller(it.node.toOffset(b), ss)

                Support.Gender.SECOND -> drawHinge(it.node.toOffset(b), ss)
                Support.Gender.THIRD -> drawFixed(it.node.toOffset(b), ss)
            }}
    }
    // --- beams ---

    structure.getBeams().map {
        drawBeam(it.node1.toOffset(b), it.node2.toOffset(b), s)
    }
    // --- nodes ---
    structure.nodes.map {
        if (it.beams.isEmpty()) return@map
        drawNode(it.toOffset(b), s)
        if (nodeLabels)
            drawLabel(it.toOffset(b), it.name, textMeasurer, Directions.L, ss)
    }

    if (data.diagramType == DiagramType.NONE) return
    // --- loads and reactions ---

    // here we apply the methods to a copy, so we can compare if the force is or not a reaction
    val loadScale = Preferences.supportSide * s / data.maxLoad

    structure.getPointLoads().map {
        if (it.isReaction && data.diagramType != DiagramType.LOADS || !it.isReaction) {
            drawPointLoad(it.node.toOffset(b), it.vector, it.isReaction, loadScale)
            if (loadLabels)
                drawLabel(
                    it.node.toOffset(b), it.vector.length().u("kN"), textMeasurer, Directions.T, ss
                )
        }
    }

    structure.nodes.map {
        val moment = it.momentum - it.reactionMomentum
        if (moment != 0f) {
            drawMoment(
                appliedNodeOffset = it.toOffset(b),
                clockWise = moment < 0f,
                isReaction = it.reactionMomentum != 0f,
                s = ss
            )
            if (loadLabels)
                drawLabel(it.toOffset(b), moment.u("kNm"), textMeasurer, Directions.C, ss)
        }
    }

    structure.getDistributedLoads().map {
        drawDistributedLoad(it.node1.toOffset(b), it.node2.toOffset(b), it.vector, loadScale)
        if (loadLabels)
            drawLabel(
                (it.node1.toOffset(b) + it.node2.toOffset(b))/2f,
                it.vector.length().u("kN/M"),
                textMeasurer,
                it.vector.normalize().toOffset(),
                ss
            )
    }

    // --- charts ---
    val color = when (data.diagramType) {
        DiagramType.NORMAL -> Preferences.normalDiagramColor
        DiagramType.SHEAR -> Preferences.shearDiagramColor
        DiagramType.MOMENT -> Preferences.momentDiagramColor
        else -> return
    }
    val yScale = s / data.absYMax

    data.diagrams.map { entry ->
        chart(
            entry.value.first,
            color,
            entry.key.node1.toOffset(b),
            entry.key.node2.toOffset(b),
            yScale
        )
        if (loadLabels)
            structure.nodes.sortedBy { it.pos.x }.mapIndexed { n, node ->
                drawLabel(
                    node.toOffset(b),
                    entry.value.second[n].toString(),
                    textMeasurer,
                    Directions.T * 1.5f + Directions.R,
                    ss
                )
            }
    }
}

fun Node.toOffset(basis: Basis) = (Offset(pos.x, -pos.y) * basis.scale + basis.origin)
fun Vector.toOffset() = Offset(x, -y)
fun Offset.toVector() = Vector(x, y)

fun Vector.rotationArg() =  // damn this -0f!!!!
    - (atan(inclination()) +
            (if (x.sign < 0f) PI.toFloat() else 0f)) +
            PI.toFloat()/2

fun Number.u(u: String) = "$this $u"

class Basis(meanPoint: Offset, baseLength: Float, center: Offset) {
    val scale: Float = baseLength
    val origin: Offset = center - meanPoint * scale
}