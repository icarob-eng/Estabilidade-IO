package com.montaigne.estabilidade_io

class MathUtils {
    companion object {
        fun isStable(structure: Structure): Boolean {
            var genders = 0
            for (support in structure.getSupports())
                genders += support.gender.reactions
            return genders == 3
        }

        private fun getMomentum(structure: Structure, axis: Vector): Float {
            var momentum = 0F
            for (load in structure.getEqvLoads())
                momentum += (load.knot.pos - axis).crossModule(load.vector)
                // T = d x F; d = d1 - d_origin
            return momentum
        }

        fun getResultantForce(structure: Structure): Vector {
            var result = Vector(0F, 0F)
            for (load in structure.getEqvLoads())
                result += load.vector
            return result
        }

        fun determineReactions(structure: Structure) {
            val momentum = getMomentum(structure, structure.knots[0].pos)  // todo: determine reference knot
            val resultant = getResultantForce(structure)
            // from here we need to know hot to balance the resultant
        }
    }
}