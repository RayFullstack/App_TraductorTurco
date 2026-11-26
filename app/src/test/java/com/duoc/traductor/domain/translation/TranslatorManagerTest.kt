package com.duoc.traductor.domain.translation

import com.duoc.traductor.data.remote.ResponseData
import com.duoc.traductor.data.remote.TranslationApiService
import com.duoc.traductor.data.remote.TranslationResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TranslatorManagerTest {

    private val apiService: TranslationApiService = mockk()
    private val manager = TranslatorManager(apiService)

    @Test
    fun `translate traduce de espanol a turco conservando tildes y ene`() = runTest {
        coEvery { apiService.translate("Tomo café por las mañanas", "es|tr") } returns
            TranslationResponse(ResponseData("Sabahları kahve içerim"))

        val result = manager.translate("  Tomo café por las mañanas  ", trToEs = false)

        assertEquals("Sabahları kahve içerim", result)
        coVerify(exactly = 1) { apiService.translate("Tomo café por las mañanas", "es|tr") }
    }

    @Test
    fun `translate traduce de turco a espanol con caracteres especiales turcos`() = runTest {
        coEvery { apiService.translate("Günaydın, nasılsınız?", "tr|es") } returns
            TranslationResponse(ResponseData("Buenos días, ¿cómo está?"))

        val result = manager.translate("Günaydın, nasılsınız?", trToEs = true)

        assertEquals("Buenos días, ¿cómo está?", result)
        coVerify(exactly = 1) { apiService.translate("Günaydın, nasılsınız?", "tr|es") }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `translate lanza excepcion cuando el texto esta vacio`() = runTest {
        manager.translate("   ", trToEs = false)
    }
}
