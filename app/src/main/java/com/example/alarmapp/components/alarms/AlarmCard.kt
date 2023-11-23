package com.example.alarmapp.components.alarms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alarmapp.model.Alarm
import com.example.alarmapp.model.data.alarmData

@Composable
fun AlarmCard(
    alarm: Alarm,
    modifier: Modifier = Modifier
) {
    var checked by remember { mutableStateOf(true) }
    Card(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(64.dp)
        ) {
            Text(text = alarm.getTimeString(), fontSize = 30.sp)
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                }
            )

        }
    }
}

@Preview
@Composable
fun AlarmCardPreview() {
    AlarmCard(alarmData[0])
}