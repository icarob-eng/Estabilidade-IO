@file:OptIn(ExperimentalMaterial3Api::class)

package com.moon.estabilidade_io

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.moon.estabilidade_io.drawer.DiagramType
import com.moon.estabilidade_io.drawer.MainCanvas
import com.moon.estabilidade_io.ui.components.BottomAppBarSelector
import com.moon.estabilidade_io.ui.components.BottomSheetContent
import com.moon.estabilidade_io.ui.theme.EstabilidadeIOTheme

class MainActivity : ComponentActivity() {
    private val mainVM: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val uiState by mainVM.uiState.collectAsState()

            EstabilidadeIOTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(text =
                            uiState.diagramData?.structure?.name?: "Nenhuma estrutura selecionada"
                            ) },
                            modifier = Modifier.height(40.dp)
                        ) },
                    bottomBar = {
                        val types = listOf(
                            Triple("Estrutura", R.drawable.baseline_straighten_24, DiagramType.NONE),
                            Triple("Cargas", R.drawable.baseline_download_24, DiagramType.LOADS),
                            Triple("Reações", R.drawable.baseline_close_fullscreen_24, DiagramType.REACTIONS),
                            Triple("DEN", R.drawable.baseline_swipe_right_alt_24, DiagramType.NORMAL),
                            Triple("DEC", R.drawable.baseline_vertical_align_bottom_24, DiagramType.SHEAR),
                            Triple("DMF", R.drawable.round_rotate_90_degrees_ccw_24, DiagramType.MOMENT)
                        )
                        BottomAppBarSelector (
                            itemContents = types,
                            modifier = Modifier.height(50.dp),
                            onItemClick = mainVM::setDiagramType
                        )
                                },
                    floatingActionButton = {
                        FloatingActionButton(
                            shape = CircleShape,
                            onClick = mainVM::parseYamlValue
                        ) {
                            Icon(Icons.Default.PlayArrow, null)
                        }
                    }
                ) {paddingValues ->
                    val mainCanvasModifier = Modifier
                        .size(LocalConfiguration.current.smallestScreenWidthDp.dp)

                    val diagramData = uiState.diagramData

                    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        Column(modifier = Modifier.padding(paddingValues)) {
                            MainCanvas(mainCanvasModifier, diagramData)
                            BottomSheetContent(
                                mainVM.yamlValue
                            ) { mainVM.yamlValue = it }
                        }
                    } else {
                        Row(modifier = Modifier.padding(paddingValues)) {
                            MainCanvas(mainCanvasModifier, diagramData)
                            BottomSheetContent(
                                mainVM.yamlValue
                            ) { mainVM.yamlValue = it }
                        }
                    }
                }
            }
        }
    }
}
