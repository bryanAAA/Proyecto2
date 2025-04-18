package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.widget.Toast
import com.cristianrojas.borrador.R
import utils.CartManager

@Composable
fun PantallaPago(navController: NavController) {
    val carrito = CartManager.getCart()
    val subtotal = carrito.entries.sumOf {
        val precio = it.value * extraerPrecio(it.key)
        precio
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("Total a pagar:", fontSize = 18.sp)
                Text("₡$subtotal", style = MaterialTheme.typography.titleLarge)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        var nombreTarjeta by remember { mutableStateOf("") }
        var numeroTarjeta by remember { mutableStateOf("") }
        var fechaExpiracion by remember { mutableStateOf("") }
        var cvv by remember { mutableStateOf("") }

        OutlinedTextField(
            value = nombreTarjeta,
            onValueChange = { nombreTarjeta = it },
            label = { Text("Nombre en la Tarjeta") },
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "Nombre")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = numeroTarjeta,
            onValueChange = { numeroTarjeta = it },
            label = { Text("Número de Tarjeta") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.payments_24),
                    contentDescription = "Número de tarjeta"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = fechaExpiracion,
            onValueChange = { fechaExpiracion = it },
            label = { Text("Fecha de Expiración (MM/YY)") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.calendar_month_24),
                    contentDescription = "Fecha de expiración"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = cvv,
            onValueChange = { cvv = it },
            label = { Text("CVV") },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = "CVV")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(24.dp))

        val regexTarjeta = Regex("^\\d{16}$")
        val regexFecha = Regex("^(0[1-9]|1[0-2])/\\d{2}$")
        val regexCVV = Regex("^\\d{3}$")

        val camposCompletos = nombreTarjeta.isNotBlank() &&
                numeroTarjeta.isNotBlank() &&
                fechaExpiracion.isNotBlank() &&
                cvv.isNotBlank()

        val tarjetaValida = regexTarjeta.matches(numeroTarjeta)
        val cvvValido = regexCVV.matches(cvv)
        val fechaValida = regexFecha.matches(fechaExpiracion)

        val fechaNoVencida = if (fechaValida) {
            val parts = fechaExpiracion.split("/")
            val mes = parts[0].toInt()
            val anio = "20${parts[1]}".toInt()

            val hoy = java.util.Calendar.getInstance()
            val mesActual = hoy.get(java.util.Calendar.MONTH) + 1
            val anioActual = hoy.get(java.util.Calendar.YEAR)

            anio > anioActual || (anio == anioActual && mes >= mesActual)
        } else false

        val puedePagar = camposCompletos && tarjetaValida && cvvValido && fechaNoVencida

        Button(
            onClick = {
                if (!camposCompletos) {
                    Toast.makeText(
                        navController.context,
                        "Por favor, complete todos los campos.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!tarjetaValida) {
                    Toast.makeText(
                        navController.context,
                        "El número de tarjeta debe tener 16 dígitos.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!cvvValido) {
                    Toast.makeText(
                        navController.context,
                        "El CVV debe tener 3 dígitos.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!fechaValida) {
                    Toast.makeText(
                        navController.context,
                        "Formato de fecha inválido. Usa MM/YY.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!fechaNoVencida) {
                    Toast.makeText(
                        navController.context,
                        "La tarjeta está vencida.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        navController.context,
                        "Procesando pago...",
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.navigate("factura/${nombreTarjeta.trim()}/${numeroTarjeta.trim()}")
                }
            },
            enabled = puedePagar,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Realizar Orden")
        }
    }
}