@file:OptIn(ExperimentalMaterial3Api::class)

package com.moon.estabilidade_io.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                        TooltipImageButton(hint = "Salvar alterações", onClick = { /*TODO*/ }) {
                            Icon(Icons.Default.Done, contentDescription = "Salvar aterações")
                        }
                    }
                )
            }
        ) {
            Column (Modifier.padding(it).padding(10.dp)) {
                Text("texto inserido: ${settingsVm.text}")

                TextField(value = settingsVm.text, onValueChange = {value -> settingsVm.text = value})
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() = SettingsScreen()