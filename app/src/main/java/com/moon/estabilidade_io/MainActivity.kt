@file:OptIn(ExperimentalMaterial3Api::class)

package com.moon.estabilidade_io

import android.content.Intent
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.moon.estabilidade_io.drawer.MainCanvas
import com.moon.estabilidade_io.ui.activities.HelpActivity
import com.moon.estabilidade_io.ui.activities.SettingsActivity
import com.moon.estabilidade_io.ui.components.BottomAppBarSelector
import com.moon.estabilidade_io.ui.components.BottomSheetContent
import com.moon.estabilidade_io.ui.components.TooltipImageButton
import com.moon.estabilidade_io.ui.components.selections
import com.moon.estabilidade_io.ui.theme.EstabilidadeIOTheme
import com.moon.estabilidade_io.ui.viewModels.PreferencesKeys

class MainActivity : ComponentActivity() {
    private val mainVM: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val uiState by mainVM.uiState.collectAsState()
            EstabilidadeIOTheme {

                WrongStructureDialogLauncher(vm = mainVM)
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(text =
                            uiState.diagramData?.structure?.name?: "Nenhuma estrutura selecionada"
                            ) },
                            modifier = Modifier.padding(horizontal = 10.dp),
                            actions = {
                                TooltipImageButton(
                                    hint = "Ajuda",
                                    onClick = { startActivity( Intent(
                                        this@MainActivity.applicationContext,
                                        HelpActivity::class.java
                                    )) }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.round_help_outline_24),
                                        contentDescription = "Ajuda"
                                    )
                                }
                                TooltipImageButton(
                                    hint = "Configurações",
                                    onClick = { startActivity( Intent(
                                        this@MainActivity.applicationContext,
                                        SettingsActivity::class.java
                                    )) }) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Configurações"
                                    )
                                }
                            }
                        ) },
                    bottomBar = {
                        BottomAppBarSelector (
                            itemContents = selections,
                            modifier = Modifier.height(50.dp),
                            onItemClick = mainVM::setDiagramType
                        )
                                },
                    floatingActionButton = {
                        PlainTooltipBox(
                            tooltip = { Text("Mostrar estrutura") },
                            containerColor = MaterialTheme.colorScheme.background
                        ) {
                            FloatingActionButton(
                                onClick = mainVM::parseYamlValue,
                                modifier = Modifier.tooltipAnchor(),
                                shape = CircleShape
                            ) {
                                Icon(Icons.Default.PlayArrow, null)
                            }
                        }
                    }
                ) {paddingValues ->

                    val mainCanvasModifier = Modifier
                        .size(LocalConfiguration.current.smallestScreenWidthDp.dp)

                    val diagramData = uiState.diagramData

                    PreferencesKeys.InitPreferences()

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
