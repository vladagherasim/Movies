package com.example.movies.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.movies.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {

}