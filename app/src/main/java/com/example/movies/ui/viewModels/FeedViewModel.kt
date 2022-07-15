package com.example.movies.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.movies.data.MovieRepository
import com.example.movies.ui.ItemMovie
import com.example.movies.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {

    private val _exceptions = MutableLiveData<Exception>()
    val exceptions = _exceptions.toLiveData()

    fun getMovies(movieTitle: String): Flow<PagingData<ItemMovie>> {
        return if (movieTitle.isBlank()) {
            repository.getMovies(viewModelScope)
        } else {
            repository.getSearchResults(movieTitle, viewModelScope)
        }
    }

}