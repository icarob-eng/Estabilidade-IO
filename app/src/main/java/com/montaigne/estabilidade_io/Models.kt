package com.montaigne.estabilidade_io

import kotlin.math.sqrt

object Consts {
    @JvmField
    val HORIZONTAL = Vector(1F, 0F)
    @JvmField
    val VERTICAL = Vector(0F, 1F)
}

enum class SupportGender (val reactions: Int){
    FIRST(1), SECOND(2), THIRD(3)
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


data class Knot(val name: String, val pos: Vector, val structure: Structure?) {
    var support: Support? = null  // only one support by knot
    var momentum = 0F
    val bars: MutableList<Bar> = mutableListOf()
    val loads: MutableList<Load> = mutableListOf()
    val distributedLoads: MutableList<DistributedLoad> = mutableListOf()

    init {
        structure?.knots?.add(this)  // null for temporary knots
    }
}

data class Support(val knot: Knot, val gender: SupportGender, val dir: Vector) {
    val direction: Vector = dir.normalize()

    init { knot.support = this }

    fun getReactions(): Triple<Vector, Vector?, Float?> {
        return when (gender) {
            SupportGender.FIRST -> Triple(direction, null, null)
            SupportGender.SECOND -> Triple(direction, direction.getNormal(), null)
            SupportGender.THIRD -> Triple(direction, direction.getNormal(), 0F)
        }
    }
}

data class Bar(val knot1: Knot, val knot2: Knot) {
    init {
        knot1.bars.add(this)
        knot2.bars.add(this)
    }
}


data class Load(val knot: Knot, val vector: Vector) {
    init { knot.loads.add(this) }
}

// forces are assumed to be normal to the load length
data class DistributedLoad(val knot1: Knot, val knot2: Knot, val norm: Float) {
    init {
        knot1.distributedLoads.add(this)
        knot2.distributedLoads.add(this)
    }

    fun getEqvLoad() = Load(
        Knot(knot1.name + knot2.name,
            (knot1.pos + knot2.pos) / 2F,  // midpoint
            null),
        (knot2.pos - knot1.pos).getNormal() * (knot2.pos - knot1.pos).modulus() * norm
        // bisector vector times modulus of the entire distributed force
    )
}


data class Structure(val name: String, val knots: MutableList<Knot> = mutableListOf()) {
    fun getSupports() = knots.mapNotNull { it.support }
    fun getBars() = knots.flatMap { it.bars }
    fun getLoads() = knots.flatMap { it.loads }
    fun getDistributedLoads() = knots.flatMap { it.distributedLoads }
    fun getEqvLoads() = getLoads() + getDistributedLoads().map { it.getEqvLoad() }
}

