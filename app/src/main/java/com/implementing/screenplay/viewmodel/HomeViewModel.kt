package com.implementing.screenplay.viewmodel


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.implementing.screenplay.model.Film
import com.implementing.screenplay.model.Genre
import com.implementing.screenplay.repository.GenreFilmRepository
import com.implementing.screenplay.repository.HomeRepository
import com.implementing.screenplay.utils.FilmType
import com.implementing.screenplay.utils.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val genreFilmRepository: GenreFilmRepository
) : ViewModel() {

//    Changeable(realtime updation) list of datum but read-only json data is _nowPlayingFilm
    private val _nowPlayingFilm = mutableStateOf<Flow<PagingData<Film>>>(emptyFlow())
    val nowPlayingFilm : MutableState<Flow<PagingData<Film>>> = _nowPlayingFilm

    var selectedFilmType: MutableState<FilmType> = mutableStateOf(FilmType.MOVIE)

    val selectedGenre: MutableState<Genre> = mutableStateOf(Genre(null, "All"))
    private var _filmGenre = mutableStateListOf(Genre(null, "All"))
    val filmGenre: SnapshotStateList<Genre> = _filmGenre

    init {
        hitNetworkCall()
    }

    fun hitNetworkCall(
        filmType: FilmType = selectedFilmType.value,
        genreId: Int? = selectedGenre.value.id
    ) {
        genreFilmChip()
        nowPlayingFilmNetwork(filmType, genreId)
    }

    fun genreFilmChip() {
        viewModelScope.launch {
            val defaultGenre = Genre(null, "All")
            when (val resultChip = genreFilmRepository.genreFilm(selectedFilmType.value)) {
                is ResourceState.Success -> {
                    _filmGenre.clear()
                    _filmGenre.add(defaultGenre)
                    resultChip.data?.genres?.forEach {
                        _filmGenre.add(it)
                    }
                }
                is ResourceState.Error -> {
                    Timber.e("Error loading Genres")
                }
                else -> {}
            }
        }
    }
    fun nowPlayingFilmNetwork(filmType: FilmType, genreId: Int?) {
        viewModelScope.launch {
            _nowPlayingFilm.value = filterItem(
                genreId,
                homeRepository.nowPlayingFilm(filmType)
            ).cachedIn(viewModelScope)
        }
    }

    fun filterItem(genreId: Int?, data: Flow<PagingData<Film>>): Flow<PagingData<Film>> {
        return if (genreId != null) {
            data.map { result ->
                result.filter { film ->
                    film.genreIds!!.contains(genreId)
                }
            }
        } else {
            data
        }
    }



}