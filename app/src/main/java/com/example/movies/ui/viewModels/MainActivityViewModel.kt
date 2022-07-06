package com.example.movies.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {

    private val key = "d88664a2e2c16e8647ce06f3a02cc096"

    fun getGenresToDatabase() {
        viewModelScope.launch { repository.getGenresToDatabase(key) }
    }
}