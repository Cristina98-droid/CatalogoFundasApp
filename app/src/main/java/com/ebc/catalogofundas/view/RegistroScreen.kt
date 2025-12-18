package com.ebc.catalogofundas.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import com.ebc.catalogofundas.viewmodel.LoginViewModel

@Composable
fun RegistroScreen(
    viewModel: LoginViewModel = viewModel(),
    onRegistroExitoso: () -> Unit,
    onCancelar: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmarPassword by remember { mutableStateOf("") }
    var mostrarError by remember { mutableStateOf<String?>(null) }

    val registroExitoso by viewModel.registroExitoso.observeAsState()


    LaunchedEffect(registroExitoso) {
        if (registroExitoso == true) {
            onRegistroExitoso()
        } else if (registroExitoso == false) {
            mostrarError = "Revisa los datos ingresados."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmarPassword,
            onValueChange = { confirmarPassword = it },
            label = { Text("Confirmar contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                mostrarError = null

                if (password != confirmarPassword) {
                    mostrarError = "Las contraseñas no coinciden."
                } else {
                    viewModel.registrar(correo, password)
                }
            }
        ) {
            Text("Registrar")
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onCancelar() }
        ) {
            Text("Cancelar")
        }

        if (mostrarError != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = mostrarError!!,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
