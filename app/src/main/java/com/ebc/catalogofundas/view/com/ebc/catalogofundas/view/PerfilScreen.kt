package com.ebc.catalogofundas.view

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.ebc.catalogofundas.utils.SessionManager
import com.ebc.catalogofundas.viewmodel.CatalogoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    catalogoViewModel: CatalogoViewModel,
    onVolver: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val context = LocalContext.current

    // Usuario del ViewModel (se usa sin .value)
    val usuario = catalogoViewModel.usuario

    // Lista de favoritas desde el ViewModel
    val favoritas = catalogoViewModel.obtenerFavoritas()

    var showEditDialog by remember { mutableStateOf(false) }

    // Selector de imagen
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { catalogoViewModel.actualizarFotoPerfil(it.toString()) }
    }

    // Convertimos URI a Bitmap para mostrar la foto
    val fotoBitmap: Bitmap? = remember(usuario.fotoUri) {
        val uriString = usuario.fotoUri ?: return@remember null
        runCatching {
            val uri = uriString.toUri()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        }.getOrNull()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar perfil")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Foto / icono
            if (fotoBitmap != null) {
                Image(
                    bitmap = fotoBitmap.asImageBitmap(),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Icono de perfil",
                        modifier = Modifier.size(110.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            TextButton(onClick = { pickImageLauncher.launch("image/*") }) {
                Text(if (usuario.fotoUri == null) "Agregar foto" else "Cambiar foto")
            }

            Spacer(Modifier.height(8.dp))

            Text(usuario.nombre, style = MaterialTheme.typography.titleMedium)
            Text("Modelo: ${usuario.modeloTelefono}", style = MaterialTheme.typography.bodyMedium)

            // ✅ Botón cerrar sesión
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    SessionManager.logout(context)
                    onCerrarSesion()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Mis favoritos",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp)
            )

            if (favoritas.isEmpty()) {
                Text(
                    text = "Aún no tienes fundas en favoritos.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Start)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(favoritas) { funda ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = funda.imagenResId),
                                    contentDescription = funda.nombre,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = funda.nombre,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text(
                                        text = "$${funda.precio}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showEditDialog) {
            EditProfileDialog(
                nombreActual = usuario.nombre,
                modeloActual = usuario.modeloTelefono,
                onDismiss = { showEditDialog = false },
                onSave = { nuevoNombre, nuevoModelo ->
                    catalogoViewModel.actualizarPerfil(nuevoNombre, nuevoModelo)
                    showEditDialog = false
                }
            )
        }
    }
}

@Composable
private fun EditProfileDialog(
    nombreActual: String,
    modeloActual: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var nombre by remember { mutableStateOf(nombreActual) }
    var modelo by remember { mutableStateOf(modeloActual) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar perfil") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = modelo,
                    onValueChange = { modelo = it },
                    label = { Text("Modelo de teléfono") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(nombre, modelo) }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}



