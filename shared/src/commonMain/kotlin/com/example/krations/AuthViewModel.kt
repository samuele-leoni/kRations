package com.example.krations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.krations.core.data.CurrentUserProvider
import com.example.krations.core.data.SupabaseSyncEngine
import com.example.krations.core.database.LocalDataOwnershipMigrator
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.seconds

data class AuthUiState(
    val initializing: Boolean = true,
    val authenticated: Boolean = false,
    val email: String? = null,
    val loading: Boolean = false,
    val syncing: Boolean = false,
    val message: String? = null,
    val error: String? = null,
)

class AuthViewModel(
    private val supabase: SupabaseClient,
    private val currentUserProvider: CurrentUserProvider,
    private val localDataOwnershipMigrator: LocalDataOwnershipMigrator,
    private val supabaseSyncEngine: SupabaseSyncEngine,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            supabase.auth.sessionStatus.collect { status ->
                when (status) {
                    SessionStatus.Initializing ->
                        _uiState.update { it.copy(initializing = true) }
                    is SessionStatus.Authenticated -> {
                        val userId = status.session.user?.id
                        if (userId.isNullOrBlank()) {
                            currentUserProvider.setAuthenticatedUser(null)
                            _uiState.update {
                                it.copy(
                                    initializing = false,
                                    authenticated = false,
                                    email = null,
                                    loading = false,
                                    error = "Could not identify the signed-in account.",
                                )
                            }
                        } else {
                            runCatching { localDataOwnershipMigrator.moveLocalDataTo(userId) }
                                .onSuccess {
                                    currentUserProvider.setAuthenticatedUser(userId)
                                    _uiState.update {
                                        it.copy(
                                            initializing = false,
                                            authenticated = true,
                                            email = status.session.user?.email,
                                            loading = false,
                                            error = null,
                                        )
                                    }
                                    synchronizeNow()
                                }
                                .onFailure { error ->
                                    currentUserProvider.setAuthenticatedUser(null)
                                    _uiState.update {
                                        it.copy(
                                            initializing = false,
                                            authenticated = false,
                                            email = null,
                                            loading = false,
                                            error = error.message ?: "Could not assign local data to this account.",
                                        )
                                    }
                                }
                        }
                    }
                    is SessionStatus.NotAuthenticated -> {
                        currentUserProvider.setAuthenticatedUser(null)
                        _uiState.update {
                            it.copy(initializing = false, authenticated = false, email = null, loading = false)
                        }
                    }
                    is SessionStatus.RefreshFailure -> {
                        currentUserProvider.setAuthenticatedUser(null)
                        _uiState.update {
                            it.copy(
                                initializing = false,
                                authenticated = false,
                                email = null,
                                loading = false,
                                error = "Your session expired. Please sign in again.",
                            )
                        }
                    }
                }
            }
        }
    }

    fun synchronizeNow() {
        val userId = currentUserProvider.userId.value
        if (userId == "local-user" || _uiState.value.syncing) return

        _uiState.update { it.copy(syncing = true, error = null) }
        viewModelScope.launch {
            try {
                withTimeout(45.seconds) {
                    supabaseSyncEngine.synchronize(userId)
                }
            } catch (_: TimeoutCancellationException) {
                _uiState.update {
                    it.copy(error = "Synchronization timed out. Check your connection and try again.")
                }
            } catch (error: kotlinx.coroutines.CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(error = error.message ?: "Synchronization could not be completed.")
                }
            } finally {
                _uiState.update { it.copy(syncing = false) }
            }
        }
    }

    fun signIn(email: String, password: String) = authenticate(email, password) {
        supabase.auth.signInWith(Email) {
            this.email = email.trim()
            this.password = password
        }
    }

    fun signUp(email: String, password: String) = authenticate(email, password) {
        supabase.auth.signUpWith(Email, redirectUrl = AndroidAuthRedirect.url) {
            this.email = email.trim()
            this.password = password
        }
        if (supabase.auth.currentSessionOrNull() == null) {
            _uiState.update {
                it.copy(message = "Check your email to confirm your account, then sign in.")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null, message = null) }
            runCatching { supabase.auth.signOut() }.onFailure { error ->
                _uiState.update {
                    it.copy(loading = false, error = error.message ?: "Could not sign out.")
                }
            }
        }
    }

    fun clearFeedback() = _uiState.update { it.copy(message = null, error = null) }

    private fun authenticate(email: String, password: String, action: suspend () -> Unit) {
        val normalizedEmail = email.trim()
        if (normalizedEmail.isBlank() || '@' !in normalizedEmail) {
            _uiState.update { it.copy(error = "Enter a valid email address.", message = null) }
            return
        }
        if (password.isBlank()) {
            _uiState.update { it.copy(error = "Enter your password.", message = null) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null, message = null) }
            runCatching { action() }
                .onSuccess { _uiState.update { it.copy(loading = false) } }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(loading = false, error = error.message ?: "The request could not be completed.")
                    }
                }
        }
    }
}
