package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import model.Usuario
import utils.SessionManager

@Composable
fun RegisterScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Crea una cuenta",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                var usuario by remember { mutableStateOf(Usuario()) }

                OutlinedTextField(
                    value = usuario.nombre,
                    onValueChange = { usuario = usuario.copy(nombre = it) },
                    label = { Text("Nombre de Usuario") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = usuario.password,
                    onValueChange = { usuario = usuario.copy(password = it) },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))
                val db = Data.ReservaDao(LocalContext.current);
                val encryption = security.authentication();
                Button(
                    onClick = {
                        if (usuario.nombre.isNotBlank() && usuario.password.isNotBlank() && usuario.nombre.isNotBlank()) {
                            // Logic to register the user
                            db.insertarUsuario(usuario.nombre,encryption.AESGCMEncrypter(usuario.password))
                            SessionManager.currentUser = usuario
                            println("Registrado como: ${SessionManager.currentUser?.nombre}")
                            navController.navigate("home")
                        } else {
                            println("Llena los campos acordemente.")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Registrarse")
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = {
                    navController.popBackStack() // Navigate back to login screen
                }) {
                    Text("¿Ya tienes una cuenta? Inicia sesión")
                }
            }
        }
    }
}
