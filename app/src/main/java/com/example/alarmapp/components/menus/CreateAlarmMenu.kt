package com.example.alarmapp.components.menus

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.alarmapp.components.alarm.AlarmViewModel
import com.example.alarmapp.components.menus.viewModels.CreateAlarmViewModel
import com.example.alarmapp.model.data.Alarm
import com.example.alarmapp.model.data.AlarmDatabaseViewModel
import com.example.alarmapp.model.data.taskDifficulties
import com.example.alarmapp.model.data.taskTypes
import com.example.alarmapp.model.data.weekdays

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAlarmMenu(
    alarmId: String? = null,
    navController: NavHostController,
    alarmDatabaseViewModel: AlarmDatabaseViewModel,
    createAlarmViewModel: CreateAlarmViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val alarmViewModel = AlarmViewModel(context)
    val createAlarmUiState by createAlarmViewModel.uiState.collectAsState()
    var alarmLoaded by remember { mutableStateOf(false) }
    var alarm by remember(alarmId) { mutableStateOf<Alarm?>(null) }

    var alarmSoundSelected by remember { mutableStateOf<String>("Default") }
    val alarmPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                alarmSoundSelected = createAlarmViewModel.updateSoundSelected(context, uri)
            }
        }
    )

    LaunchedEffect(true) {
        alarmLoaded = false
        createAlarmViewModel.resetUiState(null)
    }

    if (alarmId == null) alarmLoaded = true

    if (alarmId != null && !alarmLoaded) {
        alarmDatabaseViewModel.getAlarmById(alarmId.toInt())
        alarmDatabaseViewModel.foundAlarm.observeAsState().value?.let { foundAlarm ->
            alarm = foundAlarm
        }
    }

    if (alarm != null && !alarmLoaded) {
        if (alarmId?.toInt() == alarm!!.id) alarmLoaded = true
        createAlarmViewModel.resetUiState(alarm)
    }

    if (alarmLoaded) {

        val timePickerState = rememberTimePickerState(
            initialHour = alarm?.hour?: createAlarmUiState.hourSelected,
            initialMinute = alarm?.minute?: createAlarmUiState.minuteSelected,
            is24Hour = false
        )

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = modifier.weight(0.925f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = "Set an alarm", fontSize = 30.sp)
                }
                Spacer(modifier = Modifier.height(20.dp))
                TimePicker(state = timePickerState, modifier = modifier)

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                        .background(color = Color(0xFFF0F0F0)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(15.dp))
                    LazyColumn(
                        modifier = modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // Options for selecting days
                        item {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Day",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = modifier.weight(0.2f)
                                )
                                LazyRow(
                                    modifier = modifier
                                        .weight(0.8f)
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    items(weekdays) { weekday ->
                                        WeekdayTextButton(
                                            weekday = weekday,
                                            isSelected = createAlarmUiState
                                                .weekdaysSelected
                                                .contains(weekday),
                                            onClick = {
                                                createAlarmViewModel.updateWeekdays(weekday)
                                            }
                                        )
                                    }
                                }
                            }
                            Divider(modifier = Modifier.padding(horizontal = 12.dp),thickness = 0.5.dp)
                        }
                        // Options for selecting tasks
                        item {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Task",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(text = createAlarmUiState.taskSelected, fontSize = 16.sp)
                                    Box(
                                        modifier = modifier
                                            .wrapContentSize(Alignment.CenterEnd)
                                    ) {
                                        IconButton(
                                            onClick = {
                                                createAlarmViewModel.expandTaskSelector(true)
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.ArrowDropDown,
                                                contentDescription = "Task selection options"
                                            )
                                        }
                                        DropdownMenu(
                                            expanded = createAlarmUiState.taskSelectorExpanded,
                                            onDismissRequest = {
                                                createAlarmViewModel.expandTaskSelector(false)
                                            }
                                        ) {
                                            taskTypes.forEach { taskType ->
                                                DropdownMenuItem(
                                                    text = { Text(taskType) },
                                                    onClick = {
                                                        createAlarmViewModel.updateTaskSelected(taskType)
                                                        createAlarmViewModel.expandTaskSelector(false)
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Divider(modifier = Modifier.padding(horizontal = 12.dp),thickness = 0.5.dp)
                        }
                        // Options for number of task rounds
                        item {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "No. of Rounds",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = modifier.weight(0.4f)
                                )
                                Row(
                                    modifier = modifier
                                        .weight(0.6f)
                                        .padding(horizontal = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "1")
                                    Slider(
                                        modifier = modifier.width(170.dp),
                                        enabled = createAlarmUiState.taskSelected != taskTypes[2] &&
                                            createAlarmUiState.taskSelected != taskTypes[3],
                                        value = createAlarmUiState.roundsSelected.toFloat(),
                                        onValueChange = { createAlarmViewModel.updateRoundCount(it.toInt()) },
                                        steps = 3,
                                        valueRange = 1f..5f,
                                    )
                                    Text(text = "5")
                                }
                            }
                            Divider(modifier = Modifier.padding(horizontal = 12.dp),thickness = 0.5.dp)
                        }
                        // Options for task difficulty
                        item {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Difficulty",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = modifier
                                )
                                taskDifficulties.forEach { difficulty ->
                                    Row(
                                        modifier = modifier.padding(horizontal = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        RadioButton(
                                            selected = difficulty == createAlarmUiState.difficultySelected,
                                            onClick = { createAlarmViewModel.updateTaskDifficulty(difficulty) },
                                            enabled = createAlarmUiState.taskSelected != taskTypes[2] &&
                                                    createAlarmUiState.taskSelected != taskTypes[3]
                                        )
                                        Text(text = difficulty)
                                    }
                                }
                            }
                            Divider(modifier = Modifier.padding(horizontal = 12.dp),thickness = 0.5.dp)
                        }
                        // Options for alarm sound
                        item {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Sound: ",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = modifier
                                    )
                                    Box(modifier = modifier.padding(start = 4.dp)) {
                                        Text(
                                            text = alarmSoundSelected,
                                            fontSize = 14.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                                TextButton(
                                    modifier = modifier,
                                    onClick = { alarmPickerLauncher.launch("audio/*") }
                                ) {
                                    Text(text = "Select", fontSize = 16.sp)
                                }
                            }
                            Divider(modifier = Modifier.padding(horizontal = 12.dp),thickness = 0.5.dp)
                        }
                        // Options for enabling snooze
                        item {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Snooze",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = modifier
                                )
                                Switch(
                                    checked = createAlarmUiState.snoozeEnabled,
                                    onCheckedChange = {
                                        createAlarmViewModel.updateSnoozeEnabled(!createAlarmUiState.snoozeEnabled)
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Row(
                modifier = modifier
                    .weight(0.075f)
                    .fillMaxWidth()
                    .background(color = Color(0xFFF0F0F0)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(
                    onClick = {
                        createAlarmViewModel.resetUiState(null)
                        navController.popBackStack()
                    }
                ) {
                    Text(text = "Cancel", fontSize = 20.sp)
                }
                TextButton(
                    onClick = {
                        var alarmDays =
                            if (createAlarmUiState.weekdaysSelected.isEmpty()) weekdays
                            else createAlarmUiState.weekdaysSelected
                        if (alarm != null) {
                            alarmViewModel.cancelAlarm(alarm!!)
                            alarm!!.days = alarmDays
                            alarm!!.hour = timePickerState.hour
                            alarm!!.minute = timePickerState.minute
                            alarm!!.task = createAlarmUiState.taskSelected
                            alarm!!.difficulty = createAlarmUiState.difficultySelected
                            alarm!!.rounds = createAlarmUiState.roundsSelected
                            alarm!!.sound = createAlarmUiState.alarmSoundSelected
                            alarm!!.snooze = createAlarmUiState.snoozeEnabled
                            alarmDatabaseViewModel.updateAlarm(alarm!!)
                            alarmViewModel.setAlarm(alarm!!)
                            alarm = null
                        } else {
                            val createAlarm = Alarm(
                                days = alarmDays,
                                hour = timePickerState.hour,
                                minute = timePickerState.minute,
                                task = createAlarmUiState.taskSelected,
                                difficulty = createAlarmUiState.difficultySelected,
                                rounds = createAlarmUiState.roundsSelected,
                                sound = createAlarmUiState.alarmSoundSelected,
                                snooze = createAlarmUiState.snoozeEnabled,
                                enabled = true,
                            )
                            alarmDatabaseViewModel.insertAlarm(createAlarm)
                            alarmViewModel.setAlarm(createAlarm)
                        }
                        createAlarmViewModel.resetUiState(null)
                        navController.popBackStack()
                    }
                ) {
                    Text(text = "Confirm", fontSize = 20.sp)
                }
            }
        }

    }
}

@Composable
fun WeekdayTextButton(
    weekday: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var textColor by remember { mutableStateOf(Color.Unspecified) }
    var buttonColor by remember { mutableStateOf(Color.Transparent) }
    var daySelected by remember { mutableStateOf(false) }

    TextButton(
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor, contentColor = textColor),
        onClick = {
            onClick()
            daySelected = !daySelected
            buttonColor = if (daySelected) Color.LightGray else Color.Transparent
            textColor = if (daySelected) Color.Red else Color.Unspecified
        }
    ) {
        Text(text = weekday, fontSize = 12.sp)
    }

    LaunchedEffect(isSelected) {
        daySelected = false
        buttonColor = if (isSelected) Color.LightGray else Color.Transparent
        textColor = if (isSelected) Color.Red else Color.Unspecified
        daySelected = isSelected
    }

}

/*
@Preview(showBackground = true)
@Composable
fun CreateAlarmMenuPreview() {
    CreateAlarmMenu(navController = rememberNavController())
}
*/