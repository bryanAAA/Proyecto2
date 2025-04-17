package ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cristianrojas.borrador.R
import utils.CartManager

data class MenuItem(
    val name: String,
    val price: String,
    val imageRes: Int
)

@Composable
fun OrdenarScreen(navController: NavController) {
    val menuItems = remember {
        mutableStateListOf(
            MenuItem("The Debugger", "₡7950", R.drawable.burger1),
            MenuItem("Infinite Loop", "₡6950", R.drawable.burger2),
            MenuItem("Null Pointer", "₡5950", R.drawable.burger3),
            MenuItem("Stack Overflow Burger", "₡8500", R.drawable.burger4),
            MenuItem("Code Review Burger", "₡7800", R.drawable.burger5),
            MenuItem("404 Not Found", "₡2500", R.drawable.bebida1),
            MenuItem("Soft Reset", "₡5500", R.drawable.bebida2),
            MenuItem("JavaScript Shake", "₡4550", R.drawable.bebida3),
            MenuItem("Refactor Cola", "₡2800", R.drawable.bebida4),
            MenuItem("Compile Coffee", "₡3200", R.drawable.bebida5),
            MenuItem("Syntax S’mores", "₡2500", R.drawable.postre1),
            MenuItem("Bug-Free Brownie", "₡2750", R.drawable.postre2),
            MenuItem("Cookies Overflow", "₡2300", R.drawable.postre3),
            MenuItem("Runtime Cheesecake", "₡2900", R.drawable.postre4),
            MenuItem("Kernel Ice Cream", "₡2750", R.drawable.postre5)
        )
    }

    val quantities = remember {
        mutableStateListOf(
            *menuItems.map { CartManager.getCantidad(it.name) }.toTypedArray()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Icono de carrito con contador (parte superior derecha)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(onClick = { navController.navigate("carrito") }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cart),
                    contentDescription = "Carrito"
                )
            }

            val totalCantidad = CartManager.getCart().values.sum()
            if (totalCantidad > 0) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .offset(x = (-4).dp, y = 4.dp)
                        .size(18.dp)
                        .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "$totalCantidad",
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }
        }

        Text(
            text = "Menú - BugFree Burgers",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(menuItems) { index, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = item.imageRes),
                            contentDescription = item.name,
                            modifier = Modifier
                                .height(120.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = item.name,
                            fontSize = 14.sp
                        )
                        Text(
                            text = item.price,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(onClick = { quantities[index]++ }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_up),
                                    contentDescription = "Aumentar"
                                )
                            }

                            Text(
                                text = "${quantities[index]}",
                                style = MaterialTheme.typography.titleSmall
                            )

                            IconButton(
                                onClick = {
                                    if (quantities[index] > 0) quantities[index]--
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_down),
                                    contentDescription = "Disminuir"
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón para agregar la orden al carrito
        Button(
            onClick = {
                menuItems.forEachIndexed { index, item ->
                    if (quantities[index] > 0) {
                        CartManager.addItem(item.name, quantities[index])
                    }
                }
                navController.navigate("carrito")
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp)
        ) {
            Text("Agregar a la Orden")
        }

        Text(
            text = "© 2025 BugFree Burgers - Todos los derechos reservados",
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
