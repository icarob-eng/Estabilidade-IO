package com.moon.estabilidade_io.drawer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.moon.kstability.Axis
import com.moon.kstability.Diagrams
import com.moon.kstability.Node
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
    val s = Preferences.baseScale.toPx()
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
            Support.Gender.FIRST -> if (Preferences.useRollerB) drawRollerB(it.node.toOffset())
            else drawRoller(it.node.toOffset())

            Support.Gender.SECOND -> drawHinge(it.node.toOffset())
            Support.Gender.THIRD -> drawFixed(it.node.toOffset())
        }
    }
    // todo: check if order matters
    structure.getBeams().map {
        drawBeam(it.node1.toOffset(), it.node2.toOffset())
    }
    structure.nodes.map {
        drawNode(it.toOffset())
        if (nodeLabels) drawLabel(it.toOffset(), it.name, drawArgs, Directions.T)
    }

    if (diagramType == DiagramType.NONE) return

    // todo: scaling
    structure.getPointLoads().map {
        drawPointLoad(it.node.toOffset(), it.vector)
        if (loadLabels)
            drawLabel(it.node.toOffset(), it.vector.length().u("kN"), drawArgs, Directions.T)
    }
    // todo: check if order matters
    structure.getDistributedLoads().map {
        drawDistributedLoad(it.node1.toOffset(), it.node2.toOffset(), it.vector)
        if (loadLabels)
            drawLabel(
                (it.node1.toOffset() + it.node2.toOffset())/2f,
                it.vector.length().u("kN/M"),
                drawArgs,
                it.vector.normalize().toOffset()
            )
    }
    structure.nodes.map {
        if (it.momentum != 0f) {
            drawMoment(it.toOffset(), it.momentum < 0f)
            if (loadLabels)
                drawLabel(it.toOffset(), it.momentum.u("kNm"), drawArgs, Directions.T)
        }
    }

    if (diagramType == DiagramType.LOADS) return
    // todo: reaction forces
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

fun Node.toOffset() = Offset(pos.x, pos.y)
fun Vector.toOffset() = Offset(x, y)
fun Offset.toVector() = Vector(x, y)

fun Number.u(u: String) = "$this $u"