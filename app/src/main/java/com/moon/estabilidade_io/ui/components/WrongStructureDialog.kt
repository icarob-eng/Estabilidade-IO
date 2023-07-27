package com.moon.estabilidade_io.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun WrongStructureDialog(
    errorMessage: String,
    onCancel: () -> Unit,
    onRestore: () -> Unit
) {
    Dialog(
        onDismissRequest = onCancel,
    ) {
        Card {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Estrutura passada é invalida",
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Column {
                    Text(text = "Mensagem de erro gerada:",
                        modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    Spacer(Modifier.padding(8.dp))
                    Text(text = errorMessage, modifier = Modifier.background(Color.DarkGray), // todo: use pallet
                        fontFamily = FontFamily.Monospace)
                    Divider(Modifier.padding(8.dp))
//                    Text(  // fixme: restore not working (see mainVM.restoreYaml())
//                        text = "Deseja restaurar os valores digitados para a última versão validada?",
//                        modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
//                    )
                }
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = onCancel) { Text(text = "Cancelar") }
//                    TextButton(onClick = onRestore) { Text(text = "Restaurar valores") }
                }
            }
        }
    }
}

@Preview
@Composable
fun WrongStructureDialogPreview() {
    var showDialog by remember { mutableStateOf(true) }

    Surface(
        Modifier
            .fillMaxSize()
            .clickable { showDialog = true }) {
        if (showDialog)
            WrongStructureDialog(
                "soup is good food",
                onCancel = {
                    Log.d("Dialog_Preview", "onCancel()")
                    showDialog = false
                           },
                onRestore = {
                    Log.d("Dialog_Preview", "onRestore()")
                    showDialog = false
                }
            )
    }
}