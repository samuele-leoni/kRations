package com.example.krations.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krations.profile.ui.ProfileIcon
import com.example.krations.shoppinglist.ui.ShoppingListScreen

private val AppBackground = Color(0xFFFFF8FF)
private val Ink = Color(0xFF211F26)

@Composable
fun HomeScreen(email: String?, onProfileClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text("kRations", fontSize = 22.sp, fontWeight = FontWeight.Medium, color = Ink)
                if (email != null) {
                    Text(email, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            TextButton(
                onClick = onProfileClick,
                modifier = Modifier.semantics { contentDescription = "Profile and account access" },
            ) {
                ProfileIcon()
            }
        }
        ShoppingListScreen(modifier = Modifier.weight(1f))
    }
}
