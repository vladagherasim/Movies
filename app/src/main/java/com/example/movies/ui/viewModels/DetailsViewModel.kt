package com.example.movies.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.data.MovieRepository
import com.example.movies.data.dto.MovieDTO
import com.example.movies.utils.assignValue
import com.example.movies.utils.createLiveData
import com.example.movies.utils.launchOn
import com.example.movies.utils.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {
    private val _movieData = createLiveData<MovieDTO>()
    val movieData = _movieData.toLiveData()

    private val _exceptions = MutableLiveData<Exception>()
    val exceptions = _exceptions.toLiveData()

    private val _totalReviews = createLiveData<Int>()
    val totalReviews = _totalReviews.toLiveData()

    private val key = "d88664a2e2c16e8647ce06f3a02cc096"

    fun getReviewsNumber(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            launchOn {
                repository.getReviewsNumber(key, id)
            }.subscribeOver(_exceptions) {
                collectLatest {
                    _totalReviews.assignValue(it)
                }
            }
        }
    }

    fun getMovie(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            launchOn {
                repository.getMovieDetails(id, key)
            }.subscribeOver(_exceptions) {
                _movieData.assignValue(this)
            }
        }
    }
}