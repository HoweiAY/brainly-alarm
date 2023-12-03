package com.example.alarmapp.components.tasks

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alarmapp.components.alarm.AlarmViewModel
import kotlinx.coroutines.delay
import net.objecthunter.exp4j.ExpressionBuilder


@Composable
fun MathEquation(
    difficulty: String = "Easy",
    rounds: Int = 1,
    context: Context = LocalContext.current.applicationContext,
    stopAlarmSound: () -> Unit,
) {
    val alarmViewModel = AlarmViewModel(context)

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val desiredWidth = with(LocalDensity.current) { screenWidth * 0.8f }

    var equation by remember { mutableStateOf(generateEquation(difficulty)) }
    var input by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var currentRound by remember { mutableIntStateOf(1) }

    fun handleTaskCompleted(){
        stopAlarmSound()
        alarmViewModel.onAlarmDismissed(context)
        //(context as? Activity)?.finish()
    }

    LaunchedEffect(isCorrect) {
        delay(1000)
        isCorrect = null
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$currentRound/$rounds",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "What is the result of the expression?",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = equation,
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = input,
            onValueChange = { input = it },
            label = { Text(text = "Answer") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier
                .width(desiredWidth)
                .padding(bottom = 16.dp)
                .height(56.dp),
            textStyle = TextStyle(fontSize = 18.sp)
        )

        Button(
            onClick = {
                if (isCorrect == null) {
                    val expectedAnswer = evaluateExpression(equation)

                    if (input.isNotEmpty() && input.toIntOrNull() == expectedAnswer) {
                        isCorrect = true
                        if (currentRound == rounds) {
                            handleTaskCompleted()
                        } else {
                            currentRound++
                            equation = generateEquation(difficulty)
                        }
                    } else {
                        isCorrect = false
                    }
                    input = ""
                }
            },
            modifier = Modifier
                .width(desiredWidth/2)
                .padding(bottom = 16.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6200EE),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            if (isCorrect == null){
                Text(
                    text = "Submit",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            } else {
                isCorrect?.let { isCorrect ->
                    ResultIcon(isCorrect)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun ResultIcon(isCorrect: Boolean) {
    val alpha by animateFloatAsState(targetValue = 1f, label = "")

    Icon(
        imageVector = if(isCorrect) Icons.Default.Check else Icons.Default.Close,
        tint = if(isCorrect) Color.Green else Color.Red,
        contentDescription = null,
        modifier = Modifier
            .padding(4.dp)
            .alpha(alpha)
    )
}

fun generateEquation(difficulty: String): String {
    val numbers = when(difficulty){
        "Easy" -> List(2) { (1..30).random() }
        "Normal" -> List(3) { (1..50).random() }
        "Hard" -> List(3) { (1..20).random() }
        else -> List(3) { (1..20).random() }
    }

    val operators = mutableListOf("+", "-")
    if (difficulty == "Hard") operators.add("*")

    val expression = StringBuilder()

    for (i in numbers.indices) {
        expression.append(numbers[i])

        if (i < numbers.size - 1) {
            expression.append(" ${operators.random()} ")
        }
    }

    return expression.toString()
}

fun evaluateExpression(expression: String): Int {
    return try {
        val result = ExpressionBuilder(expression).build().evaluate().toInt()
        result
    } catch (e: Exception) {
        0
    }
}

