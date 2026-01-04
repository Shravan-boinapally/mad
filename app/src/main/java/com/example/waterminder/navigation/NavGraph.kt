package com.example.waterminder.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.waterminder.db.dao.UserDAO
import com.example.waterminder.models.AuthViewModel
import com.example.waterminder.ui.screens.*

private const val SLIDE_ANIM_DURATION = 350
private val smoothEasing = FastOutSlowInEasing

private val enterTransitionForward:
        AnimatedContentTransitionScope<*>.() -> EnterTransition = {
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(SLIDE_ANIM_DURATION, easing = smoothEasing)
    )
}

private val exitTransitionForward:
        AnimatedContentTransitionScope<*>.() -> ExitTransition = {
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(SLIDE_ANIM_DURATION, easing = smoothEasing)
    )
}

private val enterTransitionBackward:
        AnimatedContentTransitionScope<*>.() -> EnterTransition = {
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(SLIDE_ANIM_DURATION, easing = smoothEasing)
    )
}

private val exitTransitionBackward:
        AnimatedContentTransitionScope<*>.() -> ExitTransition = {
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(SLIDE_ANIM_DURATION, easing = smoothEasing)
    )
}

@Composable
fun NavGraph(
    startDestination: String = "splash",
    userDao: UserDAO
) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = enterTransitionForward,
        exitTransition = exitTransitionForward,
        popEnterTransition = enterTransitionBackward,
        popExitTransition = exitTransitionBackward
    ) {

        composable("splash") {
            SplashScreen(navController)
        }

        composable("signup") {
            SignupScreen(navController, authViewModel)
        }

        composable("login") {
            LoginScreen(navController, authViewModel)
        }

        composable("home") {
            HomeScreen(
                navController,
                userDao
            )
        }

        composable("dailyLog") {
            DailyLogScreen(navController)
        }

        composable("waterGoalSetUp") {
            WaterGoalSetupScreen(navController)
        }
    }
}
