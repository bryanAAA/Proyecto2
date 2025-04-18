package ui.screens

import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import utils.CartManager
import java.io.File
import java.io.FileOutputStream

@Composable
fun FacturaScreen(navController: NavController, nombreCliente: String, numeroTarjeta: String) {
    val context = LocalContext.current
    val carrito = CartManager.getCart()
    val subtotal = carrito.entries.sumOf { it.value * extraerPrecio(it.key) }
    val tarjetaCensurada = "XXXX-XXXX-XXXX-" + numeroTarjeta.takeLast(4)
    var correoUsuario by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Pago realizado con éxito", color = MaterialTheme.colorScheme.primary, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(6.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Factura", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))

                Text("BugFree Burgers", fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Text("Teléfono: +506 8888-8888", fontSize = 14.sp)
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Text("Detalles del Cliente", fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)
                Text("Nombre: $nombreCliente")
                Text("Número de Tarjeta: $tarjetaCensurada")
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Text("Productos Ordenados", fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)
                LazyColumn(modifier = Modifier.heightIn(max = 150.dp)) {
                    carrito.entries.forEach {
                        item {
                            val precioUnitario = extraerPrecio(it.key)
                            Text("- ${it.key} x${it.value} = ₡${precioUnitario * it.value}")
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Total: ₡$subtotal", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = correoUsuario,
            onValueChange = { correoUsuario = it },
            label = { Text("Correo del destinatario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                navController.navigate("home") {
                    popUpTo("carrito") { inclusive = true }
                }
            }) {
                Text("Volver")
            }

            Button(onClick = {
                try {
                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val fileName = "factura_bugfreeburgers.pdf"
                    val file = File(downloadsDir, fileName)
                    val pdfDocument = PdfDocument()
                    val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
                    val page = pdfDocument.startPage(pageInfo)
                    val canvas = page.canvas
                    val paint = Paint()
                    paint.textSize = 12f

                    var y = 25
                    canvas.drawText("Factura - BugFree Burgers", 10f, y.toFloat(), paint)
                    y += 25
                    canvas.drawText("Teléfono: +506 8888-8888", 10f, y.toFloat(), paint)
                    y += 40

                    canvas.drawText("Detalles del Cliente:", 10f, y.toFloat(), paint)
                    y += 20
                    canvas.drawText("Nombre: $nombreCliente", 10f, y.toFloat(), paint)
                    y += 20
                    canvas.drawText("Tarjeta: $tarjetaCensurada", 10f, y.toFloat(), paint)
                    y += 30

                    canvas.drawText("Productos Ordenados:", 10f, y.toFloat(), paint)
                    y += 20
                    carrito.entries.forEach {
                        val nombre = it.key
                        val cantidad = it.value
                        val precio = extraerPrecio(nombre)
                        val total = cantidad * precio
                        canvas.drawText("- $nombre x$cantidad = ₡$total", 10f, y.toFloat(), paint)
                        y += 20
                    }

                    y += 20
                    canvas.drawText("Total: ₡$subtotal", 10f, y.toFloat(), paint)

                    pdfDocument.finishPage(page)
                    pdfDocument.writeTo(FileOutputStream(file))
                    pdfDocument.close()

                    Toast.makeText(context, "Factura guardada en ${file.absolutePath}", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Error al guardar la factura", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Descargar Factura")
            }

            Button(onClick = {
                try {
                    val facturaTexto = buildString {
                        append("Factura - BugFree Burgers\n")
                        append("Teléfono: +506 8888-8888\n\n")
                        append("Detalles del Cliente:\n")
                        append("Nombre: $nombreCliente\n")
                        append("Número de Tarjeta: $tarjetaCensurada\n\n")
                        append("Productos Ordenados:\n")
                        carrito.entries.forEach {
                            val cantidad = it.value
                            val precio = extraerPrecio(it.key)
                            val total = cantidad * precio
                            append("- ${it.key} x$cantidad = ₡$total\n")
                        }
                        append("\nTotal: ₡$subtotal\n")
                        append("\n¡Gracias por tu compra!\nBugFree Burgers")
                    }

                    val emailIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "message/rfc822" // Usa apps de correo electrónico
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(correoUsuario.text))
                        putExtra(Intent.EXTRA_SUBJECT, "Factura - BugFree Burgers")
                        putExtra(Intent.EXTRA_TEXT, facturaTexto)
                    }

                    context.startActivity(Intent.createChooser(emailIntent, "Enviar factura por correo"))

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Error al enviar la factura", Toast.LENGTH_SHORT).show()
                }
            }, enabled = correoUsuario.text.isNotBlank()) {
                Text("Enviar por Correo")
            }
        }
    }
}