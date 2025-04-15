package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cristianrojas.borrador.ImageCarousel
import com.cristianrojas.borrador.R
import androidx.compose.ui.unit.sp


@Composable
fun HomeScreen(navController: NavController) {
    val images = listOf(
        R.drawable.restaurante1,
        R.drawable.restaurante2,
        R.drawable.restaurante3
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
                text = "Bienvenido a BugFree Burgers",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Disfruta de la mejor experiencia culinaria con un toque de tecnología.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 12.dp),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ){
            ImageCarousel(imageList = images)
        }

        Text(
            text = "© 2025 BugFree Burgers - Todos los derechos reservados",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
    }
}