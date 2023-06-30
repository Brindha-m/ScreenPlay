package com.implementing.screenplay.repository

import com.implementing.screenplay.data.remote.ApiService
import com.implementing.screenplay.data.remote.response.GenreResponse
import com.implementing.screenplay.utils.FilmType
import com.implementing.screenplay.utils.ResourceState
import retrofit2.HttpException
import javax.inject.Inject

class GenreFilmRepository @Inject
    constructor(private val apiService: ApiService)
{
        suspend fun genreFilm(filmType: FilmType): ResourceState<GenreResponse> {
            val response = try {
                when (filmType) {
                    FilmType.MOVIE -> apiService.getMovieGenre()
                    FilmType.TVSHOW -> apiService.getTvShowGenres()
                }
                /**
                --- Old normal way using if else ---
                    if (filmType == FilmType.MOVIE)
                        apiService.getMovieGenre()
                    else if(filmType == FilmType.TVSHOW)
                        apiService.getTvShowGenres()
                    else TODO()
                 **/

            }
            catch (e: Exception) {
                return ResourceState.Error("Unknown error occurred: ${e.localizedMessage}")
            }
            catch (e: HttpException) {
                return ResourceState.Error("HTTP error: ${e.localizedMessage}")
            }
        return ResourceState.Success(response)

        }
}