package com.example.movies.data.modules

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movies.data.database.GenreDao
import com.example.movies.data.database.GenreDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            GenreDatabase::class.java,
            GenreDatabase.DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun provideDao(database: GenreDatabase): GenreDao = database.genreDao()
}