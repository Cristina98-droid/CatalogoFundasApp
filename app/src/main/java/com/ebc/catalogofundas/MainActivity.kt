package com.ebc.catalogofundas


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ebc.catalogofundas.view.CatalogoScreen
import com.ebc.catalogofundas.view.DetalleScreen
import com.ebc.catalogofundas.view.LoginScreen
import com.ebc.catalogofundas.view.PerfilScreen
import com.ebc.catalogofundas.view.RegistroScreen
import com.ebc.catalogofundas.viewmodel.CatalogoViewModel
import com.ebc.catalogofundas.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CatalogoFundasApp()
        }
    }
}

@Composable
fun CatalogoFundasApp() {
    val navController = rememberNavController()
    val loginVM: LoginViewModel = viewModel()
    val catalogoVM: CatalogoViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // LOGIN
        composable("login") {
            LoginScreen(
                viewModel = loginVM,
                onLoginExitoso = {
                    navController.navigate("catalogo") {
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
                    navController.popBackStack() // vuelve a "login"
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



