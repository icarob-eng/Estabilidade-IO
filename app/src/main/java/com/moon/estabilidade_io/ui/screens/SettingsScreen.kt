@file:OptIn(ExperimentalMaterial3Api::class)

package com.moon.estabilidade_io.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moon.estabilidade_io.ui.components.BackButton
import com.moon.estabilidade_io.ui.components.LabeledSettingsSwitch
import com.moon.estabilidade_io.ui.components.TooltipImageButton
import com.moon.estabilidade_io.ui.theme.EstabilidadeIOTheme
import com.moon.estabilidade_io.ui.viewModels.SettingsViewModel

@Composable
fun SettingsScreen() {
    val settingsVm: SettingsViewModel = viewModel()
    val context = LocalContext.current

    EstabilidadeIOTheme {
        Scaffold (
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Configurações") },
                    navigationIcon = { BackButton() },
                    actions = {
                        TooltipImageButton(
                            hint = "Salvar alterações",
                            onClick = { settingsVm.savePreferences(context) }
                        ) {
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

                LabeledSettingsSwitch(
                    text = "Usar outra aparência para apoio de segundo gênero",
                    checked = state.useRollerB, onCheckedChange = {settingsVm.useRollerBToggle()}
                )
                LabeledSettingsSwitch(
                    text = "Mostrar bordas",
                    checked = state.showEdges, onCheckedChange = {settingsVm.showEdgesToggle()}
                )

                Switch(checked = settingsVm.showColors, onCheckedChange = {settingsVm.showColors = !settingsVm.showColors})

                Row (Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(onClick = { settingsVm.clearDataStore(context) }) {
                        Icon(Icons.Default.Warning, contentDescription = "")
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Limpar preferências.\nIsso reinicia o app")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() = SettingsScreen()