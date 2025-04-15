package repository

import android.content.Context
import Data.ReservaDao
import model.Reserva

class ReservaRepository(private val context: Context) {

    private val dao = ReservaDao(context)

    fun obtenerTodas(): List<Reserva> {
        return dao.obtenerTodas()
    }

    fun obtenerPorId(id: Int): Reserva? {
        return dao.obtenerPorId(id)
    }

    fun insertar(reserva: Reserva) {
        if (!validarReserva(reserva)) return
        dao.insertar(reserva)
    }

    fun actualizar(reserva: Reserva) {
        if (!validarReserva(reserva)) return
        dao.actualizar(reserva)
    }

    fun eliminar(id: Int) {
        dao.eliminar(id)
    }

    fun obtenerPersonasReservadasPorHora(fechaHora: String): Int {
        return dao.obtenerPersonasPorHora(fechaHora)
    }

    private fun validarReserva(reserva: Reserva): Boolean {
        if (!fechaEnHoraExacta(reserva.fechaHora)) return false
        if (!fechaEnFuturo(reserva.fechaHora)) return false
        if (reserva.numeroPersonas !in 1..30) return false

        val personasExistentes = dao.obtenerPersonasPorHora(reserva.fechaHora)
        if (personasExistentes + reserva.numeroPersonas > 30) return false

        return true
    }

    private fun fechaEnHoraExacta(fecha: String): Boolean {
        return fecha.endsWith(":00")
    }

    private fun fechaEnFuturo(fecha: String): Boolean {
        val now = System.currentTimeMillis()
        val fechaLong = ReservaDao.parseFecha(fecha) ?: return false
        return fechaLong > now
    }
}