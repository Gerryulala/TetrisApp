package com.example.tetris.data.model

enum class TetrominoType {
    I, O, T, S, Z, J, L
}

data class Tetromino(
    val type: TetrominoType,
    val shape: List<Pair<Int, Int>>,
    val color: Int
)

object TetrominoFactory {
    fun create(type: TetrominoType): Tetromino {
        val (shape, color) = when (type) {
            TetrominoType.I -> Pair(
                listOf(0 to 3, 0 to 4, 0 to 5, 0 to 6),
                0xFF00FFFF.toInt() // Cyan
            )
            TetrominoType.O -> Pair(
                listOf(0 to 4, 0 to 5, 1 to 4, 1 to 5),
                0xFFFFFF00.toInt() // Yellow
            )
            TetrominoType.T -> Pair(
                listOf(0 to 4, 1 to 3, 1 to 4, 1 to 5),
                0xFF800080.toInt() // Purple
            )
            TetrominoType.S -> Pair(
                listOf(0 to 4, 0 to 5, 1 to 3, 1 to 4),
                0xFF00FF00.toInt() // Green
            )
            TetrominoType.Z -> Pair(
                listOf(0 to 3, 0 to 4, 1 to 4, 1 to 5),
                0xFFFF0000.toInt() // Red
            )
            TetrominoType.J -> Pair(
                listOf(0 to 3, 1 to 3, 1 to 4, 1 to 5),
                0xFF0000FF.toInt() // Blue
            )
            TetrominoType.L -> Pair(
                listOf(0 to 5, 1 to 3, 1 to 4, 1 to 5),
                0xFFFFA500.toInt() // Orange
            )
        }
        return Tetromino(type, shape, color)
    }

    fun random(): Tetromino {
        val types = TetrominoType.values()
        return create(types.random())
    }
}
