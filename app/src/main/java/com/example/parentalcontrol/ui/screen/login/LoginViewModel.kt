package com.example.parentalcontrol.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parentalcontrol.data.repository.AuthRepository
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        uiState = LoginUiState(isLoading = true, error = null)

        viewModelScope.launch {
            val result = repository.loginParent(email, password)

            result.onSuccess {
                uiState = LoginUiState()
                onSuccess()
            }.onFailure {
                uiState = LoginUiState(isLoading = false, error = "Invalid email or password")
            }
        }
    }

    fun clearError() {
        uiState = uiState.copy(error = null)
    }
}
