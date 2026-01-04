package com.example.waterminder.ui.screens

import com.example.waterminder.ui.theme.AppBackground
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.waterminder.db.entity.WaterLogEntity
import com.example.waterminder.db.modules.DatabaseModule
import com.example.waterminder.ui.theme.WaterMinderTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyLogScreen(
    navController: NavController
) {

    val waterBlue = Color(0xFF0288D1)
    val context = LocalContext.current
    val db = DatabaseModule.getDb(context)
    var logs by remember { mutableStateOf(listOf<WaterLogEntity>()) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Daily Logs \uD83D\uDCDD", fontWeight = FontWeight.Bold) },
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
                    "Your Entries",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0277BD)
                )

                Spacer(modifier = Modifier.height(10.dp))

                LaunchedEffect(Unit) {
                    logs = db.waterLogDao().getAllLogs()
                }

                LazyColumn {
                    items(logs) { log ->
                        val time = java.text.SimpleDateFormat("hh:mm a").format(java.util.Date(log.timestamp))
                        LogItem(
                            text = "$time — ${log.amountMl} ml",
                            onDelete = {
                                scope.launch {
                                    db.waterLogDao().clearLogs()
                                    logs = db.waterLogDao().getAllLogs()
                                }
                            }
                        )
                    }
                }


                Spacer(modifier = Modifier.height(15.dp))

            }
        }
    }
}

@Composable
fun SummaryCard(total: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB3E5FC)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Today's Total",
                fontSize = 18.sp,
                color = Color(0xFF0277BD),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "$total ml",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0277BD)
            )
        }
    }
}


@Composable
fun LogItem(text: String, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.LocalDrink,
                contentDescription = null,
                tint = Color(0xFF0277BD),
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = text,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFE53935)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DailyLogScreenPreview() {
    WaterMinderTheme {
        DailyLogScreen(navController = rememberNavController())
    }
}
