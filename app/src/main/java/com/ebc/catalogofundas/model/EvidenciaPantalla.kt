data class EvidenciaPantalla(
    val idEvidencia: Long = 0L,
    val nombrePantalla: String,
    val descripcion: String,
    val rutaImagen: String,
    val animacion: String? = null,
    val fecha: Long = System.currentTimeMillis()
)

