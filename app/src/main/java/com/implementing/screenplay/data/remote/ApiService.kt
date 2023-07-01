package com.implementing.screenplay.data.remote

import com.implementing.screenplay.data.remote.response.CastResponse
import com.implementing.screenplay.data.remote.response.GenreResponse
import com.implementing.screenplay.data.remote.response.MovieResponse
import com.implementing.screenplay.utils.Utils
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    /** Movies **/
    // -- Movie Genre --
    @GET("genre/movie/list")
    suspend fun getMovieGenre(
        @Query("api_key") apiKey: String = Utils.apiKey,
        @Query("language") language: String = "en"
    ): GenreResponse

//    Sample https://api.themoviedb.org/3/genre/movie/list?api_key=c02086cdfb63f21e9a53947164e4dec5&language=en
// -- All, Action, Animation, Comedy, Crime, Drama, Family so on..

    // -- Movie Now Playing -- (need pagination)
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = Utils.apiKey,
        @Query("language") language: String = "en"
    ): MovieResponse

    // -- Movie Detail cast -- {movie_id} - path param and api_key - query param
    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCast(
        @Path("movie_id") filmId: Int,
        @Query("api_key") apiKey: String = Utils.apiKey
    ): CastResponse



    /** TV Shows **/
    @GET("genre/tv/list")
    suspend fun getTvShowGenres(
        @Query("api_key") apiKey: String = Utils.apiKey,
        @Query("language") language: String = "en-US"
    ): GenreResponse

    // -- TV Show Now Playing -- (need pagination)
    @GET("tv/on_the_air")
    suspend fun getNowPlayingTvSeries(
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = Utils.apiKey,
        @Query("language") language: String = "en"
    ): MovieResponse

    // -- TV Show Detail cast -- {movie_id} - path param and api_key - query param
    @GET("tv/{tv_id}/credits")
    suspend fun getTvShowCast(
        @Path("tv_id") filmId: Int,
        @Query("api_key") apiKey: String = Utils.apiKey
    ): CastResponse


    /** Search Engine **/

}