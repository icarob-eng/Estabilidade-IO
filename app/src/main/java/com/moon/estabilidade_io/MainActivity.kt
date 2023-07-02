package com.moon.estabilidade_io

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.moon.estabilidade_io.drawer.DiagramType
import com.moon.estabilidade_io.drawer.MainCanvas
import com.moon.estabilidade_io.drawer.trussSample
import com.moon.estabilidade_io.ui.theme.EstabilidadeIOTheme
import com.moon.kstability.Structure

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sample = trussSample.copy(name="MainActivity Sample")

        setContent {
            EstabilidadeIOTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainCanvas(
                        Modifier
                            .fillMaxSize()
                            .background(Color.LightGray),
                        // generate structure and pass it here
                        sample,
                        // select the desired DiagramType @see StructureDrawer.DiagramType
                        DiagramType.NONE
                    )
                }
            }
        }
    }
}
