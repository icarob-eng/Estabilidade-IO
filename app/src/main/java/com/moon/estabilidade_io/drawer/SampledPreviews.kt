package com.moon.estabilidade_io.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.moon.kstability.Beam
import com.moon.kstability.Node
import com.moon.kstability.PointLoad
import com.moon.kstability.Structure
import com.moon.kstability.Support
import com.moon.kstability.Vector

val sampleA = Structure("Basic Sample A", mutableListOf(
    Node("A", Vector(0,0)).apply {
        Support(this, Support.Gender.SECOND, Vector.Consts.VERTICAL)
    },
    Node("B", Vector(1, 0)).apply {
        PointLoad(this, Vector(0,-10))
    },
    Node("C", Vector(2,0)).apply {
        Support(this, Support.Gender.FIRST, Vector.Consts.VERTICAL)
    }
)).also {
    Beam(it.nodes.first(), it.nodes.last())
}

@Preview
@Composable
fun StructureSampleAPreview() {
    MainCanvas(
        Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        sampleA,
        DiagramType.REACTIONS
    )
}

val trussSample = Structure("Truss sample", mutableListOf(
    Node("A", Vector(0,0)).apply {
        Support(this, Support.Gender.SECOND, Vector.Consts.VERTICAL)
    },
    Node("B", Vector(1, 0)),
    Node("C", Vector(2,0)).apply {
        Support(this, Support.Gender.FIRST, Vector.Consts.VERTICAL)
    },
    Node("D", Vector(0.5f, 0.5f)).apply {
        PointLoad(this, Vector(0,-10))
    },
    Node("E", Vector(1.5f, 0.5f)).apply {
        PointLoad(this, Vector(0,-10))
    }
)).also {
    Beam(it.nodes.find { node ->  node.name == "A" }!!, it.nodes.find { node -> node.name == "B" }!!)
    Beam(it.nodes.find { node ->  node.name == "B" }!!, it.nodes.find { node ->  node.name == "C" }!!)
    Beam(it.nodes.find { node ->  node.name == "D" }!!, it.nodes.find { node ->  node.name == "A" }!!)
    Beam(it.nodes.find { node ->  node.name == "D" }!!, it.nodes.find { node ->  node.name == "B" }!!)
    Beam(it.nodes.find { node ->  node.name == "E" }!!, it.nodes.find { node ->  node.name == "B" }!!)
    Beam(it.nodes.find { node ->  node.name == "E" }!!, it.nodes.find { node ->  node.name == "C" }!!)
    Beam(it.nodes.find { node ->  node.name == "D" }!!, it.nodes.find { node ->  node.name == "E" }!!)
}  // maybe this isn't the best way to instantiate a structure but whatever, this is just a sample

@Preview
@Composable
fun StructureSampleBPreview() {
    MainCanvas(
        Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        trussSample,
        DiagramType.NONE
    )
}