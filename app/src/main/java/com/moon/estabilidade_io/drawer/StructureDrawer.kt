package com.moon.estabilidade_io.drawer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import com.moon.kstability.Node
import com.moon.kstability.Support
import com.moon.kstability.Vector

enum class DiagramType {
    /**
     * Tells `MainCanvas` what to draw along with the given structure.
     * @property NONE Draw only the structure
     * @property LOADS Draw the structure and the applied loads
     * @property REACTIONS Draw the structure, applied loads, and reaction forces
     * @property NORMAL Draw the structure, reaction forces and Normal Stress Diagram
     * @property SHEAR Draw the structure, reaction forces and Shear Stress Diagram
     * @property MOMENT Draw the structure, reaction forces and Bending Moment Diagram
     *
     * @see MainCanvas
     * @see drawStructure
     */
    NONE, LOADS, REACTIONS, NORMAL, SHEAR, MOMENT
}

/**
 * Draws an entire `Structure`, with preloaded `StructureProperties`.
 *
 * @param sProperties Where most arguments are passed, including the structure and its properties.
 * @param diagramType Selects what will be drawn. See enum.
 * @param textMeasurer Necessary for printing labels.
 * @param nodeLabels Determines if each node name will be printed.
 * @param loadLabels Determines if load values and polynomials will be printed.
 *
 * @see DiagramType
 */
@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawStructure(
    sProperties: StructureProperties,
    diagramType: DiagramType,
    textMeasurer: TextMeasurer,
    nodeLabels: Boolean = false,
    loadLabels: Boolean = true
) {
    drawTest(1f) // draw scale test
    val structure = sProperties.structure
    if (structure.nodes.size == 0) return
    val s = Preferences.baseScale.toPx()
    val b = Basis(sProperties.meanPoint.toOffset(), s, center)

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
        when (it.gender) {
            // todo: rotate support
            Support.Gender.FIRST -> if (Preferences.useRollerB) drawRollerB(it.node.toOffset(b))
            else drawRoller(it.node.toOffset(b))

            Support.Gender.SECOND -> drawHinge(it.node.toOffset(b))
            Support.Gender.THIRD -> drawFixed(it.node.toOffset(b))
        }
    }
    // --- beams ---
    // todo: check if order matters
    structure.getBeams().map {
        drawBeam(it.node1.toOffset(b), it.node2.toOffset(b))
    }
    // --- nodes ---
    structure.nodes.map {
        drawNode(it.toOffset(b))
        if (nodeLabels && it.beams.isNotEmpty())
            drawLabel(it.toOffset(b), it.name, textMeasurer, Directions.T)
    }

    if (diagramType == DiagramType.NONE) return
    // --- loads and reactions ---

    // here we apply the methods to a copy, so we can compare if the force is or not a reaction
    val loadScale = Preferences.supportSide * s /
            if(diagramType != DiagramType.LOADS) sProperties.sMaxLoad else sProperties.maxLoad

    sProperties.stableCopy.getPointLoads().map {
        val isReaction = it !in structure.getPointLoads()
        if (isReaction && diagramType != DiagramType.LOADS || !isReaction) {
            drawPointLoad(it.node.toOffset(b), it.vector, isReaction, loadScale)
            if (loadLabels)
                drawLabel(it.node.toOffset(b), it.vector.length().u("kN"), textMeasurer, Directions.T)
        }
    }
    sProperties.stableCopy.nodes.map {
        val eqvNode = structure.nodes.find { node -> it.name == node.name }!!
        var resultMoment = eqvNode.momentum
        var isReaction = false

        if (diagramType != DiagramType.LOADS) {
            resultMoment = it.momentum
            isReaction = eqvNode.momentum != it.momentum
        }

        if (resultMoment != 0f) {
            drawMoment(it.toOffset(b), resultMoment < 0f, isReaction)
            if (loadLabels)
                drawLabel(it.toOffset(b), resultMoment.u("kNm"), textMeasurer, Directions.T)
        }
    }
    // todo: check if order matters
    structure.getDistributedLoads().map {
        drawDistributedLoad(it.node1.toOffset(b), it.node2.toOffset(b), it.vector, loadScale)
        if (loadLabels)
            drawLabel(
                (it.node1.toOffset(b) + it.node2.toOffset(b))/2f,
                it.vector.length().u("kN/M"),
                textMeasurer,
                it.vector.normalize().toOffset()
            )
    }

    // --- charts ---
    val color = when (diagramType) {
        DiagramType.NORMAL -> Preferences.normalDiagramColor
        DiagramType.SHEAR -> Preferences.shearDiagramColor
        DiagramType.MOMENT -> Preferences.momentDiagramColor
        else -> return
    }
    val yScale = s / sProperties.absYMax

    sProperties.diagrams.map {entry ->
        chart(
            entry.value.first,
            color,
            entry.key.node1.toOffset(b),
            entry.key.node2.toOffset(b),
            yScale
        )
        if (loadLabels)
            drawLabel(
                (entry.key.node1.toOffset(b) + entry.key.node2.toOffset(b))/2f,
                entry.value.second.first().toString(), // todo: draw each label
                textMeasurer,
                Directions.T
            )
    }
}

fun Node.toOffset(basis: Basis) = (Offset(pos.x, -pos.y) * basis.scale + basis.origin)
fun Vector.toOffset() = Offset(x, -y)
fun Offset.toVector() = Vector(x, y)

fun Number.u(u: String) = "$this $u"

class Basis(meanPoint: Offset, baseLength: Float, center: Offset) {
    val scale: Float = baseLength
    val origin: Offset = center - meanPoint * scale
}