// HighScoreRepository.kt
package com.example.tetris.data.repository

import com.example.tetris.data.model.HighScore
import com.example.tetris.data.local.HighScoreDao
import javax.inject.Inject

class HighScoreRepository @Inject constructor(
    private val highScoreDao: HighScoreDao
) {
    suspend fun getHighScore(): HighScore? = highScoreDao.getHighScore()

    suspend fun saveHighScore(score: Int) {
        highScoreDao.saveHighScore(HighScore(id = 1, score = score))
    }
}