package com.moon.estabilidade_io.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LabeledSettingsSwitch(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Spacer(Modifier.height(6.dp))
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, modifier = Modifier.width((LocalConfiguration.current.screenWidthDp * 0.75).dp))
        // todo: use flex
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
    Divider()
}

@Composable
fun LabeledSettingsSlider(text: String, value: Float, onValueChange: (Float) -> Unit) {
    Spacer(Modifier.height(6.dp))
    Text(text = text)
    Slider(value = value, onValueChange = onValueChange, steps = 10)
    Divider()
}

@Composable
fun LabeledColorInput(text: String, value: String, onValueChange: (String) -> Unit, isError: Boolean = false) {
    Spacer(Modifier.height(6.dp))
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = text, modifier = Modifier.width((LocalConfiguration.current.screenWidthDp * 0.7).dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.width(100.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
            singleLine = true,
            isError = isError
        )
    }
}

@Preview
@Composable
fun SettingsActivityComponentsPreview() {
    Scaffold {
        Column (
            Modifier
                .padding(it)
                .padding(10.dp)) {
            val text = "Texto muito logo de diversas linhas para testar as capaciades deste negócio"
            LabeledSettingsSwitch(
                text = text,
                checked = true, onCheckedChange = {}
            )
            LabeledSettingsSwitch(
                text = text,
                checked = true, onCheckedChange = {}
            )
            LabeledSettingsSlider(text = text, value = 0.5f, onValueChange = {})
            LabeledColorInput(text = text, value = 0x100000.toString(), onValueChange = {})
        }
    }
}