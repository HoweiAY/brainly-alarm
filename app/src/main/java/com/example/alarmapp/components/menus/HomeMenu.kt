package com.example.alarmapp.components.menus

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.alarmapp.components.alarms.AlarmCard
import com.example.alarmapp.model.Alarm
import com.example.alarmapp.model.data.alarmData

@Composable
fun HomeMenu(
    alarmData: List<Alarm>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(alarmData) {alarm ->
            AlarmCard(alarm)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview
@Composable
fun HomeMenuPreview() {
    HomeMenu(alarmData)
}