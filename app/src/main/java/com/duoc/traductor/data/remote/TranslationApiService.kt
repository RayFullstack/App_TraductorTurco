package com.duoc.traductor.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class TranslationResponse(
    val responseData: ResponseData
)

data class ResponseData(
    val translatedText: String
)

interface TranslationApiService {
    @GET("get")
    suspend fun translate(
        @Query("q") text: String,
        @Query("langpair") langPair: String
    ): TranslationResponse

    companion object {
        private const val BASE_URL = "https://api.mymemory.translated.net/"

        fun create(): TranslationApiService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslationApiService::class.java)
    }
}