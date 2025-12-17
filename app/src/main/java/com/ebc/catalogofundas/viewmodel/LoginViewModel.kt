package com.ebc.catalogofundas.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    // ===============================
    // Estados observables por la UI
    // ===============================
    private val _loginExitoso = MutableLiveData(false)
    val loginExitoso: LiveData<Boolean> = _loginExitoso

    private val _registroExitoso = MutableLiveData(false)
    val registroExitoso: LiveData<Boolean> = _registroExitoso


    // Usuario simulado en memoria

    private var correoRegistrado: String? = null
    private var passwordRegistrado: String? = null

    // ===============================
    // Registro de usuario
    // ===============================
    fun registrar(correo: String, password: String) {
        if (correo.isNotBlank() && password.isNotBlank()) {
            correoRegistrado = correo
            passwordRegistrado = password
            _registroExitoso.value = true
        } else {
            _registroExitoso.value = false
        }
    }


    // Inicio de sesión

    fun iniciarSesion(correo: String, password: String) {
        val esCorrecto =
            correo.isNotBlank() &&
                    password.isNotBlank() &&
                    correo == correoRegistrado &&
                    password == passwordRegistrado

        _loginExitoso.value = esCorrecto
    }


    fun limpiarEstados() {
        _loginExitoso.value = false
        _registroExitoso.value = false
    }
}


