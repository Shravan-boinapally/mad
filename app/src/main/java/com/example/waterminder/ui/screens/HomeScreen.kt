package com.example.waterminder.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.waterminder.ui.theme.AppBackground
import com.example.waterminder.ui.theme.WaterMinderTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.waterminder.db.dao.UserDAO
import com.example.waterminder.db.entity.UserEntity
import com.example.waterminder.db.entity.WaterIntakeEntity
import com.example.waterminder.db.entity.WaterLogEntity
import com.example.waterminder.db.modules.DatabaseModule
import com.google.firebase.logger.Logger
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    userDao: UserDAO
) {

    val scope = rememberCoroutineScope()

    val scrollState = rememberScrollState()
    val waterBlue = Color(0xFF0288D1)
    val context = LocalContext.current

    var goal by remember { mutableStateOf(2000) }
    var progress by remember { mutableFloatStateOf(0f) }
    val intake = (progress * goal).toInt()

    var intakeMl by remember { mutableStateOf(0) }

    var showReward by remember { mutableStateOf(false) }
    var rewardShownOnce by remember { mutableStateOf(false) }

    var showLogoutDialog by remember { mutableStateOf(false) }

    val hydrationTips = listOf(
        "Drink a glass of water right after waking up 🌅",
        "Carry a water bottle wherever you go 💧",
        "Sip water every 30–60 minutes ⏰",
        "Drink water before meals 🍽️",
        "If you feel tired, drink water first 😴",
        "Increase water intake during hot weather ☀️",
        "Drink more water when exercising 🏃‍♂️",
        "Don’t wait until you feel thirsty 🚫"
    )

    var currentTip by remember {
        mutableStateOf(hydrationTips.random())
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentTip = hydrationTips.random()
            kotlinx.coroutines.delay(1000)
        }
    }


    LaunchedEffect(Unit) {
        val db = DatabaseModule.getDb(context)

        goal = db.waterGoalDao()
            .getLatestGoal()
            ?.goalMl ?: 2000

        val savedIntake = db.waterIntakeDao().getIntake()
        intakeMl = savedIntake?.intakeMl ?: 0


        progress = intakeMl.toFloat() / goal
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home \uD83C\uDFE1", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = waterBlue,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                modifier = Modifier.shadow(6.dp),
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.PowerSettingsNew,
                            contentDescription = "Logout",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        AppBackground {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .padding(24.dp),
            ) {

                SectionTitle("Today's Water Goal \uD83C\uDFAF")

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = progress,
                                strokeWidth = 10.dp,
                                color = Color(0xFF0288D1),
                                trackColor = Color(0xFFE1F5FE),
                                modifier = Modifier.size(150.dp)
                            )

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "$intake ml",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0277BD)
                                )
                                Text(
                                    "of $goal ml",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFFE53935
                                    )
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                onClick = {
                                    intakeMl = 0
                                    progress = 0f
                                    scope.launch {
                                        DatabaseModule.getDb(context)
                                            .waterIntakeDao()
                                            .saveIntake(
                                                WaterIntakeEntity(intakeMl = 0)
                                            )
                                    }
                                }
                            ) {
                                Text("Reset", color = Color.White, fontSize = 16.sp)
                            }

                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF43A047
                                    )
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                onClick = {
                                    if (progress < 1f) {
                                        val newIntake = intakeMl + 100
                                        intakeMl = newIntake
                                        progress = newIntake.toFloat() / goal

                                        if (progress >= 1f && !rewardShownOnce) {
                                            showReward = true
                                            rewardShownOnce = true
                                        }


                                        scope.launch {
                                            DatabaseModule.getDb(context)
                                                .waterIntakeDao()
                                                .saveIntake(
                                                    WaterIntakeEntity(intakeMl = newIntake)
                                                )
                                            DatabaseModule.getDb(context)
                                                .waterLogDao().insertLog(
                                                WaterLogEntity(amountMl = 100)
                                            )
                                        }
                                    }
                                }
                            ) {
                                Text("Add 100ml", color = Color.White, fontSize = 16.sp)
                            }

                        }

                    }
                }


                Spacer(modifier = Modifier.height(30.dp))

                SectionTitle("Hydration Tip \uD83D\uDCA1")

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFB3E5FC)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "tip",
                            tint = Color(0xFFDDAF61),
                            modifier = Modifier.size(32.dp)
                        )


                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            currentTip,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                SectionTitle("Today's Intake \uD83E\uDD5B")

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalDrink,
                            contentDescription = "intake",
                            tint = Color(0xFF0277BD),
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            "${intakeMl / 1000f} L consumed so far",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                SectionTitle("Stay on Track \uD83D\uDE09")
                Spacer(modifier = Modifier.height(10.dp))
                StayOnTrackCard(
                    "Water Goal Setup",
                    Icons.Default.Settings
                ) {
                    navController.navigate("waterGoalSetUp")
                }

                StayOnTrackCard(
                    "Daily Log",
                    Icons.AutoMirrored.Filled.List
                ) {
                    navController.navigate("dailyLog")
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = {
                showLogoutDialog = false
            },

            title = { Text("Confirm Logout!") },
            text = { Text("Are you sure you want to log out?") },

            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        userDao.clearUser()
                        navController.navigate("login") {
                            popUpTo(0)
                        }
                    }
                }) {
                    Text("Logout", color = Color.Red)
                }
            },

            dismissButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    if (showReward) {
        AlertDialog(
            containerColor =  Color.White,
            onDismissRequest = { showReward = false },
            confirmButton = {
                Button(onClick = { showReward = false }) {
                    Text("Claim 🎁")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showReward = false }) {
                    Text("Cancel")
                }
            },
            title = {
                Text("🎉 Goal Completed!")
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("You hit your water goal today 💧")
                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Scratch Reward 🎊", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("🎁 ₹10 Voucher")
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Code: WATER10",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                }
            }
        )
    }

}

@Composable
fun StayOnTrackCard(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color(0xFF558B2F),
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(text, fontSize = 18.sp, fontWeight = FontWeight.Medium)
        }
    }
}


@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF0277BD)
    )
}

class FakeUserDao : UserDAO {
    override suspend fun saveUser(user: UserEntity) {
    }

    override suspend fun deleteUser(user: UserEntity) {
    }

    override suspend fun getUser(): UserEntity {
        return UserEntity(email = "user@example.com")
    }

    override suspend fun clearUser() {
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    WaterMinderTheme {
        HomeScreen(
            navController = rememberNavController(),
            userDao = FakeUserDao()
        )
    }
}
