package com.ebc.catalogofundas.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import com.ebc.catalogofundas.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginExitoso: () -> Unit,
    onCrearCuenta: () -> Unit = {}
) {
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mostrarError by remember { mutableStateOf(false) }

    // ANIMACIÓN TÍTULO
    var startAnim by remember { mutableStateOf(false) }
    val escala by animateFloatAsState(
        targetValue = if (startAnim) 1f else 0.8f,
        animationSpec = tween(600),
        label = ""
    )
    LaunchedEffect(Unit) { startAnim = true }


    val loginExitoso by viewModel.loginExitoso.observeAsState()

    LaunchedEffect(loginExitoso) {
        if (loginExitoso == true) {
            onLoginExitoso()
        } else if (loginExitoso == false) {
            mostrarError = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Catálogo de Fundas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.scale(escala)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // BOTÓN INICIAR SESIÓN
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                mostrarError = false
                viewModel.iniciarSesion(correo, password)
            }
        ) {
            Text("Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(8.dp))

        //  BOTÓN DE CREAR CUENTA
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onCrearCuenta() }
        ) {
            Text("Crear cuenta")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (mostrarError) {
            Text(
                text = "Correo o contraseña incorrectos.",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}



