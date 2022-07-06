package com.example.movies.data

import com.example.movies.data.database.Genre
import com.example.movies.data.database.GenreDao
import com.example.movies.data.dto.MovieDTO
import com.example.movies.ui.ItemMovie
import com.example.movies.ui.ItemReview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class MovieRepository @Inject constructor
    (
    private val movieService: MovieService,
    private val genreDao: GenreDao
) {

    suspend fun getMovies(key: String): Flow<List<ItemMovie>> {
        return combine(
            flowOf(movieService.getMovies(key)), genreDao.getGenres()
        ) { remote, db ->
            val genres = db.map { Genre(it.id, it.name) }
            remote.results.map { movie ->
                ItemMovie(
                    movie.id,
                    movie.title,
                    movie.voteAverage,
                    movie.posterPath.toString(),
                    movie.releaseDate.take(4),
                    movie.genres.mapNotNull { genreID ->
                        genres.find {
                            it.id == genreID
                        }?.name
                    }.joinToString(separator = ", ")
                )
            }
        }
    }

    suspend fun getReviews(id: Int, key: String): List<ItemReview> {
        return movieService.getReviews(id, key).results.map {
            ItemReview(
                it.id,
                it.author,
                it.authorDetails.rating.toString(),
                it.url,
                it.updatedAt,
                it.content
            )
        }
    }

    suspend fun getMovieDetails(id: Int, key: String): MovieDTO {
        return movieService.getMovieDetails(id, key)
    }

    suspend fun getGenresToDatabase(key: String) {
        val genres = movieService.getGenres(key).genres
        genres.forEach { genre ->
            genreDao.insert(
                Genre(
                    genre.id,
                    genre.name
                )
            )
        }
    }

    suspend fun getSearchResults(key: String, movieTitle: String): Flow<List<ItemMovie>> {
        return combine(
            flowOf(movieService.searchMovie(key, movieTitle.replace("\\s".toRegex(), "+"))),
            genreDao.getGenres()
        ) { remote, db ->
            val genres = db.map { Genre(it.id, it.name) }
            remote.searchItems.map { movie ->
                ItemMovie(
                    movie.id,
                    movie.title,
                    movie.voteAverage,
                    movie.posterPath ?: "",
                    movie.releaseDate?.take(4) ?: "",
                    movie.genreIds.mapNotNull { genreID ->
                        genres.find {
                            it.id == genreID
                        }?.name
                    }.joinToString(separator = ", ")
                )
            }
        }
    }

}

