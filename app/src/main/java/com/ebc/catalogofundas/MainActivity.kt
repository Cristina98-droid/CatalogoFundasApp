package com.ebc.catalogofundas

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ebc.catalogofundas.notifications.NotificationHelper
import com.ebc.catalogofundas.view.CatalogoScreen
import com.ebc.catalogofundas.view.DetalleScreen
import com.ebc.catalogofundas.view.LoginScreen
import com.ebc.catalogofundas.view.PerfilScreen
import com.ebc.catalogofundas.view.RegistroScreen
import com.ebc.catalogofundas.viewmodel.CatalogoViewModel
import com.ebc.catalogofundas.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Viene de NotificationHelper cuando el usuario toca la notificación
        val openFavorites = intent?.getBooleanExtra("openFavorites", false) ?: false

        setContent {
            CatalogoFundasApp(openFavorites = openFavorites)
        }
    }
}

@Composable
fun CatalogoFundasApp(openFavorites: Boolean) {
    val navController = rememberNavController()
    val loginVM: LoginViewModel = viewModel()
    val catalogoVM: CatalogoViewModel = viewModel()

    val context = LocalContext.current

    // 1) Crear canal de notificación (una vez)
    LaunchedEffect(Unit) {
        NotificationHelper.createChannel(context)
    }

    // 2) Pedir permiso de notificaciones (Android 13+)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { /* no necesitas hacer nada */ }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // 3) Escuchar eventos del ViewModel para mostrar notificación
    LaunchedEffect(Unit) {
        catalogoVM.uiEvents.collectLatest { event ->
            when (event) {
                is CatalogoViewModel.UiEvent.MostrarNotificacion -> {
                    NotificationHelper.showStyleNotification(
                        context = context,
                        openFavorites = event.openFavorites
                    )
                }
            }
        }
    }

    // Ruta a la que queremos ir cuando el usuario toque la notificación
    val routeAfterLogin = if (openFavorites) "perfil" else "catalogo"

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // LOGIN
        composable("login") {
            LoginScreen(
                viewModel = loginVM,
                onLoginExitoso = {
                    navController.navigate(routeAfterLogin) {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onCrearCuenta = {
                    navController.navigate("registro")
                }
            )
        }

        // REGISTRO
        composable("registro") {
            RegistroScreen(
                viewModel = loginVM,
                onRegistroExitoso = { navController.popBackStack() }, // vuelve a "login"
                onCancelar = { navController.popBackStack() }
            )
        }

        // CATÁLOGO
        composable("catalogo") {
            CatalogoScreen(
                catalogoViewModel = catalogoVM,
                onIrPerfil = { navController.navigate("perfil") },
                onIrDetalle = { id ->
                    navController.navigate("detalle/$id")
                }
            )
        }

        // PERFIL (tu pantalla donde normalmente están favoritos)
        composable("perfil") {
            PerfilScreen(
                catalogoViewModel = catalogoVM,
                onVolver = { navController.popBackStack() }
            )
        }

        // DETALLE
        composable("detalle/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            DetalleScreen(
                idFunda = id,
                catalogoViewModel = catalogoVM,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}






