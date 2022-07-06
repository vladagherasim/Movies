package com.example.movies.data.database

import androidx.room.*

@Database(entities = [Genre::class], version = 1, exportSchema = false)
abstract class GenreDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao

    companion object {
        const val DATABASE_NAME = "genres_database"
    }
}