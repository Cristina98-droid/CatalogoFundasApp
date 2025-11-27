package com.ebc.catalogofundas.viewmodel


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.ebc.catalogofundas.R
import com.ebc.catalogofundas.model.Funda

class CatalogoViewModel : ViewModel() {

    val fundas = mutableStateListOf<Funda>()

    init {
        cargarFundas()
    }

    private fun cargarFundas() {
        fundas.addAll(
            listOf(
                Funda(1, "Funda Floral", "Diseño floral elegante", 150.0, R.drawable.funda_floral),
                Funda(2, "Funda Anime", "Personaje anime popular", 180.0, R.drawable.funda_anime),
            )
        )
    }

    fun obtenerFundaPorId(id: Int): Funda? {
        return fundas.find { it.id == id }
    }

    fun toggleFavorita(id: Int) {
        val funda = obtenerFundaPorId(id)
        funda?.let {
            it.esFavorita = !it.esFavorita
        }
    }
    fun obtenerFavoritas(): List<Funda> {
        return fundas.filter { it.esFavorita }
    }
}
