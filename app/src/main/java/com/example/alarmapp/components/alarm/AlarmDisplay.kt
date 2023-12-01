package com.example.alarmapp.components.alarm

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.alarmapp.AppScreen
import com.example.alarmapp.model.data.Alarm
import com.example.alarmapp.model.data.taskDifficulties
import com.example.alarmapp.model.data.taskTypes
import java.util.Calendar
import java.util.Locale

@Composable
fun AlarmDisplay(
    //alarmDatabaseViewModel: AlarmDatabaseViewModel,
    alarmIntent: Intent,
    context: Context = LocalContext.current,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val alarmViewModel = AlarmViewModel(LocalContext.current)

    var id by remember { mutableIntStateOf(alarmIntent.getIntExtra("alarmId", 0)) }
    var day by remember { mutableIntStateOf(alarmIntent.getIntExtra("dayOfWeek", 1)) }
    var hour by remember { mutableIntStateOf(alarmIntent.getIntExtra("hour", 8)) }
    var minute by remember { mutableIntStateOf(alarmIntent.getIntExtra("minute", 0)) }
    var task by remember { mutableStateOf(alarmIntent.getStringExtra("task")) }
    var roundCount by remember { mutableIntStateOf(alarmIntent.getIntExtra("roundCount", 1)) }
    var difficulty by remember { mutableStateOf(alarmIntent.getStringExtra("difficulty")) }
    var sound by remember { mutableStateOf(alarmIntent.getStringExtra("sound")) }
    var snooze by remember { mutableStateOf(alarmIntent.getBooleanExtra("snooze", true)) }

    LaunchedEffect(Unit) {
        if (task == null) task = taskTypes[4]
        if (difficulty == null) difficulty = taskDifficulties[0]
        if (sound == null) sound = "Default"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val hourString = String.format(Locale.getDefault(), "%02d", hour)
            val minuteString = String.format(Locale.getDefault(), "%02d", minute)
            Text(
                text = "Wake up!",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xFFCB0000)
            )
            Text(text = "$hourString:$minuteString", fontSize = 100.sp)
        }
        Spacer(modifier = modifier.height(80.dp))

        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 30.dp)
            ) {
                Text(text = "Begin task", fontSize = 24.sp)
            }
            Spacer(modifier = modifier.height(30.dp))
            Button(
                onClick = {
                    val snoozeAlarm = alarmViewModel.cancelAlarm(
                        alarmToSnooze(id, day, hour, minute, task, roundCount, difficulty, sound)
                    )
                    if (navController.previousBackStackEntry == null) {
                        (context as? Activity)?.finish()
                    } else {
                        navController.popBackStack(AppScreen.AlarmScreen.name, inclusive = true)
                    }
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 30.dp)
            ) {
                Text(text = "Snooze for 5 minutes", fontSize = 24.sp)
            }
        }
    }
}

fun alarmToSnooze(
    id: Int,
    day: Int,
    hour: Int,
    minute: Int,
    task: String? = taskTypes[4],
    roundCount: Int,
    difficulty: String? = taskDifficulties[0],
    sound: String? = "Default"
): Alarm {

    val adjustedMinute = minute % 60
    val minuteOverflow = minute / 60

    // Normalize hour value
    val adjustedHour = (hour + minuteOverflow) % 24
    val hourOverflow = (hour + minuteOverflow) / 24

    val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

    val weekday = when ((day - currentDayOfWeek + 7 + 7 * hourOverflow) % 7) {
        Calendar.MONDAY -> "Mon"
        Calendar.TUESDAY -> "Tue"
        Calendar.WEDNESDAY -> "Wed"
        Calendar.THURSDAY -> "Thu"
        Calendar.FRIDAY -> "Fri"
        Calendar.SATURDAY -> "Sat"
        Calendar.SUNDAY -> "Sun"
        else -> "Sun"
    }
    return Alarm(id, listOf<String>(weekday), hour, minute, task!!, roundCount, difficulty!!, sound!!)
}

@Preview(showBackground = true)
@Composable
fun AlarmDisplayPreview() {
    AlarmDisplay(Intent(), navController = rememberNavController())
}