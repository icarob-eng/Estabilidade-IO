package com.moon.estabilidade_io.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moon.estabilidade_io.R
import com.moon.estabilidade_io.models.MenuItem
import com.moon.estabilidade_io.ui.theme.EstabilidadeIOTheme

@Composable
fun DrawerHeader(){
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                /*brush = Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    ),
                )*/
                color = colorResource(id = R.color.primary)
            )
    ){
        Text(text = "Opções", color = Color.White, modifier =  Modifier.padding(start = 20.dp))
    }
}

@Composable
fun DrawerBody(items:List<MenuItem>, modifier: Modifier = Modifier){
    LazyColumn(modifier = modifier){
        items(items.size){
            index ->
            Box(Modifier.background(color = MaterialTheme.colorScheme.background)){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = items[index].icon,
                        contentDescription = items[index].contentDescription,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = items[index].title, color = Color.White)
                }
            }
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DrawerBodyPreview(){
    EstabilidadeIOTheme {
        DrawerBody(listOf(MenuItem("config","Configurações", null, icon = Icons.Default.Settings)))
    }
}

@Preview(uiMode =  UI_MODE_NIGHT_YES)
@Composable
fun DrawerHeaderPreview(){
    EstabilidadeIOTheme{
        DrawerHeader()
    }
}