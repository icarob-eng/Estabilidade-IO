@file:OptIn(ExperimentalMaterial3Api::class)

package com.moon.estabilidade_io.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moon.estabilidade_io.R
import com.moon.estabilidade_io.ui.components.BackButton
import com.moon.estabilidade_io.ui.theme.EstabilidadeIOTheme
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun HelpScreen() {
    EstabilidadeIOTheme {
        Scaffold (
            topBar = { CenterAlignedTopAppBar(
                title = { Text(text = "Ajuda e informações") },
                modifier = Modifier.height(32.dp),
                navigationIcon = { BackButton() },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            ) }
        ){ paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(10.dp),
                contentAlignment = Alignment.Center) {
                MarkdownText(
                    markdown = LocalContext.current.resources
                        .openRawResource(R.raw.help_text)
                        .bufferedReader().use { it.readText() },
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}

@Composable
@Preview
fun HelpScreenPreview() = HelpScreen()