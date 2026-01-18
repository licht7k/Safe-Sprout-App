package com.example.parentalcontrol.ui.screen.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parentalcontrol.data.repository.AuthRepository
import kotlinx.coroutines.launch

data class SignUpUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)


class SignUpViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf(SignUpUiState())
        private set

    fun register(name: String, email: String, password: String, onSuccess: () -> Unit) {
        uiState = SignUpUiState(isLoading = true, error = null)

        viewModelScope.launch {
            val result = repository.registerParent(name, email, password)

            result.onSuccess {
                uiState = SignUpUiState()
                onSuccess()
            }.onFailure {
                    e ->
                uiState = SignUpUiState(
                    isLoading = false,
                    error = e.message ?: e.toString()
                )
            }
        }
    }

    fun clearError() {
        uiState = uiState.copy(error = null)
    }
}
