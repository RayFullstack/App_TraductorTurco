package com.duoc.traductor.domain.translation

import com.duoc.traductor.data.remote.TranslationApiService
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class TranslatorManager(
    private val apiService: TranslationApiService = TranslationApiService.create()
) {
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
        val cleanText = text.trim()
        require(cleanText.isNotEmpty()) { "El texto a traducir no puede estar vacío" }

        val langPair = if (trToEs) "tr|es" else "es|tr"

        return try {
            val response = apiService.translate(cleanText, langPair)
            val result = response.responseData.translatedText
            if (result.isNotBlank()) result else translateOfflineFallback(cleanText, trToEs)
        } catch (e: Exception) {
            translateOfflineFallback(cleanText, trToEs)
        }
    }

    private suspend fun translateOfflineFallback(text: String, trToEs: Boolean): String {
        return withTimeout(10_000L) {
            val (source, target) = if (trToEs) {
                TranslateLanguage.TURKISH to TranslateLanguage.SPANISH
            } else {
                TranslateLanguage.SPANISH to TranslateLanguage.TURKISH
            }
            val translator = client(source, target)
            translator.downloadModelIfNeeded(DownloadConditions.Builder().build()).await()
            translator.translate(text).await()
        }
    }

    fun close() {
        cache.values.forEach { it.close() }
        cache.clear()
    }
}
