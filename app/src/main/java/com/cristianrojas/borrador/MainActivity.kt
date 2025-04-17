package com.cristianrojas.borrador

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.cristianrojas.borrador.navigation.AppNavigation
import com.cristianrojas.borrador.ui.theme.BorradorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BorradorTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopNavigationBar(navController) },
                    bottomBar = { BottomAuthBar(navController) }
                ) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(navController: androidx.navigation.NavController) {
    TopAppBar(
        title = { Text("") },
        actions = {
            TextButton(onClick = { navController.navigate("home") }) { Text("Inicio") }
            TextButton(onClick = { navController.navigate("menu") }) { Text("Menú") }
            TextButton(onClick = { navController.navigate("ordenar") }) { Text("Ordenar") }
            TextButton(onClick = { navController.navigate("reserva") }) { Text("Reservar") }
            TextButton(onClick = { navController.navigate("roles") }) { Text("Manejo Roles") }
        }
    )
}

@Composable
fun BottomAuthBar(navController: androidx.navigation.NavController) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("register") },
            label = { Text("Registrar") },
            icon = {}
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("login") },
            label = { Text("Login") },
            icon = {}
        )
    }
}