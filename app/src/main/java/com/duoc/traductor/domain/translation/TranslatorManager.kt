package com.duoc.traductor.domain.translation

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await

class TranslatorManager {
    private val cache = mutableMapOf<String, Translator>()

    private fun client(source: String, target: String): Translator =
        cache.getOrPut("$source-$target") {
            Translation.getClient(
                TranslatorOptions.Builder()
                    .setSourceLanguage(source)
                    .setTargetLanguage(target)
                    .build()
            )
        }

    suspend fun translate(text: String, trToEs: Boolean): String {
        val (source, target) = if (trToEs) {
            TranslateLanguage.TURKISH to TranslateLanguage.SPANISH
        } else {
            TranslateLanguage.SPANISH to TranslateLanguage.TURKISH
        }
        val translator = client(source, target)
        translator.downloadModelIfNeeded(DownloadConditions.Builder().build()).await()
        return translator.translate(text).await()
    }

    fun close() {
        cache.values.forEach { it.close() }
        cache.clear()
    }
}
