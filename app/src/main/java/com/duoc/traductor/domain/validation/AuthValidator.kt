package com.duoc.traductor.domain.validation

/** Lógica de validación pura: no depende de ningún componente visual. */
object AuthValidator {
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun validateName(name: String): String? = when {
        name.isBlank() -> "El nombre es obligatorio"
        name.trim().length < 3 -> "Mínimo 3 caracteres"
        else -> null
    }

    fun validateEmail(email: String): String? = when {
        email.isBlank() -> "El correo es obligatorio"
        !EMAIL_REGEX.matches(email.trim()) -> "Correo inválido"
        else -> null
    }

    fun validatePassword(pass: String): String? = when {
        pass.isBlank() -> "La contraseña es obligatoria"
        pass.length < 6 -> "Mínimo 6 caracteres"
        !pass.any { it.isDigit() } -> "Debe incluir al menos un número"
        else -> null
    }

    fun validateConfirm(pass: String, confirm: String): String? = when {
        confirm.isBlank() -> "Confirma tu contraseña"
        pass != confirm -> "Las contraseñas no coinciden"
        else -> null
    }
}

object TranslationValidator {
    fun validateInput(text: String): String? = when {
        text.isBlank() -> "Escribe un texto para traducir"
        text.length > 500 -> "Máximo 500 caracteres"
        else -> null
    }
}
