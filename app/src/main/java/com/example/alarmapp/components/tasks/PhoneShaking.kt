package com.example.alarmapp.components.tasks

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alarmapp.R
import com.example.alarmapp.components.alarm.AlarmViewModel

@Composable
fun PhoneShaking(
    context: Context = LocalContext.current.applicationContext,
    stopAlarmSound: () -> Unit,
) {
    val alarmViewModel = AlarmViewModel(context)
    var remainingShakeTime by remember { mutableIntStateOf((15..30).random()) }

    val sensorManager = LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    val threshold = 11f
    val shakeTimeout = 150

    var lastShakeTime: Long = 0

    fun handleTaskCompleted(){
        stopAlarmSound()
        alarmViewModel.onAlarmDismissed(context)
        //(context as? Activity)?.finish()
    }

    val sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val acceleration = kotlin.math.sqrt((x * x + y * y + z * z).toDouble())

                if (acceleration > threshold) {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastShakeTime > shakeTimeout) {
                        lastShakeTime = currentTime
                        if (remainingShakeTime == 0){
                            handleTaskCompleted()
                        } else {
                            remainingShakeTime--
                        }
                    }
                }
            }
        }
    }


    DisposableEffect(Unit) {
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Shake your phone to stop the alarm!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp),
            color = Color(0xFF03A9F4)
        )
        Text(
            text = "$remainingShakeTime ${if (remainingShakeTime > 1) "shakes" else "shake"} to go!",
            fontSize = 18.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Image(
            painter = painterResource(R.drawable.shake),
            contentDescription = "shake",
            modifier = Modifier
                .size(200.dp)
                .padding(vertical = 16.dp)
        )
    }

}

@Preview
@Composable
fun PhoneShakingPreview() {
    //PhoneShaking()
}