package Data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import model.Reserva

class ReservaDao(private val context: Context) {

    private val dbHelper = AppDatabaseHelper(context)

    fun insertar(reserva: Reserva) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBContract.ReservaEntry.COLUMN_NUMERO_PERSONAS, reserva.numeroPersonas)
            put(DBContract.ReservaEntry.COLUMN_NOMBRE_CLIENTE, reserva.nombreCliente)
            put(DBContract.ReservaEntry.COLUMN_TELEFONO, reserva.telefono)
            put(DBContract.ReservaEntry.COLUMN_FECHA_HORA, reserva.fechaHora)
        }
        db.insert(DBContract.ReservaEntry.TABLE_NAME, null, values)
        db.close()
    }

    fun obtenerTodas(): List<Reserva> {
        val lista = mutableListOf<Reserva>()
        val db = dbHelper.readableDatabase

        val cursor: Cursor = db.query(
            DBContract.ReservaEntry.TABLE_NAME,
            null, null, null, null, null, null
        )

        with(cursor) {
            while (moveToNext()) {
                val reserva = Reserva(
                    id = getInt(getColumnIndexOrThrow(DBContract.ReservaEntry.COLUMN_ID)),
                    numeroPersonas = getInt(getColumnIndexOrThrow(DBContract.ReservaEntry.COLUMN_NUMERO_PERSONAS)),
                    nombreCliente = getString(getColumnIndexOrThrow(DBContract.ReservaEntry.COLUMN_NOMBRE_CLIENTE)),
                    telefono = getString(getColumnIndexOrThrow(DBContract.ReservaEntry.COLUMN_TELEFONO)),
                    fechaHora = getString(getColumnIndexOrThrow(DBContract.ReservaEntry.COLUMN_FECHA_HORA))
                )
                lista.add(reserva)
            }
        }
        cursor.close()
        db.close()
        return lista
    }

    fun obtenerPorId(id: Int): Reserva? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DBContract.ReservaEntry.TABLE_NAME,
            null,
            "${DBContract.ReservaEntry.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        var reserva: Reserva? = null
        if (cursor.moveToFirst()) {
            reserva = Reserva(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.ReservaEntry.COLUMN_ID)),
                numeroPersonas = cursor.getInt(cursor.getColumnIndexOrThrow(DBContract.ReservaEntry.COLUMN_NUMERO_PERSONAS)),
                nombreCliente = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ReservaEntry.COLUMN_NOMBRE_CLIENTE)),
                telefono = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ReservaEntry.COLUMN_TELEFONO)),
                fechaHora = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.ReservaEntry.COLUMN_FECHA_HORA))
            )
        }
        cursor.close()
        db.close()
        return reserva
    }

    fun obtenerPersonasPorHora(fechaHora: String): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM(${DBContract.ReservaEntry.COLUMN_NUMERO_PERSONAS}) FROM ${DBContract.ReservaEntry.TABLE_NAME} WHERE ${DBContract.ReservaEntry.COLUMN_FECHA_HORA} = ?",
            arrayOf(fechaHora)
        )

        val total = if (cursor.moveToFirst()) cursor.getInt(0) else 0
        cursor.close()
        db.close()
        return total
    }

    fun actualizar(reserva: Reserva) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBContract.ReservaEntry.COLUMN_NUMERO_PERSONAS, reserva.numeroPersonas)
            put(DBContract.ReservaEntry.COLUMN_NOMBRE_CLIENTE, reserva.nombreCliente)
            put(DBContract.ReservaEntry.COLUMN_TELEFONO, reserva.telefono)
            put(DBContract.ReservaEntry.COLUMN_FECHA_HORA, reserva.fechaHora)
        }
        db.update(
            DBContract.ReservaEntry.TABLE_NAME,
            values,
            "${DBContract.ReservaEntry.COLUMN_ID} = ?",
            arrayOf(reserva.id.toString())
        )
        db.close()
    }

    fun eliminar(id: Int) {
        val db = dbHelper.writableDatabase
        db.delete(
            DBContract.ReservaEntry.TABLE_NAME,
            "${DBContract.ReservaEntry.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
        db.close()
    }

    companion object {
        fun parseFecha(fecha: String): Long? {
            return try {
                val formato = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
                val date = formato.parse(fecha)
                date?.time
            } catch (e: Exception) {
                null
            }
        }
    }
}