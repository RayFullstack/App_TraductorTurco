package com.duoc.traductor.native

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/** Recurso nativo 2: vibración (permiso VIBRATE). */
class HapticHelper(context: Context) {
    private val vibrator: Vibrator? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(VibratorManager::class.java)?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }

    fun success() = vibrate(40)
    fun error() = vibrate(220)

    private fun vibrate(ms: Long) {
        val v = vibrator ?: return
        if (v.hasVibrator()) {
            v.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}
