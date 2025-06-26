package com.example.tetris

import com.example.tetris.data.model.Cell
import com.example.tetris.data.repository.TetrisGame
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals

class TetrisGameTest {

    private lateinit var game: TetrisGame

    @Before
    fun setup() {
        game = TetrisGame()
    }

    @Test
    fun `tablero inicia vacío`() {
        for (row in game.board) {
            for (cell in row) {
                assertFalse(cell.isFilled)
            }
        }
    }

    @Test
    fun `agrega celda llena y detecta fila completa`() {
        for (col in 0 until game.columns) {
            game.board[0][col] = Cell(true, 0xFFFFFF)
        }

        val cleared = game.clearFullRows()
        assertEquals(listOf(0), cleared)

        for (col in 0 until game.columns) {
            assertFalse(game.board[0][col].isFilled)
        }
    }

    @Test
    fun `no borra fila incompleta`() {
        game.board[0][0] = Cell(true, 0xFFFFFF)
        val cleared = game.clearFullRows()
        assertTrue(cleared.isEmpty())
    }
}
