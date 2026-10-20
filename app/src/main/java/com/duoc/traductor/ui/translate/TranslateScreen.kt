package com.duoc.traductor.ui.translate

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.traductor.native.HapticHelper
import com.duoc.traductor.native.SpeechHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslateScreen(
    onHistory: () -> Unit,
    onLogout: () -> Unit,
    vm: TranslateViewModel = viewModel()
) {
    val s by vm.state.collectAsState()
    val context = LocalContext.current
    val haptic = remember { HapticHelper(context) }
    val speech = remember { SpeechHelper(context) }
    var listening by remember { mutableStateOf(false) }
    var micMessage by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) { onDispose { speech.destroy() } }

    fun startListening() {
        if (!speech.isAvailable()) {
            micMessage = "El reconocimiento de voz no está disponible en este dispositivo"
            return
        }
        listening = true
        micMessage = null
        speech.start(
            languageTag = if (s.trToEs) "tr-TR" else "es-ES",
            onText = { vm.onInputChange(it) },
            onFail = { micMessage = it; haptic.error() },
            onFinish = { listening = false }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) startListening()
        else micMessage = "Permiso de micrófono denegado. Actívalo en Ajustes para dictar."
    }

    fun onMicClick() {
        if (listening) {
            speech.destroy(); listening = false; return
        }
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) startListening() else permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    LaunchedEffect(s.output) { if (s.output.isNotBlank()) haptic.success() }
    LaunchedEffect(s.error) { if (s.error != null) haptic.error() }

    val rotation by animateFloatAsState(if (s.trToEs) 0f else 180f, label = "swap")
    val micColor by animateColorAsState(
        if (listening) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        label = "mic"
    )
    val from = if (s.trToEs) "Turco" else "Español"
    val to = if (s.trToEs) "Español" else "Turco"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Traductor") },
                actions = {
                    IconButton(onClick = onHistory) {
                        Icon(Icons.Default.History, contentDescription = "Historial")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(from, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
                IconButton(onClick = vm::swap) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = "Intercambiar idiomas", modifier = Modifier.rotate(rotation))
                }
                Text(to, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
            }

            OutlinedTextField(
                value = s.input,
                onValueChange = vm::onInputChange,
                label = { Text("Texto en $from") },
                leadingIcon = { Icon(Icons.Default.Translate, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = ::onMicClick) {
                        Icon(
                            if (listening) Icons.Default.Stop else Icons.Default.Mic,
                            contentDescription = "Dictar", tint = micColor
                        )
                    }
                },
                isError = s.inputError != null,
                supportingText = { Text(s.inputError ?: "${s.input.length}/500") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            micMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Button(onClick = vm::translate, enabled = !s.loading, modifier = Modifier.fillMaxWidth()) {
                if (s.loading) CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                else Text("Traducir")
            }

            AnimatedVisibility(s.error != null) {
                Text(s.error ?: "", color = MaterialTheme.colorScheme.error)
            }

            AnimatedVisibility(s.output.isNotBlank()) {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(to, style = MaterialTheme.typography.labelMedium)
                        Text(s.output, style = MaterialTheme.typography.headlineSmall)
                        Text("Guardado en el historial", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
