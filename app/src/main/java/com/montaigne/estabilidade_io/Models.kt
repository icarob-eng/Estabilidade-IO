package com.montaigne.estabilidade_io

import java.util.InvalidPropertiesFormatException
import kotlin.math.sqrt

object Consts {
    @JvmField
    val HORIZONTAL = Vector(1F, 0F)
    @JvmField
    val VERTICAL = Vector(0F, 1F)
}

data class Vector(val x: Float, val y: Float) {
    operator fun plus(other: Vector) = Vector(this.x + other.x, this.y + other.y)
    operator fun minus(other: Vector) = Vector(this.x - other.x, this.y - other.y)
    operator fun times(other: Float) = Vector(x * other, y * other)
    operator fun div(other: Float) = Vector(x / other, y / other)

    fun modulus() = sqrt(this*this)  // = sqrt(x**2 + y**2)

    fun normalize() = this/modulus()

    operator fun times(other: Vector) = this.x * other.x + this.y * other.y  // dot product

    fun crossModule(other: Vector) = this.x * other.x - this.y * other.y

    fun getNormal() = Vector(-y, x).normalize()
}


data class Knot(val name: String, val pos: Vector)

data class Support(val knot: Knot, val gender: Int, val direction: Vector)

data class Bar(val knots: List<Knot>) {
    init {if (knots.size != 2)
        throw InvalidPropertiesFormatException("Uma barra deve possuir dois nós")}
}


data class Load(val knot: Knot, val vector: Vector)

// forças normais ao comprimento da carga
data class DistributedLoad(val knot1: Knot, val knot2: Knot, val norm: Float) {
    fun getEqLoad() = Load(
        Knot(knot1.name + knot2.name, (knot1.pos + knot2.pos) / 2F),  // midpoint
        (knot2.pos - knot1.pos).getNormal() * (knot2.pos - knot1.pos).modulus() * norm
        // bisector vector times modulus of the entire distributed force
    )
}


data class Structure(val name: String, val knot: List<Knot>, val support: Support, val bars: List<Bar>,
                     val loads: List<Load>, val distributedLoads: List<DistributedLoad>)

