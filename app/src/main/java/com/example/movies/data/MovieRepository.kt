package com.example.movies.data

import com.example.movies.data.dto.MovieDTO
import com.example.movies.ui.ItemMovie
import com.example.movies.ui.ItemReview
import javax.inject.Inject

class MovieRepository @Inject constructor
    (private val movieService: MovieService) {

    suspend fun getMovies(): List<ItemMovie> {
        return movieService.getMovies().results.map {
            ItemMovie(
                it.id,
                it.title,
                it.voteAverage,
                //TODO figure out how to get the image link and the genres properly
                it.backdropPath,
                it.releaseDate,
                it.overview
            )
        }
    }

    suspend fun getReviews(id: Int): List<ItemReview> {
        return movieService.getReviews(id).results.map {
            ItemReview (
                it.id,
                it.author,
                it.authorDetails.rating.toString(),
                it.id,
                it.updatedAt,
                it.content
            )
        }
    }

    suspend fun getMovieDetails(id: Int): MovieDTO {
        return movieService.getMovieDetails(id)
    }
}
