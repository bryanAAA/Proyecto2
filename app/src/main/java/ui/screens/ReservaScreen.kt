package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import utils.SessionManager
import viewmodel.ReservaViewModel

@Composable
fun ReservaScreen(navController: NavController, viewModel: ReservaViewModel) {

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
        viewModel.cargarReservas()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Listado de Reservas", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                viewModel.limpiarFormulario()
                navController.navigate("reserva_form")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("➕ Nueva Reserva")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(viewModel.reservas) { reserva ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Cliente: ${reserva.nombreCliente}")
                        Text("Teléfono: ${reserva.telefono}")
                        Text("Personas: ${reserva.numeroPersonas}")
                        Text("Fecha: ${reserva.fechaHora}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    viewModel.cargarReservaPorId(reserva.id)
                                    navController.navigate("reserva_form")
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("✏️ Editar")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    // Aquí podrías abrir un diálogo si quieres confirmar
                                    viewModel.eliminarReserva(reserva.id)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("🗑️ Eliminar")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    // Solo muestra en consola los detalles por ahora
                                    println("Reserva: $reserva")
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("🔍 Detalles")
                            }
                        }
                    }
                }
            }
        }
    }
}