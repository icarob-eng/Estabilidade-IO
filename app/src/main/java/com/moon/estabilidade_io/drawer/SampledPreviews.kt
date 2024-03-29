package com.moon.estabilidade_io.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moon.kstability.Beam
import com.moon.kstability.Node
import com.moon.kstability.PointLoad
import com.moon.kstability.Structure
import com.moon.kstability.Support
import com.moon.kstability.Vector
import kotlin.math.sqrt

val sampleA = Structure("Basic Sample A", hashSetOf(
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
    Beam(it["A"]!!, it["C"]!!)
}

@Preview
@Composable
fun StructureSampleAPreview() {
    MainCanvas(
        Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        DiagramData(
            sampleA,
            DiagramType.MOMENT
        )
    )
}

val sampleB = Structure("Basic Sample B", hashSetOf(
    Node("C", Vector(0,0)).apply {
        Support(this, Support.Gender.SECOND, Vector.Consts.VERTICAL)
    },
    Node("B", Vector(1, 0)).apply {
        PointLoad(this, Vector(0,-10))
    },
    Node("A", Vector(2,0)).apply {
        Support(this, Support.Gender.FIRST, Vector.Consts.VERTICAL)
    }
)).also {
    Beam(it["C"]!!, it["B"]!!)
    Beam(it["B"]!!, it["A"]!!)
}

@Preview
@Composable
fun StructureSampleBPreview() {
    MainCanvas(
        Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        DiagramData(
            sampleB,
            DiagramType.SHEAR
        )
    )
}

val trussSample = Structure("Truss sample", hashSetOf(
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
    Beam(it["A"]!!, it["B"]!!)
    Beam(it["B"]!!, it["C"]!!)
    Beam(it["D"]!!, it["A"]!!)
    Beam(it["D"]!!, it["B"]!!)
    Beam(it["E"]!!, it["B"]!!)
    Beam(it["E"]!!, it["C"]!!)
    Beam(it["D"]!!, it["E"]!!)
}  // maybe this isn't the best way to instantiate a structure but whatever, this is just a sample

@Preview
@Composable
fun StructureSampleCPreview() {
    MainCanvas(
        Modifier
            .size(LocalConfiguration.current.screenWidthDp.dp / 2)
            .background(Color.LightGray),
        DiagramData(
            trussSample,
            DiagramType.NONE
        )
    )
}

val trigCircleSample = Structure("Trig Circle Sample", hashSetOf(
    Node("Center", Vector(0,0)).also {
        val a = sqrt(3f)/2 * 30
        val b = sqrt(2f)/2 * 30
        val c = sqrt(1f)/2 * 30
        PointLoad(it, Vector(50,0))
        PointLoad(it, Vector(a,c))
        PointLoad(it, Vector(b,b))
        PointLoad(it, Vector(c,a))
        PointLoad(it, Vector(0,50))
        PointLoad(it, Vector(-a,c))
        PointLoad(it, Vector(-b,b))
        PointLoad(it, Vector(-c,a))
        PointLoad(it, Vector(-40,0))
        PointLoad(it, Vector(-a,-c))
        PointLoad(it, Vector(-b,-b))
        PointLoad(it, Vector(-c,-a))
        PointLoad(it, Vector(0,-40))
        PointLoad(it, Vector(a,-c))
        PointLoad(it, Vector(b,-b))
        PointLoad(it, Vector(c,-a))
    },
    Node("A", Vector(1,0))
)
).also { Beam(it["Center"]!!, it["A"]!!) }

@Preview
@Composable
fun TrigCircleSamplePreview() {
    MainCanvas(Modifier.fillMaxSize().background(Color.LightGray), DiagramData(trigCircleSample, DiagramType.REACTIONS))
}