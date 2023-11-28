package com.example.alarmapp.components.missions

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import net.objecthunter.exp4j.ExpressionBuilder
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.delay

enum class Difficulty{
    EASY,
    MEDIUM,
    HARD
}

@Composable
fun MathEquation(difficulty: Difficulty) {
    var equation by remember { mutableStateOf(generateEquation(difficulty)) }
    var input by remember { mutableStateOf("") }
    var correctCount by remember { mutableStateOf(0) }
    var result by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(result) {
        delay(1000)
        result = null
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            if (result != null) ResultIcon(result)
            Text(text = "${correctCount}/5")
            Text(text = "What is the result of the expression?")
            Text(text = equation)
            TextField(
                value = input,
                onValueChange = { input = it },
                label = { Text(text = "Answer") } ,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
            )
            Button(onClick = {
                val expectedAnswer = evaluateExpression(equation)

                if (input.isNotEmpty() && input.toIntOrNull() == expectedAnswer) {
                    result = "Correct"
                    equation = generateEquation(difficulty)
                    correctCount++
                } else {
                    result = "Wrong"
                }
                input = ""
            }) {
                Text(text = "Submit")
            }
        }
}

@Composable
fun ResultIcon(result: String?) {
    val alpha by animateFloatAsState(targetValue = 1f)

    Icon(
        imageVector = if(result == "Correct") Icons.Default.Check else Icons.Default.Close,
        tint = if(result == "Correct") Color.Green else Color.Red,
        contentDescription = null,
        modifier = Modifier
            .padding(16.dp)
            .alpha(alpha)
    )
}

fun generateEquation(difficulty: Difficulty): String {
    val numbers = when(difficulty){
        Difficulty.EASY -> List(2) { (1..30).random() }
        Difficulty.MEDIUM -> List(3) { (1..50).random() }
        Difficulty.HARD -> List(3) { (1..20).random() }
    }

    val operators = mutableListOf("+", "-")
    if (difficulty == Difficulty.HARD) operators.add("*")

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

