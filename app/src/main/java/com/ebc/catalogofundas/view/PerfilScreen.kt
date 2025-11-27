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
import androidx.compose.ui.Alignment
import com.ebc.catalogofundas.R


@Composable
fun PerfilScreen(
    catalogoViewModel: CatalogoViewModel,
    onVolver: () -> Unit
) {

    val favoritas = catalogoViewModel.obtenerFavoritas()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextButton(onClick = onVolver) { Text("< Volver") }

        Image(
            painter = painterResource(R.drawable.funda_floral),
            contentDescription = "Foto",
            modifier = Modifier
                .size(560.dp)
                .padding(bottom = 16.dp)
        )

        Text("Usuario Cristina")
        Text("Modelo: iPhone 15 Pro Max")

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






