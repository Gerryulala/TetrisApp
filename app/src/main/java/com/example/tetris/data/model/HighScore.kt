package com.example.tetris.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "high_score")
data class HighScore(
    @PrimaryKey val id: Int = 1, // Siempre será 1, ya que solo hay un high score
    val score: Int
)
