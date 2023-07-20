package com.moon.estabilidade_io.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moon.estabilidade_io.drawer.DiagramType


@Composable
fun BottomAppBarSelector(modifier: Modifier = Modifier, onItemClick: (DiagramType) -> Unit) {
    val types = listOf(
        Triple("Estrutura", Icons.Outlined.Close, DiagramType.NONE),
        Triple("Cargas", Icons.Outlined.KeyboardArrowDown, DiagramType.LOADS),
        Triple("Reações", Icons.Default.Refresh, DiagramType.REACTIONS),
        Triple("DEN", Icons.Default.KeyboardArrowLeft, DiagramType.NORMAL),
        Triple("DEC", Icons.Default.ArrowDropDown, DiagramType.SHEAR),
        Triple("DMF", Icons.Default.Refresh, DiagramType.MOMENT)
    )
    BottomAppBar(modifier) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            types.forEach {(label, icon, type) ->
                Button(
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(5.dp),
                    onClick = { onItemClick(type) }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(icon, label)
                        Text(text = label, textAlign = TextAlign.Center, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomSheetContent(dataString: String, dataValueChange: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(30.dp)
    ) {
        Box(modifier = Modifier.padding(15.dp)
        ) {
            TextField(
                value = dataString,
                onValueChange = dataValueChange,
                modifier = Modifier.fillMaxSize(),
                label = { Text(text = "Dados da estrutura:", fontWeight = FontWeight.Bold) },
                textStyle = TextStyle(fontFamily = FontFamily.Monospace)
            )
        }
    }
}