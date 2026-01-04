package com.example.waterminder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    var progress by remember { mutableStateOf(0.45f) } // 45% filled
    val goal = 2000 // ml
    val intake = (progress * goal).toInt()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFBBDEFB), Color.White)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Title
            Text(
                text = "💧 WaterMinder",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0277BD)
            )

            Text(
                text = "Stay hydrated, stay healthy!",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Circular Progress Indicator
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = progress,
                    strokeWidth = 12.dp,
                    color = Color(0xFF0288D1),
                    trackColor = Color(0xFFE1F5FE),
                    modifier = Modifier.size(180.dp)
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$intake ml",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0277BD)
                    )
                    Text(
                        text = "of $goal ml",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Add Water Button
            Button(
                onClick = {
                    if (progress < 1f) progress += 0.1f
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0277BD)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Add 200ml", color = Color.White, fontSize = 18.sp)
            }

            // Reset Button
            TextButton(onClick = { progress = 0f }) {
                Text("Reset", color = Color(0xFF0277BD))
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Fun footer
            Surface(
                color = Color(0xFFE3F2FD),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Tip of the day 💡", fontWeight = FontWeight.Bold, color = Color(0xFF0277BD))
                    Text(
                        "Drink a glass of water first thing in the morning!",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
