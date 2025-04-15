package ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cristianrojas.borrador.R

@Composable
fun MenuScreen() {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Hamburguesas", "Bebidas", "Postres")

    val hamburguesas = listOf(
        Triple("The Debugger", "Doble carne, queso cheddar, bacon crujiente y salsa secreta.", "₡7.950"),
        Triple("Infinite Loop", "Hamburguesa de pollo crispy con lechuga fresca y mayonesa.", "₡6.950"),
        Triple("Null Pointer", "Hamburguesa clásica con carne de res, tomate y queso.", "₡5.950"),
        Triple("Stack Overflow Burger", "Triple carne, cebolla caramelizada, queso suizo y salsa BBQ.", "₡8.500"),
        Triple("Code Review Burger", "Hamburguesa vegetariana con portobello, tomate seco y pesto.", "₡7.800")
    )

    val bebidas = listOf(
        Triple("404 Not Found", "Refresco de cola clásico.", "₡2.500"),
        Triple("Soft Reset", "Margarita.", "₡5.500"),
        Triple("JavaScript Shake", "Batido de frutas mixtas.", "₡4.550"),
        Triple("Refactor Cola", "Refresco de cola con un toque de lima.", "₡2.800"),
        Triple("Compile Coffee", "Café negro fuerte para mantenerte codificando toda la noche.", "₡3.200")
    )

    val postres = listOf(
        Triple("Syntax S’mores", "Galletas, chocolate y malvavisco derretido.", "₡2.500"),
        Triple("Bug-Free Brownie", "Brownie de chocolate suave y sin errores.", "₡2.750"),
        Triple("Cookies Overflow", "Galletas de chispas de chocolate recién horneadas.", "₡2.300"),
        Triple("Runtime Cheesecake", "Cheesecake cremoso con cobertura de frutos rojos.", "₡2.900"),
        Triple("Kernel Ice Cream", "Helado artesanal de vainilla con chispas de chocolate.", "₡2.750")
    )

    val imageSets = listOf(
        listOf(R.drawable.burger1, R.drawable.burger2, R.drawable.burger3),
        listOf(R.drawable.bebida1, R.drawable.bebida2, R.drawable.bebida3),
        listOf(R.drawable.postre1, R.drawable.postre2, R.drawable.postre3)
    )

    val itemsList = listOf(hamburguesas, bebidas, postres)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Menú",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = "*Todos los precios incluyen IVA del 13%.",
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(itemsList[selectedTabIndex]) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("${item.first} - ${item.third}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(item.second, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            imageSets[selectedTabIndex].forEach { imageId ->
                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}
