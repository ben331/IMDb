package com.globant.imdb.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.network.firebase.FirebaseAuthManager
import com.globant.imdb.domain.model.MovieDetailItem
import com.globant.imdb.domain.model.MovieItem
import com.globant.imdb.domain.model.toSimple
import com.globant.imdb.domain.moviesUseCases.GetMovieByIdUseCase
import com.globant.imdb.domain.moviesUseCases.GetOfficialTrailerUseCase
import com.globant.imdb.domain.moviesUseCases.TestServiceAvailabilityUseCase
import com.globant.imdb.domain.userUseCases.AddMovieToUserListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val authManager: FirebaseAuthManager,
    private val getMovieByIdUseCase:GetMovieByIdUseCase,
    private val getTrailerUseCase:GetOfficialTrailerUseCase,
    private val addMovieToUserListUseCase:AddMovieToUserListUseCase,
    private val testServiceAvailabilityUseCase: TestServiceAvailabilityUseCase
): ViewModel() {

    val isLoading = MutableLiveData(false)
    val currentMovie = MutableLiveData<MovieDetailItem?>()
    val videoIframe = MutableLiveData<String?>()
    val isServiceAvailable = MutableLiveData(true)

    val username:String by lazy { authManager.getEmail() }

    fun onRefresh(movieId:Int){
        viewModelScope.launch {
            val result = getMovieByIdUseCase(movieId)
            currentMovie.postValue(result)
        }
        viewModelScope.launch {
            val result = getTrailerUseCase(movieId, true)
            result?.let {
                videoIframe.postValue(result)
            }
        }
    }

    fun addMovieToWatchList(
        handleSuccess:(movie: MovieItem)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit){
        isLoading.postValue(true)
        currentMovie.value?.let {
            addMovieToUserListUseCase(it.toSimple(), CategoryType.WATCH_LIST_MOVIES, handleSuccess, handleFailure)
        }
    }

    fun recordHistory(handleFailure:(title:Int, msg:Int)->Unit){
        isLoading.postValue(true)
        currentMovie.value?.let {
            addMovieToUserListUseCase(
                it.toSimple(), CategoryType.HISTORY_MOVIES,
                { movieItem ->
                    Log.i("INFO", "Movie ${movieItem.title},id:${movieItem.id} recorded in history")
                },
                handleFailure)
        }
    }

    fun testServiceAvailability() {
        viewModelScope.launch {
            isServiceAvailable.postValue(testServiceAvailabilityUseCase())
        }
    }
}