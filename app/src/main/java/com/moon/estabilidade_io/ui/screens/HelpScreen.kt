@file:OptIn(ExperimentalMaterial3Api::class)

package com.moon.estabilidade_io.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moon.estabilidade_io.ui.theme.EstabilidadeIOTheme
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun HelpScreen() {
    EstabilidadeIOTheme {
        Scaffold (
            topBar = { CenterAlignedTopAppBar(
                title = { Text(text = "Ajuda e informações") }
            ) }
        ){
            Box(modifier = Modifier.padding(it).padding(10.dp), contentAlignment = Alignment.Center) {
                MarkdownText(
                    markdown = markdown,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
@Preview
fun HelpScreenPreview() = HelpScreen()

const val markdown =
"""
# Hello world

content here

## Subtitle

- Item
  - Sub Item
  - Thanks
  - a
  - a
  - a
  - a
  - a
  - a
  - a
  - a
  - a
  - a
  - a
  - a
  - a
  - a
  - a
  - a

`code`

```yaml
estrutura: Estrutura exemplo
nós:
  A: [0, 0]
  B: [1, 0]
  C: [2, 0]

apoios:
  A:
    gênero: 1
    direção: vertical
  C:
    gênero: 2
    direção: vertical
barras:
  - [A, C]

cargas:
  F1:
    nó: B
    direção: vertical
    módulo: -10
```
"""