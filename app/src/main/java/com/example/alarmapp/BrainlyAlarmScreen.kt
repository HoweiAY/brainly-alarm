package com.example.alarmapp

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.alarmapp.components.alarm.AlarmDisplay
import com.example.alarmapp.components.menus.CreateAlarmMenu
import com.example.alarmapp.components.menus.HomeMenu
import com.example.alarmapp.components.tasks.MathEquation
import com.example.alarmapp.components.tasks.MemoryGame
import com.example.alarmapp.components.tasks.PhoneShaking
import com.example.alarmapp.model.data.AlarmDatabaseViewModel
import com.example.alarmapp.model.data.taskDifficulties

enum class AppScreen(@StringRes val title: Int) {
    MainScreen(title = R.string.main_screen),
    AlarmScreen(title = R.string.alarm_screen),
}

enum class AlarmScreen(@StringRes val title: Int) {
    Home(title = R.string.home_menu),
    CreateAlarm(title = R.string.create_alarm_menu),
    DisplayAlarm(title = R.string.display_alarm_screen),
}

enum class TasksScreen(@StringRes val title: Int) {
    MemoryGame(title = R.string.memory_game),
    MathEquation(title = R.string.math_equation),
    PhoneShaking(title = R.string.phone_shaking),
}

@Composable
fun BrainlyAlarmApp (
    alarmIntent: Intent,
    context: Context,
    stopAlarmSound: () -> Unit,
    navController: NavHostController = rememberNavController(),
    alarmDatabaseViewModel: AlarmDatabaseViewModel,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val isAlarmTriggered = remember {
        mutableStateOf(alarmIntent.getBooleanExtra("alarmTriggered", false))
    }

    NavHost(
        navController = navController,
        startDestination = if (isAlarmTriggered.value) AppScreen.AlarmScreen.name else AppScreen.MainScreen.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        modifier = Modifier
    ) {
        navigation(
            startDestination = AlarmScreen.Home.name,
            route = AppScreen.MainScreen.name
        ) {
            composable(route = AlarmScreen.Home.name) {
                HomeMenu(
                    alarmDatabaseViewModel = alarmDatabaseViewModel,
                    navController = navController,
                )
            }

            composable(
                route = "${AlarmScreen.CreateAlarm.name}?alarmId={alarmId}",
                arguments = listOf(navArgument("alarmId") {
                    type = NavType.StringType
                    nullable = true
                }),
                enterTransition = { slideInHorizontally(
                    animationSpec = tween(300)
                ) + fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) },
                exitTransition = { slideOutHorizontally(
                    animationSpec = tween(300)
                ) + fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) },
            ) {backStackEntry ->
                CreateAlarmMenu(
                    alarmId = backStackEntry.arguments?.getString("alarmId"),
                    context = context,
                    alarmDatabaseViewModel = alarmDatabaseViewModel,
                    navController = navController,
                )
            }
        }

        navigation(
            startDestination = AlarmScreen.DisplayAlarm.name,
            route = AppScreen.AlarmScreen.name,
        ) {
            composable(route = AlarmScreen.DisplayAlarm.name) {
                AlarmDisplay(
                    alarmIntent = alarmIntent,
                    stopAlarmSound = stopAlarmSound,
                    context = context,
                    alarmDatabaseViewModel = alarmDatabaseViewModel,
                    navController = navController
                )
            }

            composable(
                route = "${TasksScreen.MemoryGame.name}/{rounds}/{difficulty}"
            ) { backStackEntry ->
                val rounds = backStackEntry.arguments?.getString("rounds")?.toInt()?: 1
                val difficulty = backStackEntry.arguments?.getString("difficulty")?: taskDifficulties[0]
                MemoryGame(
                    difficulty = difficulty,
                    rounds = rounds,
                    context = context,
                    stopAlarmSound = stopAlarmSound,
                )
            }

            composable(
                route = "${TasksScreen.MathEquation.name}/{rounds}/{difficulty}"
            ) {backStackEntry ->
                val rounds = backStackEntry.arguments?.getString("rounds")?.toInt()?: 1
                val difficulty = backStackEntry.arguments?.getString("difficulty")?: taskDifficulties[0]
                MathEquation(
                    difficulty = difficulty,
                    rounds = rounds,
                    stopAlarmSound = stopAlarmSound,
                )
            }

            composable(route = TasksScreen.PhoneShaking.name) {
                PhoneShaking(
                    context = context,
                    stopAlarmSound = stopAlarmSound,
                )
            }
        }
    }

    //HomeMenu(alarmData)
    //CreateAlarmMenu()
}