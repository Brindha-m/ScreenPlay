package com.implementing.screenplay.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.implementing.screenplay.data.remote.ApiService
import com.implementing.screenplay.data.remote.response.CastResponse
import com.implementing.screenplay.model.Film
import com.implementing.screenplay.pagination.NowPlayingFilmSource
import com.implementing.screenplay.utils.FilmType
import com.implementing.screenplay.utils.ResourceState
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class HomeRepository @Inject
    constructor(private val apiService: ApiService) {

    //<!-- Now Playing section --!-->

    fun nowPlayingFilm(filmType: FilmType): Flow<PagingData<Film>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                NowPlayingFilmSource(
                    apiService = apiService,
                    filmType = filmType
                )
            }
        ).flow
    }

    //<!-- Movie/ TV Show Cast section --!-->
    suspend fun getFilmCast(filmType: FilmType, mediaId: Int):  ResourceState<CastResponse> {
        val resultResponse = try {
            if (filmType == FilmType.MOVIE)
                apiService.getMovieCast(mediaId)
            else apiService.getTvShowCast(mediaId)
        }
        catch (e: Exception) {
            return ResourceState.Error(e.localizedMessage!!)
        } catch (e: HttpException) {
            return ResourceState.Error(e.localizedMessage!!)
        }
        return ResourceState.Success(resultResponse)
    }
}


