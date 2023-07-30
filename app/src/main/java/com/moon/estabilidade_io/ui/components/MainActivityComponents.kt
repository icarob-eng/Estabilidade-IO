@file:OptIn(ExperimentalMaterial3Api::class)

package com.moon.estabilidade_io.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.estabilidade_io.R
import com.moon.estabilidade_io.drawer.DiagramType
import com.wakaztahir.codeeditor.highlight.model.CodeLang
import com.wakaztahir.codeeditor.highlight.prettify.PrettifyParser
import com.wakaztahir.codeeditor.highlight.theme.CodeThemeType
import com.wakaztahir.codeeditor.highlight.utils.parseCodeAsAnnotatedString


data class DiagramTypeSelection(
    val label: String, val tooltip: String, val iconId: Int, val diagramType: DiagramType
)

val selections = listOf(
    DiagramTypeSelection("Estrutura",
        "Mostrar apenas a estrutrua",
        R.drawable.baseline_straighten_24,
        DiagramType.NONE
    ),
    DiagramTypeSelection(
        "Cargas",
        "Mostrar cargas da estrutura",
        R.drawable.baseline_download_24,
        DiagramType.LOADS
    ),
    DiagramTypeSelection(
        "Reações",
        "Mostrar forças de reação",
        R.drawable.baseline_close_fullscreen_24,
        DiagramType.REACTIONS
    ),
    DiagramTypeSelection(
        "DEN",
        "Estrutura e Diagrama de Esforço Normal",
        R.drawable.baseline_swipe_right_alt_24,
        DiagramType.NORMAL
    ),
    DiagramTypeSelection(
        "DEC",
        "Estrutura e Diagrama de Esforço Cortante",
        R.drawable.baseline_vertical_align_bottom_24,
        DiagramType.SHEAR
    ),
    DiagramTypeSelection(
        "DMF",
        "Estrutura e Diagrama de Momento Fletor",
        R.drawable.round_rotate_90_degrees_ccw_24,
        DiagramType.MOMENT
    )
)


@Composable
fun BottomAppBarSelector(
    itemContents: List<DiagramTypeSelection>,
    modifier: Modifier = Modifier,
    onItemClick: (DiagramType) -> Unit
) {
    BottomAppBar(modifier) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            itemContents.forEach {
                PlainTooltipBox(
                    tooltip = { Text(text = it.tooltip)},
                    containerColor = MaterialTheme.colorScheme.background
                ) {
                    Button(
                        onClick = { onItemClick(it.diagramType) },
                        modifier = Modifier.tooltipAnchor(),
                        shape = RoundedCornerShape(5.dp),
                        contentPadding = PaddingValues(0.dp)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(painter = painterResource(id = it.iconId), it.tooltip)
                                Text(text = it.label, textAlign = TextAlign.Center, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomSheetContent(value: String, onValueChange: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(30.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Dados da estrutura:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            YamlTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun YamlTextField(value: String, onValueChange: (String) -> Unit, modifier: Modifier) {
    val language = CodeLang.YAML

    val parser = remember { PrettifyParser() }
    val themeState by remember { mutableStateOf(CodeThemeType.Default) }
    val theme = remember(themeState) { themeState.theme() }

    fun parse(code: String): AnnotatedString {
        return parseCodeAsAnnotatedString(
            parser = parser,
            theme = theme,
            lang = language,
            code = code
        )
    }

    var textFieldValue by remember { mutableStateOf(TextFieldValue(parse(value))) }
    var lineTops by remember { mutableStateOf(emptyArray<Float>()) }
    val density = LocalDensity.current

    Row (
        Modifier
            .background(Color.DarkGray)
            .verticalScroll(rememberScrollState())){  // todo: external bgcolor
        if (lineTops.isNotEmpty()) {
            Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                lineTops.forEachIndexed { index, top ->
                    Text(
                        modifier = Modifier.offset(y = with(density) { top.toDp() }),
                        text = index.toString(),
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        BasicTextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it.copy(annotatedString = parse(it.text))
                onValueChange(it.text)
            },
            modifier = modifier,
            textStyle = TextStyle(fontFamily = FontFamily.Monospace),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Ascii
            ),
            onTextLayout = { result ->
                lineTops = Array(result.lineCount) { result.getLineTop(it) }
            }
        )
    }
}