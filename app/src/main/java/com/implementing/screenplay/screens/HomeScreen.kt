package com.implementing.screenplay.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.implementing.screenplay.shareScreenCompose.GenreChip
import com.implementing.screenplay.shareScreenCompose.ScrollableMovieItem
import com.implementing.screenplay.shareScreenCompose.ShowAboutCategory
import com.implementing.screenplay.utils.FilmType
import com.implementing.screenplay.viewmodel.HomeViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel(), navigator: DestinationsNavigator) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
//        SearchAndList(
//            homeViewModel = homeViewModel,
//            onRoomWatchList = { navigator.navigate(direction = DetailFilmScreenDestination()) },
//            onSearchScreen = { navigator.navigate(direction = SearchScreenDestination()) }
//        )
        NestedScroll(homeViewModel, navigator = navigator)
    }
}
@Composable
fun NestedScroll(homeViewModel: HomeViewModel, navigator: DestinationsNavigator) {

    val nowPlayingFilm = homeViewModel.nowPlayingFilm.value.collectAsLazyPagingItems()
    val genreChip = homeViewModel.filmGenre
    val filmType: FilmType = homeViewModel.selectedFilmType.value

    LazyColumn(Modifier.fillMaxSize()) {

        item {
            Text(
                text = "Genre",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
        item {
            LazyRow(
                Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(genreChip) { genreChip ->
                    GenreChip(
                        title = genreChip.name,
                        onClick = {
                            if (homeViewModel.selectedGenre.value != genreChip) {
                                homeViewModel.selectedGenre.value = genreChip
                                homeViewModel.hitNetworkCall()
                            }
                        },
                        selected = genreChip.name == homeViewModel.selectedGenre.value.name
                    )
                }
            }
        }

        item {
            Text(
                text = "Now Playing",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp),
            )
        }
        item {
            ScrollableMovieItem(
                pagingData = nowPlayingFilm,
                landscape = false,
                retryOnClick = homeViewModel::hitNetworkCall,
                navigator = navigator,
                filmType = filmType
            )
        }
    }
}