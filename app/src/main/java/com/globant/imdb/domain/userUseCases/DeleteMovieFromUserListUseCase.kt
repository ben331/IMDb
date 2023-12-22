package com.globant.imdb.domain.userUseCases

import com.globant.imdb.data.repositories.IMDbRepository
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.database.entities.movie.SyncState
import com.globant.imdb.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeleteMovieFromUserListUseCase @Inject constructor(
    private val repository: IMDbRepository,
    private val syncUserLocalDataUseCase: SyncUserLocalDataUseCase,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(movieId:Int, category: CategoryType):Boolean {
        val isDeleted = repository.deleteMovieFromCategory(movieId, category)

        return if(isDeleted){
            CoroutineScope(ioDispatcher).launch {
                syncUserLocalDataUseCase()
                try {
                    repository.deleteMovieFromCategoryDatabase(movieId, category)
                } catch (_:Exception){ }
            }
            true
        } else {
            try {
                repository.deleteMovieFromCategoryDatabase(movieId, category)
                repository.addMovieToSyncDatabase(movieId, category, SyncState.PENDING_TO_DELETE)
                true
            }catch (_: Exception){ false }
        }
    }
}