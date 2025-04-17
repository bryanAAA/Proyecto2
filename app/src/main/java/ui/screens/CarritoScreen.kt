package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import utils.CartManager

@Composable
fun CarritoScreen(navController: NavController) {
    val carrito = CartManager.getCart()
    val subtotal = carrito.entries.sumOf {
        val precio = it.value * extraerPrecio(it.key)
        precio
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Resumen de la Orden",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (carrito.isEmpty()) {
            Text(
                text = "Tu carrito está vacío.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            carrito.entries.forEach { entry ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${entry.key} (x${entry.value})")
                    Text(text = "₡${extraerPrecio(entry.key) * entry.value}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Total: ₡$subtotal",
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para volver a ordenar
            Button(
                onClick = { navController.navigate("ordenar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text("Modificar orden")
            }

            // Botón para ir a pagos
            Button(
                onClick = {
                    navController.navigate("pago")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text("Proceder al pago")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "© 2025 BugFree Burgers - Todos los derechos reservados",
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

// Esta función calcula el precio basado en el nombre del producto
fun extraerPrecio(nombre: String): Int {
    return when (nombre) {
        "The Debugger" -> 7950
        "Infinite Loop" -> 6950
        "Null Pointer" -> 5950
        "Stack Overflow Burger" -> 8500
        "Code Review Burger" -> 7800
        "404 Not Found" -> 2500
        "Soft Reset" -> 5500
        "JavaScript Shake" -> 4550
        "Refactor Cola" -> 2800
        "Compile Coffee" -> 3200
        "Syntax S’mores" -> 2500
        "Bug-Free Brownie" -> 2750
        "Cookies Overflow" -> 2300
        "Runtime Cheesecake" -> 2900
        "Kernel Ice Cream" -> 2750
        else -> 0
    }
}
