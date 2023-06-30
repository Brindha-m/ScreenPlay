package com.implementing.screenplay.data.remote.response

import com.google.gson.annotations.SerializedName
import com.implementing.screenplay.model.Genre

data class GenreResponse(
    @SerializedName("genres")
    val genres: List<Genre>
)