package com.example.alarmapp.components.menus

import android.util.Log
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.alarmapp.model.data.Alarm
import com.example.alarmapp.model.data.taskDifficulties
import com.example.alarmapp.model.data.taskTypes
import com.example.alarmapp.model.data.weekdays
import com.example.alarmapp.viewmodels.AlarmDatabaseViewModel
import com.example.alarmapp.viewmodels.CreateAlarmViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAlarmMenu(
    alarmId: Int? = null,
    navController: NavHostController,
    alarmDatabaseViewModel: AlarmDatabaseViewModel,
    createAlarmViewModel: CreateAlarmViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val createAlarmUiState by createAlarmViewModel.uiState.collectAsState()
    var alarmLoaded by remember { mutableStateOf(false) }
    var alarm by remember(alarmId) {
        mutableStateOf<Alarm?>(null)
    }

    Log.i("debug create alarm: ", "initial alarmId: ${alarmId?: "null"}")

    if (alarmId != null && !alarmLoaded) alarmDatabaseViewModel.getAlarmById(alarmId)
    alarmDatabaseViewModel.foundAlarm.observeAsState().value?.let { foundAlarm ->
        alarm = foundAlarm
    }
    Log.i("debug foundAlarm: ", "foundAlarm: id - ${alarm?.id?: "no id found"}")
    Log.i("debug foundAlarm: ", "foundAlarm: task - ${alarm?.task?: "Nope"}")

    if (alarm != null && !alarmLoaded) {
        if (alarmId == alarm!!.id) alarmLoaded = true
        createAlarmViewModel.resetUiState(alarm)
    }

    val timePickerState = rememberTimePickerState(
        initialHour = createAlarmUiState.hourSelected,
        initialMinute = createAlarmUiState.minuteSelected,
        is24Hour = false
    )

    if (alarmLoaded) {

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
                Text(text = "Set an alarm ${alarm?.id}", fontSize = 30.sp)
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
                                            Log.i("debug select days: ", createAlarmUiState.weekdaysSelected.toString())
                                            Log.i("debug select days: ", createAlarmUiState.weekdaysSelected.contains(weekday).toString())
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
                            var roundCount by remember {
                                mutableFloatStateOf(createAlarmUiState.roundsSelected.toFloat())
                            }
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
                                    enabled = taskTypes
                                        .slice(2 until taskTypes.size)
                                        .any { it != createAlarmUiState.taskSelected },
                                    value = roundCount,
                                    onValueChange = { roundCount = it },
                                    steps = 3,
                                    valueRange = 1f..5f,
                                    onValueChangeFinished = {
                                        createAlarmViewModel.updateRoundCount(roundCount.roundToInt())
                                    }
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
                                        enabled = taskTypes
                                            .slice(2 until taskTypes.size)
                                            .any { it != createAlarmUiState.taskSelected }
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
                                        text = createAlarmUiState.alarmSoundSelected,
                                        fontSize = 14.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            TextButton(
                                modifier = modifier,
                                onClick = { /*TODO*/ }
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
            TextButton(onClick = { navController.popBackStack()}) {
                Text(text = "Cancel", fontSize = 22.sp)
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Confirm", fontSize = 22.sp)
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
    var isSelected by remember { mutableStateOf(isSelected) }
    var textColor by remember { mutableStateOf(Color.Unspecified) }
    var buttonColor by remember { mutableStateOf(Color.Transparent) }

    TextButton(
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor, contentColor = textColor),
        onClick = {
            onClick()
            isSelected = !isSelected
            buttonColor = if (isSelected) Color.LightGray else Color.Transparent
            textColor = if (isSelected) Color.Red else Color.Unspecified
        }
    ) {
        Text(text = weekday, fontSize = 12.sp)
    }

    LaunchedEffect(isSelected) {
        buttonColor = if (isSelected) Color.LightGray else Color.Transparent
        textColor = if (isSelected) Color.Red else Color.Unspecified
    }

}

/*
@Preview(showBackground = true)
@Composable
fun CreateAlarmMenuPreview() {
    CreateAlarmMenu(navController = rememberNavController())
}
*/