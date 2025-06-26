package com.example.tetris.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tetris.data.repository.TetrisGame

data class Cell(val filled: Boolean)

typealias Board = List<List<Cell>>

@Composable
fun TetrisBoard(game: TetrisGame) {
    val flatBoard = game.board.flatten()

    LazyVerticalGrid(
        columns = GridCells.Fixed(game.columns),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .aspectRatio(game.columns / game.rows.toFloat())
    ) {
        items(flatBoard.size) { index ->
            val cell = flatBoard[index]
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .background(Color(cell.color))
                    .padding(1.dp)
            )
        }
    }
}
fun createEmptyBoard(rows: Int = 20, columns: Int = 10): Board {
    return List(rows) {
        List(columns) {
            Cell(filled = false)
        }
    }
}
