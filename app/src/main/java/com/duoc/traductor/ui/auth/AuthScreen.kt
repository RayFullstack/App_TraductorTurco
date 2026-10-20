package com.duoc.traductor.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.traductor.native.HapticHelper
import com.traductortuco.R

@Composable
fun AuthScreen(onLoggedIn: () -> Unit, vm: AuthViewModel = viewModel()) {
    val s by vm.state.collectAsState()
    val context = LocalContext.current
    val haptic = remember { HapticHelper(context) }

    LaunchedEffect(s.loggedIn) { if (s.loggedIn) onLoggedIn() }
    LaunchedEffect(s.errorCount) { if (s.errorCount > 0) haptic.error() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo_tuco),
            contentDescription = "Logo de Tuco",
            modifier = Modifier.size(96.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Tuco",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            "Traductor Turco ↔ Español",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            if (s.isRegister) "Crea tu cuenta" else "Inicia sesión",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(24.dp))

        AnimatedVisibility(s.isRegister) {
            AuthField(s.name, vm::onNameChange, "Nombre", Icons.Default.Person, s.nameError)
        }
        AuthField(
            s.email, vm::onEmailChange, "Correo", Icons.Default.Email, s.emailError,
            keyboardType = KeyboardType.Email
        )
        PasswordField(s.password, vm::onPasswordChange, "Contraseña", s.passwordError)
        AnimatedVisibility(s.isRegister) {
            PasswordField(s.confirm, vm::onConfirmChange, "Confirmar contraseña", s.confirmError)
        }

        AnimatedVisibility(s.message != null) {
            val color by animateColorAsState(
                if (s.messageIsError) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.primary, label = "msgColor"
            )
            Row(
                Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    if (s.messageIsError) Icons.Default.Error else Icons.Default.CheckCircle,
                    contentDescription = null, tint = color
                )
                Spacer(Modifier.width(8.dp))
                Text(s.message ?: "", color = color)
            }
        }

        Spacer(Modifier.height(8.dp))
        Button(onClick = vm::submit, enabled = !s.loading, modifier = Modifier.fillMaxWidth()) {
            if (s.loading) CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
            else Text(if (s.isRegister) "Registrarme" else "Ingresar")
        }
        TextButton(onClick = vm::toggleMode) {
            Text(if (s.isRegister) "¿Ya tienes cuenta? Inicia sesión" else "¿No tienes cuenta? Regístrate")
        }
    }
}


@Composable
private fun AuthField(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    error: String?,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null) },
        trailingIcon = { if (error != null) Icon(Icons.Default.Error, contentDescription = "Error") },
        isError = error != null,
        supportingText = { error?.let { Text(it) } },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun PasswordField(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    error: String?
) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (visible) "Ocultar" else "Mostrar"
                )
            }
        },
        isError = error != null,
        supportingText = { error?.let { Text(it) } },
        singleLine = true,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth()
    )
}
