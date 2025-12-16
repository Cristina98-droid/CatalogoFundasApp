package com.ebc.catalogofundas.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    val loginExitoso = MutableLiveData<Boolean>()
    val registroExitoso = MutableLiveData<Boolean>()

    // Aquí guardamos al usuario "registrado" en memoria
    private var correoRegistrado: String? = null
    private var passwordRegistrado: String? = null

    fun registrar(correo: String, password: String) {
        // Validación básica
        if (correo.isNotEmpty() && password.isNotEmpty()) {
            correoRegistrado = correo
            passwordRegistrado = password
            registroExitoso.value = true
        } else {
            registroExitoso.value = false
        }
    }

    fun iniciarSesion(correo: String, password: String) {
        // Solo deja entrar si coincide con lo que se registró
        val ok = correo.isNotEmpty()
                && password.isNotEmpty()
                && correo == correoRegistrado
                && password == passwordRegistrado

        loginExitoso.value = ok
    }
}

