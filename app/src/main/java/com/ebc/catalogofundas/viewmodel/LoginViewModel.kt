package com.ebc.catalogofundas.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    val loginExitoso = MutableLiveData<Boolean>()

    fun iniciarSesion(correo: String, password: String) {
        loginExitoso.value = correo.isNotEmpty() && password.isNotEmpty()
    }
}
