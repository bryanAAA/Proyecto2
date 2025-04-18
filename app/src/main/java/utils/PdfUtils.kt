package utils

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

fun getFacturaOutputStream(context: Context, fileName: String = "factura.pdf"): OutputStream? {
    return try {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDir.exists()) downloadsDir.mkdirs()

        val file = File(downloadsDir, fileName)
        Log.d("FacturaPDF", "Guardando en Descargas: ${file.absolutePath}")
        FileOutputStream(file)

    } catch (e: Exception) {
        // Si falla, intenta guardar en archivos internos
        Log.w("FacturaPDF", "No se pudo guardar en Descargas. Guardando internamente.")
        try {
            val file = File(context.filesDir, fileName)
            Log.d("FacturaPDF", "Guardando internamente: ${file.absolutePath}")
            FileOutputStream(file)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}
