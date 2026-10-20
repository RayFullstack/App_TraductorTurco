package com.duoc.traductor.data.repository

import com.duoc.traductor.data.local.AppDatabase
import com.duoc.traductor.data.local.TranslationEntity
import com.duoc.traductor.data.local.UserEntity
import java.security.MessageDigest

class AppRepository(db: AppDatabase) {
    private val userDao = db.userDao()
    private val translationDao = db.translationDao()

    val translations = translationDao.getAll()

    /** Devuelve false si el correo ya existe. */
    suspend fun register(name: String, email: String, password: String): Boolean {
        val mail = email.trim().lowercase()
        if (userDao.findByEmail(mail) != null) return false
        userDao.insert(UserEntity(name = name.trim(), email = mail, passwordHash = hash(password)))
        return true
    }

    suspend fun login(email: String, password: String): UserEntity? {
        val user = userDao.findByEmail(email.trim().lowercase()) ?: return null
        return if (user.passwordHash == hash(password)) user else null
    }

    suspend fun saveTranslation(original: String, translated: String, trToEs: Boolean) {
        translationDao.insert(
            TranslationEntity(
                original = original,
                translated = translated,
                direction = if (trToEs) "TR_ES" else "ES_TR"
            )
        )
    }

    suspend fun toggleFavorite(t: TranslationEntity) = translationDao.setFavorite(t.id, !t.isFavorite)

    suspend fun delete(t: TranslationEntity) = translationDao.delete(t)

    private fun hash(text: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(text.toByteArray())
            .joinToString("") { "%02x".format(it) }
}
