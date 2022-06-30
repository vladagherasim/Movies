package com.example.movies.data

import com.example.movies.data.dto.MovieDTO
import com.example.movies.data.dto.MovieList
import com.example.movies.data.dto.Reviews
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {
    @GET("/discover/movie")
    suspend fun getMovies(): MovieList

    @GET("/movie/{movie_id}/reviews")
    suspend fun getReviews(@Path("id") id: Int): Reviews

    @GET("/movie/{movie_id}")
    suspend fun getMovieDetails(@Path("id") id: Int): MovieDTO

}