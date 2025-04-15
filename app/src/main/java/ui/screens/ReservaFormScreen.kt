package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import viewmodel.ReservaViewModel

@Composable
fun ReservaFormScreen(
    navController: NavController,
    viewModel: ReservaViewModel,
    isEditMode: Boolean = false
) {
    val reserva = viewModel.reservaActual
    val error = viewModel.mensajeError

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isEditMode) "Editar Reserva" else "Crear Reserva",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = reserva.numeroPersonas.toString(),
            onValueChange = {
                viewModel.reservaActual = reserva.copy(numeroPersonas = it.toIntOrNull() ?: 0)
            },
            label = { Text("Número de Personas") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = reserva.nombreCliente,
            onValueChange = {
                viewModel.reservaActual = reserva.copy(nombreCliente = it)
            },
            label = { Text("Nombre del Cliente") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = reserva.telefono,
            onValueChange = {
                viewModel.reservaActual = reserva.copy(telefono = it)
            },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = reserva.fechaHora,
            onValueChange = {
                viewModel.reservaActual = reserva.copy(fechaHora = it)
            },
            label = { Text("Fecha y Hora (yyyy-MM-dd HH:mm)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (error.isNotBlank()) {
            Text(text = error, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                val ok = viewModel.guardarReserva()
                if (ok) {
                    navController.navigate("reserva") // volver al listado
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isEditMode) "Guardar Cambios" else "Crear Reserva")
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = {
            navController.navigate("reserva")
        }) {
            Text("Cancelar y Volver")
        }
    }
}