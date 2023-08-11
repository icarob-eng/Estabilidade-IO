package com.moon.estabilidade_io.ui.components

import android.app.Activity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TooltipImageButton(
    hint: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    PlainTooltipBox(
        tooltip = { Text(hint) },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        IconButton(
            onClick = onClick,
            modifier = modifier.tooltipAnchor(),
            content = content
        )
    }
}

@Composable
fun BackButton(modifier: Modifier = Modifier) {
    val activity = LocalContext.current as Activity
    TooltipImageButton(hint = "Voltar", modifier = modifier, onClick = { activity.finish() }) {
        Icon(Icons.Default.ArrowBack, "Voltar")
    }
}

