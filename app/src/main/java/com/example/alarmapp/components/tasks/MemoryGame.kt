package com.example.alarmapp.components.games

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MemoryGame(difficulty: String, rounds: Int){
    TileBoard(difficulty, rounds)
}

enum class TileState{
    DEFAULT,
    SHOWING,
    CORRECT,
    INCORRECT
}

@Composable
private fun TileBoard(
    difficulty: String = "Easy",
    rounds: Int = 2
) {
    val gridSize = when(difficulty){
        "Easy" -> 3
        "Normal" -> 3
        "Hard" -> 4
        else -> 0
    }

    val requiredTileClicks = when(difficulty){
        "Easy" -> 4
        "Normal" -> 5
        "Hard" -> 6
        else -> 0
    }

    var gameStarted by remember { mutableStateOf(false) }
    var flippingOrder by remember { mutableStateOf(emptyList<Int>()) }
    var currentPlayerIndex by remember { mutableIntStateOf(0) }
    var currentRound by remember { mutableIntStateOf(1) }
    var playerTurn by remember { mutableStateOf(false) }
    var titleText by remember { mutableStateOf("") }
    val gridItems = remember { mutableStateListOf<TileState>().apply { repeat(gridSize * gridSize) { add(TileState.DEFAULT) } } }


    suspend fun start() {
        gameStarted = true
        titleText = "Remember the order!"
        playerTurn = false
        currentPlayerIndex = 0
        gridItems.clear()
        gridItems.addAll(List(gridSize * gridSize) { TileState.DEFAULT })
        flippingOrder = (0 until gridSize * gridSize).toList().shuffled().take(requiredTileClicks)
        delay(500)
        for (index in flippingOrder) {
            delay(500)
            gridItems[index] = TileState.SHOWING
            delay(500)
            gridItems[index] = TileState.DEFAULT
        }
        playerTurn = true
        titleText = "Click the tiles in order!"
    }

    fun handleTaskCompleted(){

    }

    suspend fun handleWrongTileClick(index: Int) {
        gridItems[index] = TileState.INCORRECT
        playerTurn = false
        titleText = "Incorrect"
        delay(1000)
        start()
    }

    suspend fun handleCorrectTileClick(index: Int) {
        gridItems[index] = TileState.CORRECT
        currentPlayerIndex++
        if (currentPlayerIndex >= flippingOrder.size) {
            titleText = "Correct"
            delay(1000)
            if (currentRound == rounds) {
                handleTaskCompleted()
            } else {
                currentRound++
                start()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (gameStarted){
            Text(text = "Round: $currentRound/$rounds",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        Text(text = titleText,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        repeat(gridSize) { row ->
            Row {
                repeat(gridSize) { column ->
                    val index = row * gridSize + column
                    val tileState = gridItems[index]

                    Surface(
                        modifier = Modifier
                            .padding(2.dp)
                            .size(64.dp)
                            .clickable(enabled = playerTurn && tileState == TileState.DEFAULT) {
                                // Handle square click
                                CoroutineScope(MainScope().coroutineContext).launch {
                                    if (flippingOrder[currentPlayerIndex] == index) {
                                        // When clicked on correct tile
                                        handleCorrectTileClick(index)
                                    } else {
                                        // When clicked on wrong tile
                                        handleWrongTileClick(index)
                                    }
                                }
                            },
                        shape = RoundedCornerShape(percent = 10),
                        color = when(tileState) {
                            TileState.INCORRECT -> Color.Red
                            TileState.CORRECT -> Color.Green
                            TileState.DEFAULT -> Color.Gray
                            TileState.SHOWING -> Color.Yellow
                        },
                        border = BorderStroke(width = 1.dp, color = Color.Black)
                    ) {
                        // Content of each square in the grid
                    }
                }
            }
        }
        if (!gameStarted) {
            Button(
                onClick = {
                    CoroutineScope(MainScope().coroutineContext).launch { start() }
                },
            ) {
                Text(text = "Start")
            }
        }
    }
}

@Preview
@Composable
fun PreviewMemoryGame() {
    MemoryGame("Easy", 5)
}