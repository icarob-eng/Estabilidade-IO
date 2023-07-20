@file:OptIn(ExperimentalMaterial3Api::class)

package com.moon.estabilidade_io

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.moon.estabilidade_io.drawer.DiagramType
import com.moon.estabilidade_io.drawer.MainCanvas
import com.moon.estabilidade_io.drawer.sampleA
import com.moon.estabilidade_io.drawer.sampleB
import com.moon.estabilidade_io.ui.components.BottomAppBarSelector
import com.moon.estabilidade_io.ui.components.BottomSheetContent
import com.moon.estabilidade_io.ui.theme.EstabilidadeIOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var structureState by remember {
                mutableStateOf(sampleB.copy(name = "MainActivity Sample B"))
            }
            var diagramTypeState by remember { mutableStateOf(DiagramType.NONE) }
            var structureDataState by remember { mutableStateOf("") }

            val bgColor = Color.LightGray

            val mainCanvasModifier = Modifier
                .size(LocalConfiguration.current.smallestScreenWidthDp.dp)
                .background(bgColor)

            EstabilidadeIOTheme {
                Scaffold(
                    modifier = Modifier.background(bgColor),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(text = structureState.name) },
                            modifier = Modifier.height(40.dp)
                        ) },
                    bottomBar = {
                        BottomAppBarSelector (modifier = Modifier.height(50.dp))
                        {diagramType -> diagramTypeState = diagramType }
                                },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            structureState =
                                if (structureState ==
                                sampleB.copy(name = "MainActivity Sample B"))
                                    sampleA.copy(name = "MainActivity Sample")
                                else
                                    sampleB.copy(name = "MainActivity Sample B")
                        }) {
                            Icon(Icons.Default.Refresh, null)
                        }
                    }
                ) {paddingValues ->
                    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        Column(modifier = Modifier.padding(paddingValues)) {
                            MainCanvas(mainCanvasModifier, structureState, diagramTypeState)
                            BottomSheetContent(
                                structureDataState,
                                dataValueChange = {structureDataState = it}
                            )
                        }
                    } else {
                        Row(modifier = Modifier.padding(paddingValues)) {
                            MainCanvas(mainCanvasModifier, structureState, diagramTypeState)
                            BottomSheetContent(
                                structureDataState,
                                dataValueChange = {structureDataState = it}
                            )
                        }
                    }
                }
            }
        }
    }
}
