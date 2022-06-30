package com.example.movies.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.data.MovieRepository
import com.example.movies.ui.ItemMovie
import com.example.movies.utils.assignValue
import com.example.movies.utils.createListedLiveData
import com.example.movies.utils.launchOn
import com.example.movies.utils.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {

    private val _movies = createListedLiveData<ItemMovie>()
    val movies = _movies.toLiveData()

    private val _exceptions = MutableLiveData<Exception>()
    val exceptions = _exceptions.toLiveData()

    init {
        getMovies()
    }

    private fun getMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            launchOn {
                repository.getMovies()
            }.subscribeOver(_exceptions) {
              _movies.assignValue(this)
            }
        }
    }

}