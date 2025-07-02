package com.example.tetris.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "high_score")
data class HighScore(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,   // 🔸 ahora autogenerado
    val score: Int,
    val date: Long = System.currentTimeMillis()         // 🔸 marca de tiempo opcional
)
