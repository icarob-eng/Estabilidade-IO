package com.moon.estabilidade_io.drawer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.moon.kstability.Axis
import com.moon.kstability.Diagrams
import com.moon.kstability.Node
import com.moon.kstability.Stabilization
import com.moon.kstability.Structure
import com.moon.kstability.Support
import com.moon.kstability.Vector
import kotlin.math.absoluteValue

enum class DiagramType {
    /**
     * Tells `MainCanvas` what to draw along with the given structure.
     * @property NONE Draw only the structure
     * @property LOADS Draw the structure and the applied loads
     * @property REACTIONS Draw the structure, applied loads, and reaction forces
     * @property NORMAL Draw the structure, reaction forces and Normal Stress Diagram
     * @property SHEAR Draw the structure, reaction forces and Shear Stress Diagram
     * @property MOMENT Draw the structure, reaction forces and Bending Moment Diagram
     */
    NONE, LOADS, REACTIONS, NORMAL, SHEAR, MOMENT
}

fun DrawScope.drawStructure(
    drawArgs: DrawArgs,
    structure: Structure,
    diagramType: DiagramType,
    nodeLabels: Boolean = false,
    loadLabels: Boolean = true
) {
    drawTest(1f) // draw scale test
    if (structure.nodes.size == 0) return
    val s = Preferences.baseScale.toPx()
    val b = Basis(structure, s, center)
    val sCopy = structure.getRotatedCopy(0f) // this actually creates a deep copy
    Stabilization.stabilize(sCopy)

    /*
    # Draw order:
    1. Supports
    2. Beams
    3. Nodes
    4. Loads
    5. Charts
     */
    structure.getSupports().map {
        when (it.gender) {
            // todo: rotate support
            Support.Gender.FIRST -> if (Preferences.useRollerB) drawRollerB(it.node.toOffset(b))
            else drawRoller(it.node.toOffset(b))

            Support.Gender.SECOND -> drawHinge(it.node.toOffset(b))
            Support.Gender.THIRD -> drawFixed(it.node.toOffset(b))
        }
    }
    // todo: check if order matters
    structure.getBeams().map {
        drawBeam(it.node1.toOffset(b), it.node2.toOffset(b))
    }
    structure.nodes.map {
        drawNode(it.toOffset(b))
        if (nodeLabels && it.beams.isNotEmpty())
            drawLabel(it.toOffset(b), it.name, drawArgs, Directions.T)
    }

    if (diagramType == DiagramType.NONE) return
    // here we apply the methods to a copy, so we can compare if the force is or not a reaction
    // todo: scaling
    val ls = s / 32
    sCopy.getPointLoads().map {
        val isReaction = it !in structure.getPointLoads()
        if (isReaction && diagramType != DiagramType.LOADS || !isReaction) {
            drawPointLoad(it.node.toOffset(b), it.vector, isReaction, ls)
            if (loadLabels)
                drawLabel(it.node.toOffset(b), it.vector.length().u("kN"), drawArgs, Directions.T)
        }
    }
    sCopy.nodes.map {
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
                drawLabel(it.toOffset(b), resultMoment.u("kNm"), drawArgs, Directions.T)
        }
    }
    // todo: check if order matters
    structure.getDistributedLoads().map {
        drawDistributedLoad(it.node1.toOffset(b), it.node2.toOffset(b), it.vector, ls)
        if (loadLabels)
            drawLabel(
                (it.node1.toOffset(b) + it.node2.toOffset(b))/2f,
                it.vector.length().u("kN/M"),
                drawArgs,
                it.vector.normalize().toOffset()
            )
    }

    val diagramFun = when (diagramType) {
        DiagramType.NORMAL -> Diagrams::generateNormalFunction
        DiagramType.SHEAR -> Diagrams::generateShearFunction
        DiagramType.MOMENT -> Diagrams::generateMomentFunction
        else -> return
    }
    // todo: plot diagrams
}

/**
 * Return scale that make the axis be limited between -baseScale and baseScale.
 */
fun getYScale(baseScale: Float, axis: Axis): Float {
    val absMax = if (axis.max() >= axis.min().absoluteValue) axis.max() else axis.min().absoluteValue
    return baseScale / if (absMax != 0f) absMax else 1f
}

fun Node.toOffset(basis: Basis) = (Offset(pos.x, -pos.y) * basis.scale + basis.origin)
fun Vector.toOffset() = Offset(x, -y)
fun Offset.toVector() = Vector(x, y)

fun Number.u(u: String) = "$this $u"

class Basis(structure: Structure, baseLength: Float, center: Offset) {
    val scale: Float
    val origin: Offset
    init {
        scale = baseLength
        origin = center -
            (structure.nodes.map{ it.pos }.reduce {acc: Vector, next: Vector ->  acc + next} *scale/
                    structure.nodes.size).toOffset() // find mean point
    }
}