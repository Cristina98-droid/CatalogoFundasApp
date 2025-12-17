package com.ebc.catalogofundas.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ebc.catalogofundas.viewmodel.CatalogoViewModel
import java.net.URLEncoder
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    idFunda: Int,
    catalogoViewModel: CatalogoViewModel,
    onVolver: () -> Unit
) {
    val context = LocalContext.current
    val item = catalogoViewModel.obtenerFundaPorId(idFunda)
    val usuario = catalogoViewModel.usuario

    var esFavorita by remember { mutableStateOf(catalogoViewModel.esFavorita(idFunda)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        if (item == null) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("No se encontró la funda.")
            }
            return@Scaffold
        }

        val currency = NumberFormat.getCurrencyInstance(Locale("es", "MX"))

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = item.imagenResId),
                    contentDescription = item.nombre,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }



            Spacer(modifier = Modifier.height(14.dp))

            Text(item.nombre, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Precio: ${currency.format(item.precio)}", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    catalogoViewModel.toggleFavorito(idFunda)
                    esFavorita = catalogoViewModel.esFavorita(idFunda)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = if (esFavorita) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorito"
                )
                Spacer(Modifier.width(8.dp))
                Text(if (esFavorita) "Quitar de favoritos" else "Agregar a favoritos")
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = {
                    val telefonoWhatsApp = "5215512345678" // CAMBIA A TU NÚMERO (México: 521 + 10 dígitos)

                    val mensaje = """
                        Hola, me interesa esta funda:
                        ✅ Diseño: ${item.nombre}
                        💰 Precio: ${currency.format(item.precio)}
                        📱 Mi teléfono: ${usuario.modeloTelefono}
                    """.trimIndent()

                    val texto = URLEncoder.encode(mensaje, "UTF-8")
                    val url = "https://wa.me/$telefonoWhatsApp?text=$texto"

                    try {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    } catch (_: ActivityNotFoundException) {
                        // Si no hay WhatsApp, abre navegador igual (o podrías mostrar un mensaje)
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Contactar por WhatsApp")
            }
        }
    }
}



