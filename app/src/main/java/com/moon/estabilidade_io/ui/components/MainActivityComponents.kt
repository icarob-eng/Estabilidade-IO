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
import androidx.compose.material3.Icon
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
import com.moon.estabilidade_io.drawer.DiagramType
import com.wakaztahir.codeeditor.highlight.model.CodeLang
import com.wakaztahir.codeeditor.highlight.prettify.PrettifyParser
import com.wakaztahir.codeeditor.highlight.theme.CodeThemeType
import com.wakaztahir.codeeditor.highlight.utils.parseCodeAsAnnotatedString


@Composable
fun BottomAppBarSelector(
    itemContents: List<Triple<String, Int, DiagramType>>,
    modifier: Modifier = Modifier,
    onItemClick: (DiagramType) -> Unit
) {
    BottomAppBar(modifier) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            itemContents.forEach {(label, iconId, type) ->
                Button(
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(5.dp),
                    onClick = { onItemClick(type) }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painter = painterResource(id = iconId), label)
                        Text(text = label, textAlign = TextAlign.Center, fontSize = 10.sp)
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