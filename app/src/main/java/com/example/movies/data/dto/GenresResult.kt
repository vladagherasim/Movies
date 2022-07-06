package com.example.movies.data.dto


import com.google.gson.annotations.SerializedName

data class GenresResult(
    @SerializedName("genres")
    val genres: List<GenreDTO>
)