package com.example.tetris.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tetris.data.model.Cell
import com.example.tetris.data.model.HighScore
import com.example.tetris.data.model.TetrominoFactory
import com.example.tetris.data.model.TetrominoType
import com.example.tetris.data.repository.HighScoreRepository
import com.example.tetris.data.repository.TetrisGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val highScoreRepository: HighScoreRepository
) : ViewModel() {

    private val _tetromino = MutableStateFlow(TetrominoFactory.random())
    val tetromino: StateFlow<com.example.tetris.data.model.Tetromino> = _tetromino

    private val _offset = MutableStateFlow(0)
    val offset: StateFlow<Int> = _offset

    private val _gameOver = MutableStateFlow(false)
    val gameOver: StateFlow<Boolean> = _gameOver

    private val _flashingRows = MutableStateFlow<List<Int>>(emptyList())
    val flashingRows: StateFlow<List<Int>> = _flashingRows

    private val _game = TetrisGame()
    val game: TetrisGame = _game

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _highScore = MutableStateFlow(0)
    val highScore: StateFlow<Int> = _highScore

    private val _topScores = MutableStateFlow<List<HighScore>>(emptyList())
    val topScores: StateFlow<List<HighScore>> = _topScores

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused
    private var lastSavedScore = -1

    var onPlaySound: ((SoundEffect) -> Unit)? = null

    enum class SoundEffect {
        LINE_CLEAR,
        GAME_OVER
    }

    fun pauseGame() {
        _isPaused.value = true
    }

    fun resumeGame() {
        _isPaused.value = false
    }

    init {
        viewModelScope.launch {
            val top = highScoreRepository.getTopScores()
            _topScores.value = top
            _highScore.value = top.firstOrNull()?.score ?: 0
        }
        startFalling()
    }

    private fun startFalling() {
        viewModelScope.launch {
            while (!_gameOver.value) {
                delay(1000L)
                if (!_isPaused.value) {
                    moveDown()
                }
            }
        }
    }

    private fun moveDown() {
        val newOffset = _offset.value + 1
        val isBlocked = collides(_tetromino.value.shape, newOffset)

        if (isBlocked) {
            fixPieceAndSpawnNew()
        } else {
            _offset.value = newOffset
        }
    }

    fun softDrop() = moveDown()

    fun hardDrop() {
        while (true) {
            val newOffset = _offset.value + 1
            val isBlocked = collides(_tetromino.value.shape, newOffset)
            if (isBlocked) {
                fixPieceAndSpawnNew()
                break
            } else {
                _offset.value = newOffset
            }
        }
    }

    private fun fixPieceAndSpawnNew() {
        for ((row, col) in _tetromino.value.shape) {
            val boardRow = row + _offset.value
            if (boardRow in 0 until game.rows && col in 0 until game.columns) {
                game.board[boardRow][col] = Cell(true, _tetromino.value.color)
            }
        }

        val cleared = game.clearFullRows()
        if (cleared.isNotEmpty()) {
            _score.value += when (cleared.size) {
                1 -> 100
                2 -> 300
                3 -> 500
                else -> 800
            }
            _flashingRows.value = cleared
            onPlaySound?.invoke(SoundEffect.LINE_CLEAR)  // 🔈

            viewModelScope.launch {
                delay(150L)
                _flashingRows.value = emptyList()
            }
        }

        val newPiece = TetrominoFactory.random()
        if (collides(newPiece.shape, 0)) {
            saveCurrentScore()
            onPlaySound?.invoke(SoundEffect.GAME_OVER) // 🔈
            _gameOver.value = true
        } else {
            _tetromino.value = newPiece
            _offset.value = 0
        }
    }

    private fun collides(shape: List<Pair<Int, Int>>, offsetY: Int): Boolean {
        return shape.any { (row, col) ->
            val newRow = row + offsetY
            newRow >= game.rows ||
                    col !in 0 until game.columns ||
                    game.board.getOrNull(newRow)?.getOrNull(col)?.isFilled == true
        }
    }

    fun moveLeft() {
        val movedShape = tetromino.value.shape.map { (r, c) -> r to c - 1 }
        val isValid = movedShape.all { (_, col) -> col >= 0 } &&
                !collides(movedShape, offset.value)
        if (isValid) {
            _tetromino.value = tetromino.value.copy(shape = movedShape)
        }
    }

    fun moveRight() {
        val movedShape = tetromino.value.shape.map { (r, c) -> r to c + 1 }
        val isValid = movedShape.all { (_, col) -> col < game.columns } &&
                !collides(movedShape, offset.value)
        if (isValid) {
            _tetromino.value = tetromino.value.copy(shape = movedShape)
        }
    }

    fun rotate() {
        val current = tetromino.value
        if (current.type == TetrominoType.O) return

        val pivot = current.shape[1]
        val rotatedShape = when (current.type) {
            TetrominoType.I -> {
                val isHorizontal = current.shape.all { it.first == current.shape[0].first }
                if (isHorizontal) {
                    listOf(
                        pivot.first - 1 to pivot.second,
                        pivot.first to pivot.second,
                        pivot.first + 1 to pivot.second,
                        pivot.first + 2 to pivot.second
                    )
                } else {
                    listOf(
                        pivot.first to pivot.second - 1,
                        pivot.first to pivot.second,
                        pivot.first to pivot.second + 1,
                        pivot.first to pivot.second + 2
                    )
                }
            }

            else -> {
                current.shape.map { (r, c) ->
                    val dr = r - pivot.first
                    val dc = c - pivot.second
                    val newR = pivot.first - dc
                    val newC = pivot.second + dr
                    newR to newC
                }
            }
        }

        val isValid = rotatedShape.all { (r, c) ->
            r + offset.value in 0 until game.rows && c in 0 until game.columns
        } && !collides(rotatedShape, offset.value)

        if (isValid) {
            _tetromino.value = current.copy(shape = rotatedShape)
        }
    }

    fun restartGame() {
        for (row in 0 until game.rows) {
            for (col in 0 until game.columns) {
                game.board[row][col] = Cell(false, androidx.compose.ui.graphics.Color.Black.hashCode())
            }
        }

        saveCurrentScore()

        _tetromino.value = TetrominoFactory.random()
        _offset.value = 0
        _score.value = 0
        _gameOver.value = false

        startFalling()
    }

    private fun saveCurrentScore() = viewModelScope.launch {
        val currentScore = _score.value
        if (currentScore > 0 && currentScore != lastSavedScore) {
            highScoreRepository.saveScore(currentScore)
            lastSavedScore = currentScore
            _topScores.value = highScoreRepository.getTopScores()
            if (currentScore > _highScore.value) {
                _highScore.value = currentScore
            }
        }
    }

    fun startGame() {
        _isPlaying.value = true
        _tetromino.value = TetrominoFactory.random()
        _offset.value = 0
        _score.value = 0
        _gameOver.value = false
        startFalling()
    }

    fun exitGame() {
        _isPlaying.value = false
    }
}
