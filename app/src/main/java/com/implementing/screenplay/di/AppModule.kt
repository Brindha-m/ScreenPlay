package com.implementing.screenplay.di

import android.content.Context
import androidx.room.Room
import com.implementing.screenplay.data.local.MovieDao
import com.implementing.screenplay.data.local.MovieDatabase
import com.implementing.screenplay.data.remote.ApiService
import com.implementing.screenplay.repository.HomeRepository
import com.implementing.screenplay.repository.MyListMovieRepository
import com.implementing.screenplay.utils.Utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Used for logging HTTP requests and responses.
    @Singleton
    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    // HTTP client used to make network requests.
    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .callTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit instance
    @Singleton
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

    // Home Repo For movies

    @Singleton
    @Provides
    fun provideMovieRepository(apiService: ApiService):
            HomeRepository = HomeRepository(apiService)


    // Room DB
    @Provides
    @Singleton
    fun provideLocalDatabase(
        @ApplicationContext context: Context) : MovieDatabase =
        Room
            .databaseBuilder(
                context,
                MovieDatabase::class.java,
                "watch_list_table"
            )
             .fallbackToDestructiveMigration()
             .build()

    /** A function to provide a single instance of the local database Dao throughout our app */

    @Provides
    @Singleton
    fun provideMovieDao(movieDatabase: MovieDatabase) = movieDatabase.movieDao()


//    @Singleton
    @Provides
    fun provideMyListRepository(movieDao: MovieDao): MyListMovieRepository =
        MyListMovieRepository(movieDao = movieDao)


}