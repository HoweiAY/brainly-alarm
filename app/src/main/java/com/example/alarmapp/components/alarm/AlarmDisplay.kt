package com.example.alarmapp.components.alarm

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.navigation.NavController
import com.example.alarmapp.TasksScreen
import com.example.alarmapp.model.data.Alarm
import com.example.alarmapp.model.data.AlarmDatabaseViewModel
import com.example.alarmapp.model.data.taskDifficulties
import com.example.alarmapp.model.data.taskTypes
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.Locale

@Composable
fun AlarmDisplay(
    alarmIntent: Intent,
    stopAlarmSound: () -> Unit,
    context: Context = LocalContext.current.applicationContext,
    alarmDatabaseViewModel: AlarmDatabaseViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val alarmViewModel = AlarmViewModel(context)
    var alarm by remember { mutableStateOf<Alarm?>(null) }

    var id by remember { mutableIntStateOf(alarmIntent.getIntExtra("alarmId", 0)) }
    var day by remember { mutableIntStateOf(alarmIntent.getIntExtra("dayOfWeek", 1)) }
    var hour by remember { mutableIntStateOf(alarmIntent.getIntExtra("hour", 8)) }
    var minute by remember { mutableIntStateOf(alarmIntent.getIntExtra("minute", 0)) }
    var task by remember { mutableStateOf(alarmIntent.getStringExtra("task")) }
    var roundCount by remember { mutableIntStateOf(alarmIntent.getIntExtra("roundCount", 1)) }
    var difficulty by remember { mutableStateOf(alarmIntent.getStringExtra("difficulty")) }
    var sound by remember { mutableStateOf(alarmIntent.getStringExtra("sound")) }
    var snooze by remember { mutableStateOf(alarmIntent.getBooleanExtra("snooze", true)) }
    var enabled by remember { mutableStateOf(alarmIntent.getBooleanExtra("enabled", true)) }
    var isSnoozed by remember { mutableStateOf(alarmIntent.getBooleanExtra("isSnoozed", false)) }

    var displayHour by remember { mutableIntStateOf(hour) }
    var displayMinute by remember { mutableIntStateOf(minute) }

    alarmDatabaseViewModel.getAlarmById(id)
    alarmDatabaseViewModel.foundAlarm.observeAsState().value?.let { foundAlarm ->
        alarm = foundAlarm
    }

    LaunchedEffect(Unit) {
        val initialTime = Calendar.getInstance()
        while (true) {
            val currentTime = Calendar.getInstance()
            if (currentTime.get(Calendar.MINUTE) != initialTime.get(Calendar.MINUTE)) {
                displayHour = currentTime.get(Calendar.HOUR_OF_DAY)
                displayMinute = currentTime.get(Calendar.MINUTE)
            }
            delay(1000)
        }
    }

    LaunchedEffect(Unit) {
        if (task == null) task = taskTypes[3]
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
            val hourString = String.format(Locale.getDefault(), "%02d", displayHour)
            val minuteString = String.format(Locale.getDefault(), "%02d", displayMinute)
            Text(
                text = "Time to wake up!",
                fontSize = 24.sp,
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
                onClick = {
                    resetAlarm(
                        id = id,
                        day = day,
                        hour = hour,
                        minute = minute,
                        task = task,
                        roundCount = roundCount,
                        difficulty = difficulty,
                        sound = sound,
                        snooze = snooze,
                        enabled = enabled,
                        isSnoozed = false,
                        alarmViewModel = alarmViewModel
                    )
                    if (alarm != null) alarmDatabaseViewModel.updateAlarm(alarm!!)
                    if (task == taskTypes[3]) {
                        stopAlarmSound()
                        //(context as? Activity)?.finish()
                        alarmViewModel.onAlarmDismissed(context)
                    } else {
                        when (task) {
                            taskTypes[0] -> navController.navigate(
                                "${TasksScreen.MemoryGame.name}/$roundCount/$difficulty"
                            )
                            taskTypes[1] -> navController.navigate(
                                "${TasksScreen.MathEquation.name}/$roundCount/$difficulty"
                            )
                            taskTypes[2] -> navController.navigate(
                                TasksScreen.PhoneShaking.name
                            )
                            else -> {
                                alarmViewModel.onAlarmDismissed(context)
                                //(context as? Activity)?.finish()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 30.dp)
            ) {
                val taskText = if (task != "None") "Begin task" else "Turn off"
                Text(text = taskText, fontSize = 24.sp)
            }
            if (snooze) {
                Spacer(modifier = modifier.height(30.dp))
                Button(
                    onClick = {
                        stopAlarmSound()
                        alarmViewModel.cancelAlarm(
                            updatedAlarm(
                                id = id,
                                day = day,
                                hour = hour,
                                minute = minute,
                                task = task,
                                roundCount = roundCount,
                                difficulty = difficulty,
                                sound = sound,
                                snooze = snooze
                            ),
                            isSnoozed = isSnoozed
                        )
                        if (alarm != null) alarmDatabaseViewModel.updateAlarm(alarm!!)
                        Log.d("debug snooze:", "You snooze you lose!")
                        alarmViewModel.setAlarm(
                            updatedAlarm(
                                id = id,
                                day = day,
                                hour = hour,
                                minute = minute,
                                task = task,
                                roundCount = roundCount,
                                difficulty = difficulty,
                                sound = sound,
                                snooze = snooze
                            ),
                            reset = false,
                            snoozed = true
                        )
                        Toast.makeText(context, "Alarm snoozed for 5 minutes", Toast.LENGTH_LONG).show()
                        alarmViewModel.onAlarmDismissed(context)
                              //(context as? Activity)?.finish()
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
}

fun resetAlarm(
    id: Int,
    day: Int,
    hour: Int,
    minute: Int,
    task: String? = taskTypes[3],
    roundCount: Int,
    difficulty: String? = taskDifficulties[0],
    sound: String? = "Default",
    snooze: Boolean = true,
    enabled: Boolean = false,
    isSnoozed: Boolean = false,
    alarmViewModel: AlarmViewModel,
) {
    alarmViewModel.cancelAlarm(
        updatedAlarm(
            id = id,
            day = day,
            hour = hour,
            minute = minute,
            task = task,
            roundCount = roundCount,
            difficulty = difficulty,
            sound = sound,
            snooze = snooze
        ),
        isSnoozed = isSnoozed
    )
    if (enabled) {
        alarmViewModel.setAlarm(
            updatedAlarm(
                id = id,
                day = day,
                hour = hour,
                minute = minute,
                task = task,
                roundCount = roundCount,
                difficulty = difficulty,
                sound = sound,
                snooze = snooze
            ),
            reset = true,
            snoozed = false
        )
    }
}

fun updatedAlarm(
    id: Int,
    day: Int,
    hour: Int,
    minute: Int,
    task: String? = taskTypes[3],
    roundCount: Int,
    difficulty: String? = taskDifficulties[0],
    sound: String? = "Default",
    snooze: Boolean = true
): Alarm {

    val adjustedMinute = minute % 60
    val minuteOverflow = minute / 60

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
    return Alarm(
        id = id,
        days = listOf<String>(weekday),
        hour = adjustedHour,
        minute = adjustedMinute,
        task = task!!,
        rounds = roundCount,
        difficulty = difficulty!!,
        sound = sound!!,
        snooze = snooze
    )
}

@Preview(showBackground = true)
@Composable
fun AlarmDisplayPreview() {
    //AlarmDisplay(Intent(), navController = rememberNavController())
}