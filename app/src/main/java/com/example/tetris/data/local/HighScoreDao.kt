package com.example.tetris.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tetris.data.model.HighScore

@Dao
interface HighScoreDao {
    @Query("SELECT * FROM high_score WHERE id = 1")
    suspend fun getHighScore(): HighScore?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveHighScore(highScore: HighScore)
}
