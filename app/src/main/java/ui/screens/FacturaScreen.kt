package ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import utils.CartManager
import android.graphics.pdf.PdfDocument
import android.graphics.Paint
import java.io.File
import java.io.FileOutputStream
import utils.getFacturaOutputStream



@Composable
fun FacturaScreen(navController: NavController, nombreCliente: String, numeroTarjeta: String) {
    val carrito = CartManager.getCart()
    val subtotal = carrito.entries.sumOf { it.value * extraerPrecio(it.key) }
    val tarjetaCensurada = "XXXX-XXXX-XXXX-" + numeroTarjeta.takeLast(4)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Pago realizado con éxito", color = MaterialTheme.colorScheme.onPrimaryContainer, fontSize = 18.sp)
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
                val context = navController.context
                val fileName = "factura_bugfreeburgers.pdf"

                try {
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

                    val outputStream = getFacturaOutputStream(context, fileName)
                    outputStream?.let {
                        pdfDocument.writeTo(it)
                        Toast.makeText(context, "Factura guardada en Descargas", Toast.LENGTH_LONG).show()
                    } ?: Toast.makeText(context, "No se pudo guardar la factura", Toast.LENGTH_SHORT).show()

                    pdfDocument.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(navController.context, "Error al guardar factura", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Descargar Factura")
            }



            Button(onClick = {
                Toast.makeText(navController.context, "Factura enviada por correo.", Toast.LENGTH_SHORT).show()
            }) {
                Text("Enviar por Correo")
            }
        }
    }
}