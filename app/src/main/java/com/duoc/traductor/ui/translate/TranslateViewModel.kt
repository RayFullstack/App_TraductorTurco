package com.duoc.traductor.ui.translate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.traductor.data.local.AppDatabase
import com.duoc.traductor.data.repository.AppRepository
import com.duoc.traductor.domain.translation.TranslatorManager
import com.duoc.traductor.domain.validation.TranslationValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TranslateUiState(
    val input: String = "",
    val inputError: String? = null,
    val output: String = "",
    val trToEs: Boolean = true,
    val loading: Boolean = false,
    val error: String? = null
)

class TranslateViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = AppRepository(AppDatabase.get(app))
    private val manager = TranslatorManager()

    private val _state = MutableStateFlow(TranslateUiState())
    val state: StateFlow<TranslateUiState> = _state.asStateFlow()

    fun onInputChange(v: String) = _state.update {
        it.copy(
            input = v,
            inputError = if (v.isEmpty()) null else TranslationValidator.validateInput(v),
            output = "", error = null
        )
    }

    fun swap() = _state.update {
        it.copy(
            trToEs = !it.trToEs,
            input = it.output.ifBlank { it.input },
            output = "", inputError = null, error = null
        )
    }

    fun translate() {
        val s = _state.value
        val err = TranslationValidator.validateInput(s.input)
        if (err != null) {
            _state.update { it.copy(inputError = err) }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null, output = "") }
            try {
                val result = manager.translate(s.input.trim(), s.trToEs)
                repo.saveTranslation(s.input.trim(), result, s.trToEs)
                _state.update { it.copy(loading = false, output = result) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        loading = false,
                        error = "No se pudo traducir. Revisa tu conexión (la primera vez se descarga el modelo)."
                    )
                }
            }
        }
    }

    override fun onCleared() {
        manager.close()
        super.onCleared()
    }
}
