package com.implementing.screenplay.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.implementing.screenplay.data.remote.ApiService
import com.implementing.screenplay.model.Film
import com.implementing.screenplay.utils.FilmType
import retrofit2.HttpException
import java.io.IOException

class NowPlayingFilmSource(
    private val apiService: ApiService,
    private val filmType: FilmType
) : PagingSource<Int, Film>()
{
    override fun getRefreshKey(state: PagingState<Int, Film>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
        return try {
            val nextPage = params.key ?: 1
            val trendingFilmData = when (filmType) {
                FilmType.MOVIE -> apiService.getNowPlayingMovies(page = nextPage)
                FilmType.TVSHOW -> apiService.getNowPlayingTvSeries(page = nextPage)
            }

            LoadResult.Page(
                nextKey = if (trendingFilmData.results.isEmpty()) null else nextPage + 1,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                data = trendingFilmData.results
            )

        }
        catch (e: IOException) {
            return LoadResult.Error(e)
        }
        catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

}