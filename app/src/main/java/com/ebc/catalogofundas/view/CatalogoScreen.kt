package com.ebc.catalogofundas.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ebc.catalogofundas.model.Funda
import com.ebc.catalogofundas.viewmodel.CatalogoViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    catalogoViewModel: CatalogoViewModel,
    onIrPerfil: () -> Unit,
    onIrDetalle: (Int) -> Unit
) {
    val fundas = catalogoViewModel.fundasLiveData.observeAsState(initial = emptyList())
    val termino = catalogoViewModel.terminoBusqueda.observeAsState(initial = "")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Fundas") },
                actions = {
                    IconButton(onClick = onIrPerfil) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            OutlinedTextField(
                value = termino.value,
                onValueChange = { catalogoViewModel.filtrarFundas(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar por estilo, color o modelo...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(fundas.value) { funda ->
                    FundaItem(
                        funda = funda,
                        onClick = { onIrDetalle(funda.id) } // <- ENVÍA ID a la ruta detalle/{id}
                    )
                }
            }
        }
    }
}

@Composable
private fun FundaItem(
    funda: Funda,
    onClick: () -> Unit
) {
    val currency = NumberFormat.getCurrencyInstance(Locale("es", "MX"))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = funda.imagenResId),
                contentDescription = funda.nombre,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(funda.nombre, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Precio: ${currency.format(funda.precio)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

