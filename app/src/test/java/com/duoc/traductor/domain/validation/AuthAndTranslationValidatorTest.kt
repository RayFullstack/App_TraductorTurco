package com.duoc.traductor.domain.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class AuthAndTranslationValidatorTest {

    @Test
    fun `validateName acepta nombres validos y rechaza cortos o vacios`() {
        assertNotNull(AuthValidator.validateName(""))
        assertNotNull(AuthValidator.validateName("ab"))
        assertNull(AuthValidator.validateName("Ayşe Yılmaz"))
    }

    @Test
    fun `validateEmail valida formato correcto e incorrecto`() {
        assertNotNull(AuthValidator.validateEmail(""))
        assertNotNull(AuthValidator.validateEmail("usuario-sin-arroba.cl"))
        assertNull(AuthValidator.validateEmail("estudiante@duocuc.cl"))
    }

    @Test
    fun `validatePassword exige minimo 6 caracteres y al menos un numero`() {
        assertNotNull(AuthValidator.validatePassword("12345"))
        assertNotNull(AuthValidator.validatePassword("abcdef"))
        assertNull(AuthValidator.validatePassword("turco2026"))
    }

    @Test
    fun `validateConfirm verifica coincidencia de contrasenas`() {
        assertNotNull(AuthValidator.validateConfirm("turco2026", "otra123"))
        assertNull(AuthValidator.validateConfirm("turco2026", "turco2026"))
    }

    @Test
    fun `validateInput acepta tildes n y caracteres especiales turcos`() {
        assertEquals("Escribe un texto para traducir", TranslationValidator.validateInput("   "))
        assertEquals("Máximo 500 caracteres", TranslationValidator.validateInput("a".repeat(501)))
        assertNull(TranslationValidator.validateInput("Tomo café por las mañanas - Çok güzel"))
    }
}
