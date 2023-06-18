package com.moon.estabilidade_io.components

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.moon.estabilidade_io.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(onNavigationIconClick:() -> Unit){
    TopAppBar(
        title= { Text(text= stringResource(id = R.string.app_name), color = MaterialTheme.colorScheme.onPrimary) },
        Modifier.background(color = MaterialTheme.colorScheme.primary),
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }
        }
    )
}

@Preview
@Composable
fun AppBarPreview(){
    AppBar(onNavigationIconClick = {})
}