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
import com.ebc.catalogofundas.viewmodel.CatalogoViewModel
import com.ebc.catalogofundas.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CatalogoFundasApp()   // ← AQUÍ SE PINTA TODA LA APP
        }
    }
}

@Composable
fun CatalogoFundasApp() {
    val navController = rememberNavController()
    val loginVM: LoginViewModel = viewModel()
    val catalogoVM: CatalogoViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(loginVM) {
                navController.navigate("catalogo")
            }
        }

        composable("catalogo") {
            CatalogoScreen(
                catalogoViewModel = catalogoVM,
                onIrPerfil = { navController.navigate("perfil") },
                onIrDetalle = { id -> navController.navigate("detalle/$id") }
            )
        }

        composable("perfil") {
            PerfilScreen(
                catalogoViewModel = catalogoVM,
                onVolver = { navController.popBackStack() }
            )
        }

        composable("detalle/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
            DetalleScreen(
                idFunda = id,
                catalogoViewModel = catalogoVM,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
