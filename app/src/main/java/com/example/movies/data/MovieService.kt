package com.example.movies.data

import com.example.movies.data.dto.MovieDetailsDTO
import com.example.movies.data.dto.MovieWatchlist
import com.example.movies.data.dto.Reviews
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {
    @GET("/account/{account_id}/movie/watchlist")
    suspend fun getMovies(): MovieWatchlist

    @GET("/movie/{movie_id}/reviews")
    suspend fun getReviews(@Path("id") id: Int): Reviews

    @GET("/movie/{movie_id}")
    suspend fun getMovieDetails(@Path("id") id: Int): MovieDetailsDTO

}