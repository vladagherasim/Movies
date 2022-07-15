package com.example.movies.ui.pagingSources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movies.data.MovieService
import com.example.movies.data.dto.MovieDTO

class RegularMoviePagingSource(private val service: MovieService) :
    PagingSource<Int, MovieDTO>() {
    private val key = "d88664a2e2c16e8647ce06f3a02cc096"
    override suspend fun load(params: LoadParams<Int>): LoadResult.Page<Int, MovieDTO> {
        val start = params.key ?: 0
        val response = service.getMovies(key, params.key ?: 1)
        return LoadResult.Page(
            data = response.results,
            prevKey = when (start) {
                0 -> null
                else -> response.page - 1
            },
            nextKey = response.page + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDTO>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    }
}