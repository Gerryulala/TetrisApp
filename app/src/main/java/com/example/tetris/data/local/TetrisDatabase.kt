package com.example.tetris.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tetris.data.model.HighScore

@Database(entities = [HighScore::class], version = 1, exportSchema = false)
abstract class TetrisDatabase : RoomDatabase() {
    abstract fun highScoreDao(): HighScoreDao
}
