package com.ebc.catalogofundas.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebc.catalogofundas.R
import com.ebc.catalogofundas.model.Funda
import com.ebc.catalogofundas.model.Usuario
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CatalogoViewModel : ViewModel() {

    private val fundasOriginales: List<Funda> = listOf(
        Funda(1, "Funda Anime", "Funda con diseño anime, resistente y de alta calidad.", 150.0, R.drawable.funda_anime),
        Funda(2, "Funda Floral", "Funda con diseño floral, ideal para un estilo elegante.", 149.0, R.drawable.funda_floral),
        Funda(3, "Funda Floral Rosa", "Funda floral en tonos rosa, diseño delicado y moderno.", 150.0, R.drawable.funda_floral),
        Funda(4, "Funda Anime Neon", "Funda anime con colores neón y acabado brillante.", 150.0, R.drawable.funda_anime),
        Funda(5, "Funda Minimal", "Funda minimalista, diseño limpio y protección básica.", 150.0, R.drawable.funda_anime)
    )

    // ===============================
    // Eventos para la UI (notificaciones, etc.)
    // ===============================
    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    sealed class UiEvent {
        data class MostrarNotificacion(val openFavorites: Boolean) : UiEvent()
    }

    // ===============================
    // LiveData del catálogo
    // ===============================
    private val _fundasLiveData = MutableLiveData<List<Funda>>(fundasOriginales)
    val fundasLiveData: LiveData<List<Funda>> = _fundasLiveData

    private val _terminoBusqueda = MutableLiveData("")
    val terminoBusqueda: LiveData<String> = _terminoBusqueda

    fun filtrarFundas(texto: String) {
        _terminoBusqueda.value = texto
        val query = texto.trim().lowercase()

        _fundasLiveData.value =
            if (query.isBlank()) fundasOriginales
            else fundasOriginales.filter { it.nombre.lowercase().contains(query) }
    }

    fun obtenerFundaPorId(id: Int): Funda? =
        fundasOriginales.firstOrNull { it.id == id }

    // ===============================
    // Usuario (Perfil)
    // ===============================
    private var _usuario by mutableStateOf(
        Usuario(
            nombre = "Usuario",
            modeloTelefono = "iPhone / Android",
            fotoUri = null
        )
    )

    val usuario: Usuario
        get() = _usuario

    fun actualizarPerfil(nombre: String, modelo: String) {
        _usuario = _usuario.copy(
            nombre = nombre,
            modeloTelefono = modelo
        )
    }

    fun actualizarFotoPerfil(uri: String) {
        _usuario = _usuario.copy(fotoUri = uri)
    }

    // ===============================
    // Favoritos
    // ===============================
    private val favoritosIds = mutableSetOf<Int>()

    fun toggleFavorito(idFunda: Int) {
        val estabaVacio = favoritosIds.isEmpty()

        if (favoritosIds.contains(idFunda)) {
            favoritosIds.remove(idFunda)
        } else {
            favoritosIds.add(idFunda)

            // ✅ Notificación SOLO cuando agrega a favoritos por primera vez
            if (estabaVacio) {
                viewModelScope.launch {
                    _uiEvents.emit(UiEvent.MostrarNotificacion(openFavorites = true))
                }
            }
        }
    }

    fun esFavorita(idFunda: Int): Boolean =
        favoritosIds.contains(idFunda)

    fun obtenerFavoritas(): List<Funda> =
        fundasOriginales.filter { favoritosIds.contains(it.id) }
}












