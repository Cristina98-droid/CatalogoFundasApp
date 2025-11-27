package com.ebc.catalogofundas.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ebc.catalogofundas.viewmodel.CatalogoViewModel

@Composable
fun PerfilScreen(
    catalogoViewModel: CatalogoViewModel,
    onVolver: () -> Unit
) {

    val favoritas = catalogoViewModel.obtenerFavoritas()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        TextButton(onClick = onVolver) { Text("< Volver") }

        Text("Usuario Demo")
        Text("Modelo: iPhone 14")

        LazyColumn {
            items(favoritas) { funda ->
                Row(Modifier.padding(8.dp)) {
                    Image(
                        painter = painterResource(funda.imagenResId),
                        contentDescription = funda.titulo,
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(funda.titulo)
                        Text("$${funda.precio}")
                    }
                }
            }
        }
    }
}






