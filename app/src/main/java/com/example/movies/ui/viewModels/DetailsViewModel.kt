package com.example.movies.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.data.MovieRepository
import com.example.movies.data.dto.MovieDTO
import com.example.movies.ui.ItemReview
import com.example.movies.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    fun getReviews(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            launchOn {
                repository.getReviews(id)
            }.subscribeOver(_exceptions) {
                _reviews.assignValue(this)
            }
        }
    }

    fun getMovie(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            launchOn {
                repository.getMovieDetails(id)
            }.subscribeOver(_exceptions) {
                _movieData.assignValue(this)
            }
        }
    }
}