package com.example.movies.ui.viewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.data.MovieRepository
import com.example.movies.data.dto.MovieDTO
import com.example.movies.ui.ItemReview
import com.example.movies.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {

    private val _reviews = createListedLiveData<ItemReview>()
    val reviews = _reviews.toLiveData()

    private val _movieData = createLiveData<MovieDTO>()
    val movieData = _movieData.toLiveData()

    private val _exceptions = MutableLiveData<Exception>()
    val exceptions = _exceptions.toLiveData()

    private val _totalReviews = createLiveData<Int>()
    val totalReviews = _totalReviews.toLiveData()

    private val key = "d88664a2e2c16e8647ce06f3a02cc096"

    @RequiresApi(Build.VERSION_CODES.O)
    fun getReviews(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            launchOn {
                repository.getReviews(id, key)
            }.subscribeOver(_exceptions) {
                _reviews.assignValue(this)
            }
        }
    }

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