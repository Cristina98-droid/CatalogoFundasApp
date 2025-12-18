package com.ebc.catalogofundas.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ebc.catalogofundas.viewmodel.CatalogoViewModel
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
    val funda = catalogoViewModel.obtenerFundaPorId(idFunda)

    if (funda == null) {
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No se encontró el producto.")
            }
        }
        return
    }

    val esFav = catalogoViewModel.esFavorita(funda.id)
    val precioFormateado = remember(funda.precio) {
        NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(funda.precio)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { catalogoViewModel.toggleFavorito(funda.id) }) {
                        Icon(
                            imageVector = if (esFav) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorito"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Image(
                painter = painterResource(id = funda.imagenResId),
                contentDescription = funda.nombre,
                modifier = Modifier
                    .height(150.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )


            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = funda.nombre,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = funda.descripcion,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Precio: $precioFormateado",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { catalogoViewModel.toggleFavorito(funda.id) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (esFav) "Quitar favorito" else "Agregar favorito")
                }

                OutlinedButton(
                    onClick = {
                        val mensaje = """
                            Hola 😊
                            Me interesa esta funda:
                            • ${funda.nombre}
                            • $precioFormateado
                            ¿Me ayudas a realizar mi pedido?
                        """.trimIndent()

                        val uri = Uri.parse("https://wa.me/?text=${Uri.encode(mensaje)}")
                        val intent = Intent(Intent.ACTION_VIEW, uri)

                        try {
                            context.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            val fallback = Intent(Intent.ACTION_VIEW, Uri.parse("https://web.whatsapp.com/"))
                            context.startActivity(fallback)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("WhatsApp")
                }
            }
        }
    }
}






