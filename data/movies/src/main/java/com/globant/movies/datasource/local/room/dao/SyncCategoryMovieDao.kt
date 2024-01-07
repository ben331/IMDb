package com.globant.movies.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globant.common.CategoryType
import com.globant.common.SyncState
import com.globant.movies.datasource.local.room.entities.SyncCategoryMovieEntity

@Dao
interface SyncCategoryMovieDao {

    @Query("SELECT * FROM sync_category_movie WHERE sync_state = :state")
    suspend fun getMoviesToSync(state: SyncState):List<SyncCategoryMovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieToSync(movie: SyncCategoryMovieEntity)

    @Query("DELETE FROM sync_category_movie WHERE idMovie=:idMovie AND idCategory=:idCategory")
    suspend fun deleteMovieFromSync(idMovie:Int, idCategory: CategoryType)
}