package com.example.krations.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krations.AuthUiState
import com.example.krations.profile.ui.ProfileIcon

private val CardBackground = Color(0xFFF3EDF7)
private val Accent = Color(0xFFEADDFF)
private val Ink = Color(0xFF211F26)
private val AppBackground = Color(0xFFFFF8FF)

@Composable
fun AuthScreen(
    state: AuthUiState,
    onBack: () -> Unit,
    onSignIn: (String, String) -> Unit,
    onSignUp: (String, String) -> Unit,
    onClearFeedback: () -> Unit,
) {
    var isSignUp by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("← Continue as guest") }
        }
        Spacer(Modifier.height(20.dp))
        Surface(modifier = Modifier.size(72.dp), shape = CircleShape, color = Accent) {
            ProfileIcon(Modifier.padding(20.dp), color = Ink)
        }
        Spacer(Modifier.height(20.dp))
        Text(
            if (isSignUp) "Create account" else "Sign in",
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = Ink,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            if (isSignUp) "Create your kRations account."
            else "Sign in to your kRations account.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(28.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; onClearFeedback() },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email address") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            enabled = !state.loading,
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; onClearFeedback() },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            enabled = !state.loading,
        )
        state.error?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth())
        }
        state.message?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth())
        }
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = { if (isSignUp) onSignUp(email, password) else onSignIn(email, password) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            enabled = !state.loading,
            shape = RoundedCornerShape(28.dp),
        ) {
            if (state.loading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text(if (isSignUp) "Create account" else "Sign in")
            }
        }
        Spacer(Modifier.height(12.dp))
        HorizontalDivider(color = CardBackground)
        TextButton(
            onClick = {
                isSignUp = !isSignUp
                onClearFeedback()
            },
            enabled = !state.loading,
        ) {
            Text(
                if (isSignUp) "Already have an account? Sign in"
                else "Don't have an account? Create one",
            )
        }
    }
}
