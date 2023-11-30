package com.example.alarmapp.components.alarms

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.alarmapp.AlarmScreen
import com.example.alarmapp.model.data.Alarm
import com.example.alarmapp.viewmodels.AlarmDatabaseViewModel
import com.example.alarmapp.viewmodels.HomeViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlarmCard(
    alarm: Alarm,
    navController: NavHostController,
    alarmDatabaseViewModel: AlarmDatabaseViewModel,
    homeViewModel: HomeViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val homeUiState by homeViewModel.uiState.collectAsState()
    var alarmEnabled by remember { mutableStateOf(alarm.enabled) }
    var alarmSelected by remember { mutableStateOf(false) }

    LaunchedEffect(homeUiState.enabledAlarms) {
        alarmEnabled = homeUiState.enabledAlarms.contains(alarm)
    }

    LaunchedEffect(homeUiState.selectedAlarms) {
        alarmSelected = homeUiState.selectedAlarms.contains(alarm)
    }

    Card(
        modifier = modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = {
                    if (homeUiState.alarmEditEnabled) {
                        homeViewModel.toggleAlarmSelected(alarm)
                        alarmSelected = homeUiState.selectedAlarms.contains(alarm)
                    } else {
                        navController.navigate(
                            "${AlarmScreen.CreateAlarm.name}?alarmId=${alarm.id.toString()}"
                        )
                    }
                },
                onLongClick = {
                    homeViewModel.toggleAlarmSelected(alarm)
                    alarmSelected = homeUiState.selectedAlarms.contains(alarm)
                    homeViewModel.enableEdit()
                },
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(64.dp)
        ) {
            Column() {
                Text(
                    text = alarm.getTimeString(),
                    fontSize = 30.sp
                )
                Text(
                    text = alarm.getDaysString(),
                    fontSize = 12.sp,
                    modifier = modifier.offset(2.dp)
                )
            }

            if (!homeUiState.alarmEditEnabled) {
                alarmSelected = false
                Switch(
                    checked = alarmEnabled,
                    onCheckedChange = {
                        homeViewModel.toggleAlarmEnabled(alarm, !homeUiState.enabledAlarms.contains(alarm))
                        alarm.enabled = !alarm.enabled
                        alarmDatabaseViewModel.updateAlarm(alarm)
                        alarmEnabled = alarm.enabled
                    },
                    modifier = Modifier
                )
            }
            else
                Checkbox(
                    checked = alarmSelected,
                    onCheckedChange = {
                        homeViewModel.toggleAlarmSelected(alarm)
                        alarmSelected = homeUiState.selectedAlarms.contains(alarm)
                    }
                )
        }
    }
}

/*
@Preview
@Composable
fun AlarmCardPreview() {
    AlarmCard(alarmData[0], rememberNavController())
}
*/