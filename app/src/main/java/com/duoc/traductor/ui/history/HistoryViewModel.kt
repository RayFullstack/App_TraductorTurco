package com.duoc.traductor.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.traductor.data.local.AppDatabase
import com.duoc.traductor.data.local.TranslationEntity
import com.duoc.traductor.data.repository.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = AppRepository(AppDatabase.get(app))

    val items: StateFlow<List<TranslationEntity>> = repo.translations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleFavorite(t: TranslationEntity) {
        viewModelScope.launch { repo.toggleFavorite(t) }
    }

    fun delete(t: TranslationEntity) {
        viewModelScope.launch { repo.delete(t) }
    }
}
