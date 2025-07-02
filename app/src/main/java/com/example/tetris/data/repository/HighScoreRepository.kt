package com.example.tetris.data.repository

import com.example.tetris.data.local.HighScoreDao
import com.example.tetris.data.model.HighScore
import javax.inject.Inject

class HighScoreRepository @Inject constructor(
    private val dao: HighScoreDao
) {
    suspend fun getTopScores(): List<HighScore> {
        return dao.getTopScores()
    }

    suspend fun saveScore(score: Int) {
        dao.insertScore(HighScore(score = score))
    }
}
