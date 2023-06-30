package com.implementing.screenplay.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Film(
    @SerializedName("adult")
    val adult: Boolean? = null,
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,
    @SerializedName("poster_path")
    val posterPath: String? = null,
    @SerializedName("genre_ids")
    val genreIds: List<Int>? = null,
    @SerializedName("genres")
//  Alternative way -  val genres: List<Genre> = emptyList()
    val genres: List<Genre>?,
    @SerializedName("media_type")
    val mediaType: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("release_date", alternate = ["first_air_date"])
    val releaseDate: String,
    @SerializedName("runtime")
    val runtime: Int?,
    @SerializedName("title", alternate = ["name"])
    val title: String,
    @SerializedName("video")
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
) : Parcelable