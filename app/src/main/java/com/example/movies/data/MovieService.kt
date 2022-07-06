package com.example.movies.data

import com.example.movies.data.dto.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("3/discover/movie")
    suspend fun getMovies(@Query("api_key") apiKey: String): MovieList

    @GET("3/movie/{movie_id}/reviews")
    suspend fun getReviews(@Path("movie_id") id: Int, @Query("api_key") apiKey: String): Reviews

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String
    ): MovieDTO

    @GET("3/genre/movie/list")
    suspend fun getGenres(@Query("api_key") apiKey: String): GenresResult

    @GET("/3/search/movie")
    suspend fun searchMovie(
        @Query("api_key") apiKey: String,
        @Query("query") movieTitle: String
    ): SearchResult
}