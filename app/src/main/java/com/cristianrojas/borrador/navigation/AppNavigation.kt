package com.cristianrojas.borrador.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ui.screens.*
import viewmodel.ReservaViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val context = navController.context
    val reservaViewModel: ReservaViewModel = viewModel(
        factory = ReservaViewModel.provideFactory(context)
    )

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") { HomeScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("menu") { MenuScreen() }
        composable("carrito") { CarritoScreen(navController) }
        composable("ordenar") { OrdenarScreen(navController) }
        composable("reserva") { ReservaScreen(navController, reservaViewModel) }
        composable("reserva_form") { ReservaFormScreen(navController, reservaViewModel) }
        composable("roles") { ManejoRolesScreen(navController) }
        composable("pago") { PantallaPago(navController) }

    }
}