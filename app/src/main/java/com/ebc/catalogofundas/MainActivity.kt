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
import com.ebc.catalogofundas.utils.SessionManager
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

        // Extra cuando viene de la notificación
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

    //  Sesión guardada
    val isLoggedIn = SessionManager.isLoggedIn(context)

    // 1️⃣ Crear canal de notificación
    LaunchedEffect(Unit) {
        NotificationHelper.createChannel(context)
    }

    // 2️⃣ Permiso POST_NOTIFICATIONS (Android 13+)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { }
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

    // 3️⃣ Escuchar eventos del ViewModel (notificación)
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

    // Ruta después del login o notificación
    val routeAfterLogin = if (openFavorites) "perfil" else "catalogo"


    val startDestination = if (isLoggedIn) routeAfterLogin else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // LOGIN
        composable("login") {
            LoginScreen(
                viewModel = loginVM,
                onLoginExitoso = {
                    SessionManager.setLoggedIn(context, true)

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
                onRegistroExitoso = {
                    SessionManager.setLoggedIn(context, true)

                    navController.navigate("catalogo") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onCancelar = {
                    navController.popBackStack()
                }
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

        // PERFIL
        composable("perfil") {
            PerfilScreen(
                catalogoViewModel = catalogoVM,
                onVolver = { navController.popBackStack() },
                onCerrarSesion = {
                    SessionManager.logout(context)
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
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







