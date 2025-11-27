package com.ebc.catalogofundas.model

data class Funda(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val precio: Double,
    val imagenResId: Int,
    var esFavorita: Boolean = false
)