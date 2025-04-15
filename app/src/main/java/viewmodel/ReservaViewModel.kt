package viewmodel

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import model.Reserva
import repository.ReservaRepository

class ReservaViewModel(
    private val repository: ReservaRepository
) : ViewModel() {

    var reservas = mutableStateListOf<Reserva>()
        private set

    var reservaActual by mutableStateOf(
        Reserva(0, 1, "", "", "")
    )

    var mensajeError by mutableStateOf("")
        private set

    fun cargarReservas() {
        reservas.clear()
        reservas.addAll(repository.obtenerTodas())
    }

    fun cargarReservaPorId(id: Int) {
        reservaActual = repository.obtenerPorId(id) ?: Reserva(0, 1, "", "", "")
    }

    fun guardarReserva(): Boolean {
        if (validarReserva()) {
            if (reservaActual.id == 0) {
                repository.insertar(reservaActual)
            } else {
                repository.actualizar(reservaActual)
            }
            cargarReservas()
            return true
        }
        return false
    }

    fun eliminarReserva(id: Int) {
        repository.eliminar(id)
        cargarReservas()
    }

    private fun validarReserva(): Boolean {
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
        val fechaIngresada = try {
            formatter.parse(reservaActual.fechaHora)
        } catch (e: Exception) {
            mensajeError = "Fecha inválida. Usa el formato correcto: yyyy-MM-dd HH:mm"
            return false
        }

        if (reservaActual.numeroPersonas !in 1..30) {
            mensajeError = "El número de personas debe estar entre 1 y 30."
            return false
        }
        if (reservaActual.nombreCliente.isBlank()) {
            mensajeError = "El nombre del cliente es obligatorio."
            return false
        }
        if (reservaActual.telefono.isBlank()) {
            mensajeError = "El teléfono es obligatorio."
            return false
        }
        if (fechaIngresada.before(java.util.Date())) {
            mensajeError = "No puedes reservar en una fecha pasada."
            return false
        }

        mensajeError = ""
        return true
    }

    fun limpiarFormulario() {
        reservaActual = Reserva(0, 1, "", "", "")
        mensajeError = ""
    }
    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ReservaViewModel(ReservaRepository(context)) as T
                }
            }
        }
    }
}