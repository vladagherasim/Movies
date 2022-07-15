package com.example.movies.data

import android.annotation.SuppressLint
import androidx.paging.*
import com.example.movies.data.database.Genre
import com.example.movies.data.database.GenreDao
import com.example.movies.data.dto.MovieDTO
import com.example.movies.ui.ItemMovie
import com.example.movies.ui.ItemReview
import com.example.movies.ui.pagingSources.RegularMoviePagingSource
import com.example.movies.ui.pagingSources.SearchMoviePagingSource
import com.example.movies.utils.DateManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class MovieRepository @Inject constructor
    (
    private val movieService: MovieService,
    private val genreDao: GenreDao
) {
    private fun regularMoviePagingSource(coroutineScope: CoroutineScope) = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { RegularMoviePagingSource(movieService) }
    ).flow.cachedIn(coroutineScope)

    private fun searchMoviePagingSource(searchString: String, coroutineScope: CoroutineScope) =
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { SearchMoviePagingSource(movieService, searchString) }
        ).flow.cachedIn(coroutineScope)

    fun getMovies(coroutineScope: CoroutineScope): Flow<PagingData<ItemMovie>> {
        return combine(
            regularMoviePagingSource(coroutineScope),
            genreDao.getGenres()
        ) { remote, db ->
            val genres = db.map { Genre(it.id, it.name) }
            remote.map { movie ->
                ItemMovie(
                    movie.id,
                    movie.title,
                    movie.voteAverage,
                    "https://image.tmdb.org/t/p/original${movie.posterPath}",
                    movie.releaseDate.take(4),
                    movie.genres.mapNotNull { genreID ->
                        genres.find { genre ->
                            genre.id == genreID
                        }?.name
                    }.joinToString(separator = ", ")
                )
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    suspend fun getReviews(id: Int, key: String): List<ItemReview> {
        return movieService.getReviews(id, key).results.map {
            val rating = it.authorDetails.rating ?: ""
            ItemReview(
                it.id,
                it.authorDetails.username,
                rating.toString(),
                it.authorDetails.name,
                DateManager.convertFromISO(it.updatedAt),
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

    suspend fun getReviewsNumber(key: String, id: Int): Flow<Int> {
        return flowOf(movieService.getReviews(id, key).totalResults)
    }

    fun getSearchResults(
        movieTitle: String,
        coroutineScope: CoroutineScope
    ): Flow<PagingData<ItemMovie>> {
        return combine(
            searchMoviePagingSource(movieTitle.replace("\\s".toRegex(), "+"), coroutineScope),
            genreDao.getGenres()
        ) { remote, db ->
            val genres = db.map { Genre(it.id, it.name) }
            remote.map { movie ->
                ItemMovie(
                    movie.id,
                    movie.title,
                    movie.voteAverage,
                    "https://image.tmdb.org/t/p/original${movie.posterPath}",
                    movie.releaseDate?.take(4) ?: "",
                    movie.genreIds.mapNotNull { genreID ->
                        genres.find { genre ->
                            genre.id == genreID
                        }?.name
                    }.joinToString(separator = ", ")
                )
            }
        }
    }

}

