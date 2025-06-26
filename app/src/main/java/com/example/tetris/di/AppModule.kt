
package com.example.tetris.di

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import androidx.room.Room
import com.example.tetris.data.local.HighScoreDao
import com.example.tetris.data.local.TetrisDatabase
import com.example.tetris.data.repository.HighScoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): TetrisDatabase =
        Room.databaseBuilder(appContext, TetrisDatabase::class.java, "tetris_db").build()

    @Provides
    fun provideHighScoreDao(db: TetrisDatabase): HighScoreDao = db.highScoreDao()

    @Provides
    fun provideHighScoreRepository(highScoreDao: HighScoreDao): HighScoreRepository =
        HighScoreRepository(highScoreDao)
}
