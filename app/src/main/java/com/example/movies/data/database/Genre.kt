package com.example.movies.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres_table")
class Genre(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String
)