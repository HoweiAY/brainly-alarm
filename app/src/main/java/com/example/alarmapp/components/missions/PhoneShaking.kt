package com.example.alarmapp.components.missions

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.alarmapp.R

@Composable
fun PhoneShaking(shakeTime: Int = 10){
    var remainingShakeTime by remember { mutableStateOf(shakeTime) }
    var isShaking by remember { mutableStateOf(false) }

    val sensorManager = LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    val threshold = 11f
    val shakeTimeout = 150

    var lastShakeTime: Long = 0

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
                        isShaking = true
                        lastShakeTime = currentTime
                        remainingShakeTime--
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
        Text(text = "$remainingShakeTime")
        Image(
            painter = painterResource(R.drawable.shake),
            contentDescription = "shake",
            modifier = Modifier.size(200.dp)
        )
        if (isShaking) {
            Text(text = "Shaking detected!")
        } else {
            Text(text = "No shaking")
        }
    }

}

@Preview
@Composable
fun PhoneShakingPreview() {
    PhoneShaking()
}