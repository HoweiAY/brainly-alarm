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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MemoryGame(gridSize: Int = 3){
    TileBoard(gridSize = gridSize)
}

enum class TileState{
    DEFAULT,
    SHOWING,
    CORRECT,
    INCORRECT
}

@Composable
private fun TileBoard(
    gridSize: Int = 3,
) {
    var gameStarted by remember { mutableStateOf(false) }
    var flippingOrder by remember { mutableStateOf(emptyList<Int>()) }
    var currentPlayerIndex by remember { mutableStateOf(0) }
    var playerTurn by remember { mutableStateOf(false) }
    val gridItems = remember { mutableStateListOf<TileState>().apply { repeat(9) { add(TileState.DEFAULT) } } }

    suspend fun start() {
        playerTurn = false
        currentPlayerIndex = 0
        gridItems.clear()
        gridItems.addAll(List(9) { TileState.DEFAULT })
        flippingOrder = (0 until gridSize * gridSize).toList().shuffled().take(4)

        for (index in flippingOrder) {
            delay(500)
            gridItems[index] = TileState.SHOWING
            delay(500)
            gridItems[index] = TileState.DEFAULT
        }
        playerTurn = true
    }

    suspend fun handleWrongTileClick(index: Int) {
        gridItems[index] = TileState.INCORRECT
        playerTurn = false
        delay(500)
        gridItems.clear()
        gridItems.addAll(List(9) { TileState.DEFAULT })
        currentPlayerIndex = 0
        playerTurn = true
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                CoroutineScope(MainScope().coroutineContext).launch { start() }
            },
        ){
            Text(text = "Start")
        }
        repeat(gridSize) { row ->
            Row {
                repeat(gridSize) { column ->
                    val index = row * gridSize + column
                    val tileState = gridItems[index]

                    Surface(
                        modifier = Modifier
                            .size(64.dp)
                            .clickable(enabled = playerTurn && tileState == TileState.DEFAULT) {
                                // Handle square click
                                CoroutineScope(MainScope().coroutineContext).launch {
                                    if (flippingOrder[currentPlayerIndex] == index) {
                                        // When clicked on correct tile
                                        gridItems[index] = TileState.CORRECT
                                        currentPlayerIndex++
                                        if (currentPlayerIndex >= flippingOrder.size) {
                                            currentPlayerIndex = 0
                                            playerTurn = false
                                            gameStarted = false
                                        }
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
                        border = BorderStroke(width = 2.dp, color = Color.Black)
                    ) {
                        // Content of each square in the grid
                    }
                }
            }
        }
        Text(text = currentPlayerIndex.toString())
    }
}

@Preview
@Composable
fun PreviewMemoryGame() {
    MemoryGame()
}