package com.ebc.catalogofundas.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ebc.catalogofundas.viewmodel.CatalogoViewModel

@Composable
fun CatalogoScreen(
    catalogoViewModel: CatalogoViewModel,
    onIrPerfil: () -> Unit,
    onIrDetalle: (Int) -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Column(Modifier.fillMaxSize().padding(12.dp)) {

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Catálogo", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = onIrPerfil) { Text("Perfil") }
        }

        AnimatedVisibility(visible) {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(catalogoViewModel.fundas) { funda ->
                    Card(
                        Modifier
                            .padding(8.dp)
                            .clickable { onIrDetalle(funda.id) }
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            Image(
                                painter = painterResource(funda.imagenResId),
                                contentDescription = funda.titulo,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                            )
                            Text(funda.titulo)
                            Text("$${funda.precio}")
                        }
                    }
                }
            }
        }
    }
}
