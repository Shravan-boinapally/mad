package com.example.waterminder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.waterminder.db.entity.WaterGoalEntity
import com.example.waterminder.db.modules.DatabaseModule
import com.example.waterminder.notification.WaterReminderWorker
import com.example.waterminder.ui.theme.AppBackground
import com.example.waterminder.ui.theme.WaterMinderTheme
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterGoalSetupScreen(
    navController: NavController
) {

    var goal by remember { mutableFloatStateOf(2000f) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val snackBarHostState = remember { SnackbarHostState() }

    val waterBlue = Color(0xFF0288D1)

    Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Your Water Goal \uD83D\uDCE2", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = waterBlue,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White
                    ),
                    modifier = Modifier.shadow(6.dp),
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                        }
                    }
                )
            },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->

        AppBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Water Goal Setup",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0277BD)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Set your daily water intake goal",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(30.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFB3E5FC), RoundedCornerShape(16.dp))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${goal.toInt()} ml",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0277BD)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                Slider(
                    value = goal,
                    onValueChange = { goal = it },
                    valueRange = 1000f..5000f,
                    steps = 7,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF0277BD),
                        activeTrackColor = Color(0xFF0277BD)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        scope.launch {
                            DatabaseModule.getDb(context)
                                .waterGoalDao()
                                .saveGoal(
                                    WaterGoalEntity(goalMl = goal.toInt())
                                )

                            val workRequest =
                                OneTimeWorkRequestBuilder<WaterReminderWorker>()
                                    .setInitialDelay(1, TimeUnit.MINUTES)
                                    .build()

                            WorkManager.getInstance(context)
                                .enqueueUniqueWork(
                                    "water_reminder_worker",
                                    ExistingWorkPolicy.REPLACE,
                                    workRequest
                                )

                            snackBarHostState.showSnackbar(
                                "Goal saved: ${goal.toInt()} ml"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0277BD)
                    )
                ) {
                    Text("Save Goal", color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WaterGoalSetupScreenPreview() {
    WaterMinderTheme {
        WaterGoalSetupScreen(navController = rememberNavController())
    }
}

