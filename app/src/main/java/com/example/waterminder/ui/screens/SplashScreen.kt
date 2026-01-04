package com.example.waterminder.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.waterminder.db.modules.DatabaseModule
import com.example.waterminder.ui.theme.AppBackground
import com.example.waterminder.ui.theme.WaterMinderTheme
import kotlinx.coroutines.delay



@Composable
fun SplashScreen(navController: NavController) {

    LaunchedEffect(true) {
        delay(1500)

        val db = DatabaseModule.getDb(navController.context)
        val user = db.userDao().getUser()

        if (user != null) {
            // Email exists → auto login
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            // No user → go to login
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    AppBackground {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "💧 WaterMinder",
                color = Color(0xFF0277BD),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    WaterMinderTheme() {
        SplashScreen(navController = rememberNavController())
    }
}
