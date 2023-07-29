@file:OptIn(ExperimentalMaterial3Api::class)

package com.moon.estabilidade_io.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.moon.estabilidade_io.ui.theme.EstabilidadeIOTheme

@Composable
fun HelpScreen() {
    EstabilidadeIOTheme {
        Scaffold (
            topBar = { CenterAlignedTopAppBar(title = { Text(text = "Ajuda e informações") }) }
        ){
            Text(text = "content here", Modifier.padding(it))
        }
    }
}

@Composable
@Preview
fun HelpScreenPreview() = HelpScreen()