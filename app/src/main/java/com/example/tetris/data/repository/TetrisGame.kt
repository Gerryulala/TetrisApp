package com.example.tetris.data.repository

import com.example.tetris.data.model.Cell

class TetrisGame(
    val rows: Int = 20,
    val columns: Int = 10
) {
    val board: Array<Array<Cell>> = Array(rows) {
        Array(columns) { Cell(false, androidx.compose.ui.graphics.Color.Black.hashCode()) }
    }

    fun clearFullRows(): List<Int> {
        val fullRows = board.indices.filter { row ->
            board[row].all { it.isFilled }
        }

        for (row in fullRows) {
            for (r in row downTo 1) {
                board[r] = board[r - 1].map { it.copy() }.toTypedArray()
            }
            board[0] = Array(columns) { Cell(false, androidx.compose.ui.graphics.Color.Black.hashCode()) }
        }

        return fullRows
    }
}
