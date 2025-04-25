package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import utils.SessionManager

@Composable
fun ManejoRolesScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        // Example: Load session or user data
        val user = SessionManager.currentUser
        if (user == null) {
            // Navigate to login if no session
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        } else {
            println("User is logged in: ${user.nombre}")
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Pantalla de Manejo de Roles", fontSize = 24.sp)
    }
}