package com.ebc.catalogofundas.view

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ebc.catalogofundas.viewmodel.CatalogoViewModel

@Composable
fun DetalleScreen(
    idFunda: Int,
    catalogoViewModel: CatalogoViewModel,
    onVolver: () -> Unit
) {
    val context = LocalContext.current
    val funda = catalogoViewModel.obtenerFundaPorId(idFunda)

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    if (funda == null) return

    Column(Modifier.padding(16.dp)) {

        TextButton(onClick = onVolver) { Text("< Volver") }

        AnimatedVisibility(visible) {
            Column {
                Image(
                    painter = painterResource(funda.imagenResId),
                    contentDescription = funda.titulo,
                    modifier = Modifier.fillMaxWidth().height(260.dp)
                )
                Text(funda.titulo)
                Text(funda.descripcion)
                Text("$${funda.precio}")

                Spacer(Modifier.height(16.dp))

                Button(onClick = { catalogoViewModel.toggleFavorita(funda.id) }) {
                    Text(if (funda.esFavorita) "Quitar de favoritos" else "Agregar a favoritos")
                }

                Button(onClick = {
                    val mensaje = Uri.encode("Me interesa la funda ${funda.titulo}")
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/?text=$mensaje"))
                    context.startActivity(intent)
                }) {
                    Text("WhatsApp")
                }
            }
        }
    }
}
