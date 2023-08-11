@file:OptIn(ExperimentalMaterial3Api::class)

package com.moon.estabilidade_io.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moon.estabilidade_io.ui.components.BackButton
import com.moon.estabilidade_io.ui.components.TooltipImageButton
import com.moon.estabilidade_io.ui.theme.EstabilidadeIOTheme
import com.moon.estabilidade_io.ui.viewModels.SettingsViewModel

@Composable
fun SettingsScreen() {
    val settingsVm: SettingsViewModel = viewModel()

    EstabilidadeIOTheme {
        Scaffold (
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Configurações") },
                    navigationIcon = { BackButton() },
                    actions = {
                        TooltipImageButton(
                            hint = "Salvar alterações", onClick = settingsVm::savePreferences) {
                            Icon(Icons.Default.Done, contentDescription = "Salvar aterações")
                        }
                    }
                )
            }
        ) {
            Column (
                Modifier
                    .padding(it)
                    .padding(10.dp)) {
                val state by settingsVm.uiState.collectAsState()

                Row {
                    Text(text = "Usar outra aparência para apoio de segundo gênero")
                    Switch(checked = state.useRollerB, onCheckedChange = {settingsVm.useRollerBToggle()})
                }
                Row {
                    Text(text = "Mostrar bordas")
                    Switch(checked = state.showEdges, onCheckedChange = {settingsVm.showEdgesToggle()})
                }
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() = SettingsScreen()