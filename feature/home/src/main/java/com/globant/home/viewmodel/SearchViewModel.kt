package com.globant.home.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.ui.helpers.NetworkState
import com.globant.home.view.adapters.MovieResultAdapter
import com.globant.movies.model.MovieItem
import com.globant.movies.usecase.IsServiceAvailableUseCase
import com.globant.movies.usecase.SearchMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMovieUseCase:SearchMovieUseCase,
    private val isServiceAvailableUseCase: IsServiceAvailableUseCase
): ViewModel() {

    val resultMovies = MutableLiveData<List<MovieItem>>()

    private val _uiState:MutableStateFlow<NetworkState> = MutableStateFlow(NetworkState.Loading)
    val uiState:StateFlow<NetworkState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            isServiceAvailableUseCase.isServiceAvailable
                .collect { isConnected ->
                    _uiState.value = if(isConnected) NetworkState.Online else NetworkState.Offline
                }
        }
    }

    lateinit var adapter: MovieResultAdapter

    @SuppressLint("NotifyDataSetChanged")
    fun search(
        query:String
    ) {
        viewModelScope.launch {
            val result = searchMovieUseCase(query)
            if(result.isNotEmpty()){
                resultMovies.postValue(result)
                adapter.notifyDataSetChanged()
            }
        }
    }
}