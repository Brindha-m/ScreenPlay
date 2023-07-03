package com.implementing.screenplay.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.implementing.screenplay.data.remote.ApiService
import com.implementing.screenplay.model.Search
import com.implementing.screenplay.pagination.MultiSearchSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MultiSearchRepository @Inject constructor(private val apiService: ApiService) {
    fun searchResult(searchPars: String): Flow<PagingData<Search>> {
        return Pager(
            config = PagingConfig(20, enablePlaceholders = false),
            pagingSourceFactory = { MultiSearchSource(apiService, searchPars) }
        ).flow
    }
}