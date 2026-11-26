package com.duoc.traductor.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translations")
data class TranslationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val original: String,
    val translated: String,
    val direction: String, // "TR_ES" o "ES_TR"
    val isFavorite: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
