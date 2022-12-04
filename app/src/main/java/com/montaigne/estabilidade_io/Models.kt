package com.montaigne.estabilidade_io

import java.util.InvalidPropertiesFormatException

data class Vector(val x: Float, val y: Float) {
    operator fun plus(other: Vector) = Vector(this.x + other.x, this.y + other.y)

    operator fun minus(other: Vector) = Vector(this.x - other.x, this.y - other.y)
}


data class Knot(val name: String, val pos: Vector)

data class Support(val knot: Knot, val gender: Int, val direction: Vector)

data class Bar(val knots: List<Knot>) {
    init {if (knots.size != 2)
        throw InvalidPropertiesFormatException("Uma barra deve possuir dois nós")}
}


data class Load(val knot: Knot, val vector: Vector)

// forças normais ao comprimento da carga
data class DistributedLoad(val knots: List<Knot>, val module: Int) {
    init {if (knots.size != 2)
            throw InvalidPropertiesFormatException("Uma barra deve possuir dois nós")}
}


data class Structure(val name: String, val knot: List<Knot>, val support: Support, val bars: List<Bar>,
                     val loads: List<Load>, val distributedLoads: List<DistributedLoad>)

