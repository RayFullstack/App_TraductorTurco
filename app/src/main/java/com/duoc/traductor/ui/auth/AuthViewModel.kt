package com.duoc.traductor.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.traductor.data.local.AppDatabase
import com.duoc.traductor.data.repository.AppRepository
import com.duoc.traductor.domain.validation.AuthValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isRegister: Boolean = false,
    val name: String = "", val nameError: String? = null,
    val email: String = "", val emailError: String? = null,
    val password: String = "", val passwordError: String? = null,
    val confirm: String = "", val confirmError: String? = null,
    val loading: Boolean = false,
    val message: String? = null,
    val messageIsError: Boolean = false,
    val errorCount: Int = 0,
    val loggedIn: Boolean = false
)

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = AppRepository(AppDatabase.get(app))
    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun onNameChange(v: String) = _state.update {
        it.copy(name = v, nameError = AuthValidator.validateName(v), message = null)
    }

    fun onEmailChange(v: String) = _state.update {
        it.copy(email = v, emailError = AuthValidator.validateEmail(v), message = null)
    }

    fun onPasswordChange(v: String) = _state.update {
        it.copy(
            password = v,
            passwordError = if (it.isRegister) AuthValidator.validatePassword(v) else null,
            confirmError = if (it.isRegister && it.confirm.isNotEmpty())
                AuthValidator.validateConfirm(v, it.confirm) else it.confirmError,
            message = null
        )
    }

    fun onConfirmChange(v: String) = _state.update {
        it.copy(confirm = v, confirmError = AuthValidator.validateConfirm(it.password, v), message = null)
    }

    fun toggleMode() = _state.update {
        AuthUiState(isRegister = !it.isRegister, email = it.email)
    }

    fun submit() {
        val s = _state.value
        val nameErr = if (s.isRegister) AuthValidator.validateName(s.name) else null
        val emailErr = AuthValidator.validateEmail(s.email)
        val passErr = if (s.isRegister) AuthValidator.validatePassword(s.password)
        else if (s.password.isBlank()) "La contraseña es obligatoria" else null
        val confirmErr = if (s.isRegister) AuthValidator.validateConfirm(s.password, s.confirm) else null

        if (listOf(nameErr, emailErr, passErr, confirmErr).any { it != null }) {
            _state.update {
                it.copy(
                    nameError = nameErr, emailError = emailErr,
                    passwordError = passErr, confirmError = confirmErr,
                    errorCount = it.errorCount + 1
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(loading = true, message = null) }
            if (s.isRegister) {
                val ok = repo.register(s.name, s.email, s.password)
                if (ok) {
                    _state.update {
                        AuthUiState(
                            isRegister = false, email = it.email,
                            message = "Cuenta creada. Ahora inicia sesión"
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            loading = false, message = "Ese correo ya está registrado",
                            messageIsError = true, errorCount = it.errorCount + 1
                        )
                    }
                }
            } else {
                val user = repo.login(s.email, s.password)
                if (user != null) {
                    _state.update { it.copy(loading = false, loggedIn = true) }
                } else {
                    _state.update {
                        it.copy(
                            loading = false, message = "Correo o contraseña incorrectos",
                            messageIsError = true, errorCount = it.errorCount + 1
                        )
                    }
                }
            }
        }
    }
}
