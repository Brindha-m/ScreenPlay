package com.implementing.screenplay.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.implementing.screenplay.data.remote.response.Cast
import com.implementing.screenplay.model.Film
import com.implementing.screenplay.model.Genre
import com.implementing.screenplay.repository.GenreFilmRepository
import com.implementing.screenplay.repository.HomeRepository
import com.implementing.screenplay.utils.FilmType
import com.implementing.screenplay.utils.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieDetailRepository: HomeRepository,
    private val genreFilmRepository: GenreFilmRepository,
) :
    ViewModel() {

    private val _filmCast = mutableStateOf<List<Cast>>(emptyList())
    val filmCast: State<List<Cast>> = _filmCast

    private val _movieGenreState = mutableStateListOf(Genre(id = null, name = "All"))
    val movieGenreState: SnapshotStateList<Genre> = _movieGenreState


    fun getFilmCast(filmType: FilmType, medialId: Int) {
        viewModelScope.launch {
            movieDetailRepository.getFilmCast(filmType = filmType, mediaId = medialId).also {
                if (it is ResourceState.Success) {
                    _filmCast.value = it.data!!.castResult
                }
            }
        }
    }

    fun getFilmTag(filmType: FilmType) {
        viewModelScope.launch {
            when (val chipData = genreFilmRepository.genreFilm(filmType)) {
                is ResourceState.Error -> {
                    Timber.e("You getting Error")
                }
                is ResourceState.Success -> {
                    _movieGenreState.clear()
                    chipData.data?.genres?.forEach {
                        _movieGenreState.add(it)
                    }
                }
                is ResourceState.Loading -> TODO()
            }
        }
    }
}