package com.example.krations

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.krations.auth.ui.AuthScreen
import com.example.krations.auth.ui.LoadingScreen
import com.example.krations.home.ui.HomeScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthRoute() {
    val authViewModel: AuthViewModel = koinViewModel()
    val authState by authViewModel.uiState.collectAsState()
    var showAuth by remember { mutableStateOf(false) }
    var showAccount by remember { mutableStateOf(false) }

    LaunchedEffect(authState.authenticated) {
        if (authState.authenticated) {
            showAuth = false
        } else {
            showAccount = false
        }
    }

    when {
        authState.initializing -> LoadingScreen()
        showAuth && !authState.authenticated -> AuthScreen(
            state = authState,
            onBack = { showAuth = false },
            onSignIn = authViewModel::signIn,
            onSignUp = authViewModel::signUp,
            onClearFeedback = authViewModel::clearFeedback,
        )
        else -> HomeScreen(
            onProfileClick = {
                if (authState.authenticated) showAccount = true else showAuth = true
            },
        )
    }

    if (showAccount && authState.authenticated) {
        AlertDialog(
            onDismissRequest = { showAccount = false },
            title = { Text("My account") },
            text = { Text(authState.email ?: "Signed in") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showAccount = false
                        authViewModel.signOut()
                    },
                    enabled = !authState.loading,
                ) {
                    Text("Sign out")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAccount = false }) {
                    Text("Cancel")
                }
            },
        )
    }
}
