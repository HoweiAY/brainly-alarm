package com.example.alarmapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.alarmapp.components.menus.HomeMenu
import com.example.alarmapp.model.data.alarmData

@Preview
@Composable
fun AlarmClockApp(

    modifier: Modifier = Modifier
) {
    HomeMenu(alarmData)
}