package com.example.krations

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.krations.shoppinglist.ui.ShoppingListScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        Scaffold(modifier = Modifier.padding(8.dp)) { innerPadding ->
            ShoppingListScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}