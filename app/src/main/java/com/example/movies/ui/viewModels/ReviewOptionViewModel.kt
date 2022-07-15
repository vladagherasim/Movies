package com.example.movies.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.data.MovieRepository
import com.example.movies.ui.ItemReview
import com.example.movies.utils.assignValue
import com.example.movies.utils.createListedLiveData
import com.example.movies.utils.launchOn
import com.example.movies.utils.toLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewOptionViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {
    private val _reviews = createListedLiveData<ItemReview>()
    val reviews = _reviews.toLiveData()

    private val _exceptions = MutableLiveData<Exception>()
    val exceptions = _exceptions.toLiveData()

    private val key = "d88664a2e2c16e8647ce06f3a02cc096"

    fun getReviews(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            launchOn {
                repository.getReviews(id, key)
            }.subscribeOver(_exceptions) {
                _reviews.assignValue(this)
            }
        }
    }
}