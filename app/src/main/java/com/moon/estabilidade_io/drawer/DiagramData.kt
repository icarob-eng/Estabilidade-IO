package com.moon.estabilidade_io.drawer

import android.util.Log
import com.moon.kstability.Axes
import com.moon.kstability.Beam
import com.moon.kstability.Diagrams
import com.moon.kstability.Polynomial
import com.moon.kstability.Stabilization.isStable
import com.moon.kstability.Stabilization.stabilize
import com.moon.kstability.Structure
import com.moon.kstability.Vector
import kotlin.jvm.Throws
import kotlin.math.absoluteValue

/**
 * Class responsible for pre-calculating complex `Structure` properties, like the actual size
 * or the meanPoint. It's useful for removing this responsibility from the drawer.
 *
 * @property structure Base structure for all calculations, which is stabilized.
 * @property diagramType Selects which diagram data will be generated.
 * @property unstableStructure A deep copy of the structure that has not been stabilized. Used for
 * plotting not stabilized moment loads.
 * @property maxSize Calculates the max horizontal length of the structure.
 * @property meanPoint Finds the structures center, from its nodes.
 * @property maxLoad Finds the load (point or distributed) with the biggest length,
 * including reactions.
 * @property diagrams Generates a chart for the given `diagramType`, and its polynomials,
 * and put those in a map.
 * @property absYMax Finds the value most distant from the x axes, in the charts, and returns it.
 *
 * @throws IllegalArgumentException When the structure can't be stabilized.
 */
data class DiagramData
@Throws(IllegalArgumentException::class) constructor(
    val structure: Structure,
    val diagramType: DiagramType
) {
    val unstableStructure: Structure = structure.deepCopy()  // fixme: this is not a solution...

    init {
        if (!isStable(structure))  // fixme: this is also not a ideal solution...
            structure.stabilize()

        Log.v("Structure_Data",
            "Structure: ${structure.name}\nLoads:\n" +
                    structure.getPointLoads().map { it.toString() + "\n" }
        )
    }

    val maxSize: Float = structure.nodes.maxOf { it.pos.x } - structure.nodes.minOf { it.pos.x }

    val meanPoint: Vector = structure.getMiddlePoint()

    val maxLoad: Float = (
            structure.getPointLoads().map { it.vector } +
            structure.getDistributedLoads().map { it.vector }
            ).maxOf { it.length() }.takeIf { it != 0f } ?: 1f

    val diagrams = mutableMapOf<Beam, Pair<Axes, List<Polynomial>>>().apply {
        structure.getBeams().map {
            put(it,
                Diagrams.getDiagram(structure, it,
                    when (diagramType) {
                        DiagramType.NORMAL -> Diagrams::generateNormalFunction
                        DiagramType.SHEAR -> Diagrams::generateShearFunction
                        DiagramType.MOMENT -> Diagrams::generateMomentFunction
                        else -> return@apply
                    },
                    0.01f)
            )
        }}

    val absYMax: Float = if(diagrams.isNotEmpty()) diagrams.map {
        val axis = it.value.first.second
        return@map maxOf(axis.max(), axis.min().absoluteValue)
    }.max().takeIf { it != 0f } ?: 1f
    else 1f
}