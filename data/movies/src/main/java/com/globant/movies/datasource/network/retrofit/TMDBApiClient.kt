package com.globant.movies.datasource.network.retrofit

import com.globant.common.Constants
import com.globant.imdb.data.model.movies.MovieDetailModel
import com.globant.movies.model.movies.MoviesListModel
import com.globant.movies.model.movies.MoviesListDatesModel
import com.globant.movies.model.movies.VideoListModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiClient {

    @HEAD("account/${Constants.TMDB_ACCOUNT}")
    suspend fun headService(): Response<Void>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ):Response<MoviesListDatesModel?>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ):Response<MoviesListDatesModel?>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ):Response<MoviesListModel?>

    @GET("movie/{movieId}")
    suspend fun getMovieById(
        @Path("movieId") movieId:Int,
        @Query("language") language: String,
    ):Response<MovieDetailModel?>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ):Response<MoviesListModel?>

    @GET("movie/{movieId}/videos")
    suspend fun getTrailers(
        @Path("movieId") movieId:Int,
        @Query("language") language: String,
    ):Response<VideoListModel?>
}