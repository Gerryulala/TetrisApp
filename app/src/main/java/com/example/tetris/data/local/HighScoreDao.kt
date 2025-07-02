package com.example.tetris.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tetris.data.model.HighScore

@Dao
interface HighScoreDao {

    @Query("SELECT * FROM high_score ORDER BY score DESC, date ASC LIMIT 5")
    suspend fun getTopScores(): List<HighScore>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertScore(highScore: HighScore): Long
}
