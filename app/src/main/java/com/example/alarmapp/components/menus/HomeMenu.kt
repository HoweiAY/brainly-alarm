package com.example.alarmapp.components.menus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.alarmapp.AlarmScreen
import com.example.alarmapp.ui.AlarmCard
import com.example.alarmapp.model.data.AlarmDatabaseViewModel
import com.example.alarmapp.components.menus.viewModels.HomeViewModel

@Composable
fun HomeMenu(
    alarmDatabaseViewModel: AlarmDatabaseViewModel,
    navController: NavHostController,
    homeViewModel: HomeViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val homeUiState by homeViewModel.uiState.collectAsState()
    val alarmData by alarmDatabaseViewModel.allAlarms.observeAsState(listOf())
    var enabledAlarms by remember { mutableStateOf(homeUiState.enabledAlarms) }
    var nextAlarmMsg by remember { mutableStateOf("No alarms set") }

    LaunchedEffect(alarmData) {
        alarmData.forEach { alarm ->
            homeViewModel.toggleAlarmEnabled(alarm, alarm.enabled)
        }
        nextAlarmMsg = if (alarmData.isNotEmpty() && homeUiState.enabledAlarms.isNotEmpty())
        "Next alarm in 3 days 12 hours 4 minutes"
        else "No alarms set"
    }

    LaunchedEffect(homeUiState.enableAlarmChanged) {
        nextAlarmMsg = if (alarmData.isNotEmpty() && homeUiState.enabledAlarms.isNotEmpty())
            "Next alarm in 3 days 12 hours 4 minutes"
        else "No alarms set"
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = if (homeUiState.alarmEditEnabled) modifier.weight(0.925f) else modifier,
            verticalArrangement = Arrangement.Top,
        ) {
            Column(
                modifier = modifier.padding(start = 12.dp),
                horizontalAlignment = Alignment.Start) {
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    text = "Welcome to Brainly Alarm!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(60.dp))
            }
            Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    //text = nextAlarmMsg,
                    text = nextAlarmMsg,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Clip,
                    modifier = modifier,
                )
                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 8.dp)
                        .padding(start = 24.dp, end = 8.dp)
                ) {
                    Text(text = "Alarms", fontSize = 24.sp)
                    if (!homeUiState.alarmEditEnabled)
                        Row(
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(
                                onClick = {
                                    navController.navigate(AlarmScreen.CreateAlarm.name)
                                }
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Create alarm"
                                )
                            }
                            Box(modifier = modifier.wrapContentSize(Alignment.CenterEnd)) {
                                IconButton(onClick = { homeViewModel.selectOptions() }) {
                                    Icon(
                                        Icons.Default.MoreVert,
                                        contentDescription = "More options"
                                    )
                                }
                                DropdownMenu(
                                    expanded = homeUiState.optionsExpanded,
                                    onDismissRequest = {
                                        homeViewModel.dismissDropdown()
                                        homeViewModel.resetUiState()
                                    }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Turn all on/off") },
                                        onClick = {
                                            homeViewModel.dismissDropdown()
                                            val allEnabled = homeViewModel.enableAllAlarms(alarmData)
                                            alarmData.forEach { alarm ->
                                                alarm.enabled = allEnabled
                                                alarmDatabaseViewModel.updateAlarm(alarm)
                                            }
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Edit") },
                                        onClick = {
                                            homeViewModel.dismissDropdown(editEnabled = true)
                                        }
                                    )
                                }
                            }
                        }
                    else
                        IconButton(
                            onClick = {
                                if (homeUiState.selectedAlarms.isNotEmpty()) {
                                    homeUiState.selectedAlarms.forEach {
                                        alarmDatabaseViewModel.deleteAlarm(it)
                                    }
                                    homeViewModel.cancelAlarmsEdit()
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete alarm"
                            )
                        }
                }

                LazyColumn(
                    modifier = modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(alarmData) {alarm ->
                        AlarmCard(
                            alarm = alarm,
                            navController = navController,
                            alarmDatabaseViewModel = alarmDatabaseViewModel,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

        if (homeUiState.alarmEditEnabled)
            Row(
                modifier = modifier
                    .weight(0.075f)
                    .fillMaxWidth()
                    .background(color = Color(0xFFF0F0F0)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(onClick = { homeViewModel.resetUiState() }) {
                    Text(text = "Cancel", fontSize = 20.sp)
                }
                TextButton(onClick = { homeViewModel.selectAllAlarms(alarmData) }) {
                    Text(text = "Select all", fontSize = 20.sp)
                }
            }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun HomeMenuPreview() {
    HomeMenu(alarmData, rememberNavController())
}
*/