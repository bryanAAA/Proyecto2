package model

data class Reserva(
    val id: Int = 0,
    val numeroPersonas: Int,
    val nombreCliente: String,
    val telefono: String,
    val fechaHora: String
)
