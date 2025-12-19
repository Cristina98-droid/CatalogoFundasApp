package com.ebc.catalogofundas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebc.catalogofundas.data.BanxicoService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TipoCambioViewModel : ViewModel() {

    private val _resultado = MutableStateFlow<String?>(null)
    val resultado: StateFlow<String?> = _resultado

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun consultar(token: String) {
        viewModelScope.launch {
            try {
                _cargando.value = true
                _error.value = null
                _resultado.value = BanxicoService.obtenerTipoCambio(token)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun limpiarMensajes() {
        _error.value = null
    }
}
