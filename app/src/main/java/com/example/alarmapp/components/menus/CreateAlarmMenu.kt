package com.example.alarmapp.components.menus

import androidx.annotation.StringRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.alarmapp.model.data.taskDifficulties
import com.example.alarmapp.model.data.taskTypes
import com.example.alarmapp.model.data.weekdays
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAlarmMenu(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var taskSelectorExpanded by remember { mutableStateOf(false) }

    var weekdaysSelected by remember { mutableStateOf(mutableListOf<String>()) }
    var taskSelected by remember { mutableStateOf(taskTypes[0]) }
    var roundsSelected by remember { mutableStateOf(1) }
    var difficultySelected by remember { mutableStateOf(taskDifficulties[0]) }
    var alarmSoundSelected by remember { mutableStateOf("Default") }
    var snoozeEnabled by remember { mutableStateOf(true) }

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
            val timePickerState = rememberTimePickerState(is24Hour = false)

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
                                        isSelected = weekdaysSelected.contains(weekday),
                                        onClick = {
                                            if (weekdaysSelected.contains(weekday)) {
                                                weekdaysSelected.remove(weekday)
                                            } else {
                                                weekdaysSelected.add(weekday)
                                            }
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
                                Text(text = taskSelected, fontSize = 16.sp)
                                Box(
                                    modifier = modifier
                                        .wrapContentSize(Alignment.CenterEnd)
                                ) {
                                    IconButton(onClick = { taskSelectorExpanded = true }) {
                                        Icon(
                                            Icons.Default.ArrowDropDown,
                                            contentDescription = "Task selection options"
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = taskSelectorExpanded,
                                        onDismissRequest = { taskSelectorExpanded = false }
                                    ) {
                                        taskTypes.forEach { taskType ->
                                            DropdownMenuItem(
                                                text = { Text(taskType) },
                                                onClick = {
                                                    taskSelected = taskType
                                                    taskSelectorExpanded = false
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
                            var roundCount by remember { mutableFloatStateOf(1f) }
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
                                    enabled = taskSelected != taskTypes[2],
                                    value = roundCount,
                                    onValueChange = { roundCount = it },
                                    steps = 3,
                                    valueRange = 1f..5f,
                                    onValueChangeFinished = { roundsSelected = roundCount.roundToInt() }
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
                                        selected = difficulty == difficultySelected,
                                        onClick = { difficultySelected = difficulty }
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
                                checked = snoozeEnabled,
                                onCheckedChange = { snoozeEnabled = !snoozeEnabled }
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

@Preview(showBackground = true)
@Composable
fun CreateAlarmMenuPreview() {
    CreateAlarmMenu(navController = rememberNavController())
}