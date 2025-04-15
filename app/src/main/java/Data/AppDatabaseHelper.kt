package Data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

object DBContract {
    const val DATABASE_NAME = "reservas.db"
    const val DATABASE_VERSION = 1

    object ReservaEntry {
        const val TABLE_NAME = "Reserva"
        const val COLUMN_ID = "id"
        const val COLUMN_NUMERO_PERSONAS = "numeroPersonas"
        const val COLUMN_NOMBRE_CLIENTE = "nombreCliente"
        const val COLUMN_TELEFONO = "telefono"
        const val COLUMN_FECHA_HORA = "fechaHora"
    }
}

class AppDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DBContract.DATABASE_NAME, null, DBContract.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createReservaTable = """
            CREATE TABLE ${DBContract.ReservaEntry.TABLE_NAME} (
                ${DBContract.ReservaEntry.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${DBContract.ReservaEntry.COLUMN_NUMERO_PERSONAS} INTEGER NOT NULL,
                ${DBContract.ReservaEntry.COLUMN_NOMBRE_CLIENTE} TEXT NOT NULL,
                ${DBContract.ReservaEntry.COLUMN_TELEFONO} TEXT NOT NULL,
                ${DBContract.ReservaEntry.COLUMN_FECHA_HORA} TEXT NOT NULL
            )
        """
        db.execSQL(createReservaTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${DBContract.ReservaEntry.TABLE_NAME}")
        onCreate(db)
    }
}