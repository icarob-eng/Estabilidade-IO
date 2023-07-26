package com.moon.estabilidade_io.drawer

import android.util.Log
import com.moon.kstability.Axes
import com.moon.kstability.Beam
import com.moon.kstability.Diagrams
import com.moon.kstability.Polynomial
import com.moon.kstability.Stabilization.stabilize
import com.moon.kstability.Structure
import com.moon.kstability.Vector
import kotlin.math.absoluteValue

/**
 * Class responsible for pre-calculating complex `Structure` properties, like the actual size
 * or the meanPoint.
 *
 * @property structure Base structure for all calculations.
 * @property diagramType Selects which diagram data will be generated.
 * @property maxSize Calculates the max horizontal length of the structure.
 * @property meanPoint Finds the structures center, from its nodes.
 * @property maxLoad Finds the load (point or distributed) with the biggest length
 * @property stableCopy Creates a deep copy of the structure and applies
 * `Stabilization.stabilize(it)`.
 * @property diagrams Generates a chart for the given `diagramType`, and its polynomials,
 * and put those in a map.
 * @property sMaxLoad Same as `maxLoad`, but accounting the reaction forces.
 * @property absYMax Finds the value most distant from the x axes, in the charts, and returns it.
 */
data class StructureProperties(
    val structure: Structure,
    val diagramType: DiagramType
) {
    // todo: rewrite all of this
    val maxSize: Float = structure.nodes.maxOf { it.pos.x } - structure.nodes.minOf { it.pos.x }

    val meanPoint: Vector = structure.getMiddlePoint()

    val maxLoad: Float = getMaxLoad(structure).takeIf { it != 0f } ?: 1f

    val stableCopy = try {structure.deepCopy().also { it.stabilize() } } catch (e: IllegalArgumentException) {structure}  // todo: handle exception

    val diagrams = mutableMapOf<Beam, Pair<Axes, List<Polynomial>>>().apply {
        structure.getBeams().map {
            put(it,
                Diagrams.getDiagram(stableCopy, it,
                    when (diagramType) {
                        DiagramType.NORMAL -> Diagrams::generateNormalFunction
                        DiagramType.SHEAR -> Diagrams::generateShearFunction
                        DiagramType.MOMENT -> Diagrams::generateMomentFunction
                        else -> return@apply
                    },
                    0.01f)
            )
        }}

    val sMaxLoad: Float = getMaxLoad(stableCopy).takeIf { it != 0f } ?: 1f

    val absYMax: Float = if(diagrams.isNotEmpty()) diagrams.map {
        val axis = it.value.first.second
        return@map maxOf(axis.max(), axis.min().absoluteValue)
    }.max().takeIf { it != 0f } ?: 1f
    else 1f

    init {
        Log.v("Structure_Data",
            "Structure: ${structure.name}\nLoads:\n" +
                    structure.getPointLoads().map { it.toString() + "\n" }
        )
        Log.v("Structure_Data",
            "Stable Structure: ${stableCopy.name}\nLoads:\n" +
                    stableCopy.getPointLoads().map { it.toString() + "\n" }
        )
    }

    private fun getMaxLoad(structure: Structure): Float =
        (structure.getPointLoads().map { it.vector } +
                structure.getDistributedLoads().map { it.vector })
            .maxOf { it.length() }
}