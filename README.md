# ScreenPlay

## Flow
       +-------------+       +-------------------+
       | MainActivity| ----> |  Navigation Graph |
       +-------------+       +-------------------+
                                       |
                                       V
                             +-------------------+
                             |     ViewModel     |
                             +-------------------+
                                       |
                                       V
                             +-------------------+
                             |    Repository     |
                             +-------------------+
                                |              |
                                V              V
                     +--------------+  +------------------+
                     |  API Service |  |  Room Database   |
                     +--------------+  +------------------+
                                      |
                                      V
                             +-------------------+
                             |  Composable UI    |
                             +-------------------+

## Steps

####  👉🏻 Commit 1 

1. Import the required Dependencies change in ` gradle.kts ` in both app and project modules.
2. In `manifest.xml` add
   
    ```
     <uses-permission android:name="android.permission.INTERNET"/>
  
      <application
          android:name=".ScreenPlay"/>

   ```
3. Create a kotlin class ` ScreenPlay ` which extends : Application() and annotate it with ` @HiltAndroidApp ` .

           
-------------------------------------------------------------------------------------------------------------------------
####  👉🏻 Commit 2 

1. Alter the ui/themes for fonts and bg settings.
2. `res/drawable` for image. `res/font` for custom fonts. `res/raw` for lottie animations.

-------------------------------------------------------------------------------------------------------------------------

####  👉🏻 Commit 3 

1. Create a package named `utils` and add the tmdb api keys and Base urls.
2. Add 2 Resource states - `Loading, Success and Error`.

-------------------------------------------------------------------------------------------------------------------------

####  👉🏻 Commit 4 
1. Create the required model files. Use `@Parcelable` for android.


-------------------------------------------------------------------------------------------------------------------------

####  👉🏻 Commit 5 & 6

 1. Create data package for local and remote datum.
 2. `Local` for loacl data storage and persistent in the app - Room database.
 3. `Remote` - Contains API Servies including @GET, @QUERY, @PATH and suspend fun movielist() and response models.

-------------------------------------------------------------------------------------------------------------------------

####  👉🏻 Commit 7
1. Dependency Injection (DI) - Hilt dagger
   
2. Create `AppModule` as an `OBJECT KOTLIN FILE`
   
- `providesLoggingInterceptor()` : This function provides an instance of `HttpLoggingInterceptor` , which is used for logging HTTP requests and responses.

- `providesOkHttpClient()` : This function provides an instance of `OkHttpClient` , which is an HTTP client used to make network requests.

- `provideApiService()` : The function creates a `Retrofit instance` with the base URL and sets the OkHttpClient as the client for making network requests.

- `provideLocalDatabase()` : This function provides an instance of the MovieDatabase, which is a Room database.

3. In hilt, `@InstallIn( SingletonComponent::class )`

   - we ensure that Dagger Hilt treats it as a **`singleton`**, and there will be **`**ONLY ONE INSTANCE OF THE MODULE**`** throughout the app's lifecycle. 

-------------------------------------------------------------------------------------------------------------------------

####  👉🏻 Commit 8
1. Time to create another package `repository`.
2. GenereFilmRepository - @Inject constructor the Apiservice
3. Make use of selaed class `ResourceState` here - success, error, load that was created in the `utils` package.
4. when (filmType) {
                    FilmType.MOVIE -> apiService.getMovieGenre()
                    FilmType.TVSHOW -> apiService.getTvShowGenres()
                }
-------------------------------------------------------------------------------------------------------------------------

####  👉🏻 Commit 9

-------------------------------------------------------------------------------------------------------------------------

####  👉🏻 Commit 9


-------------------------------------------------------------------------------------------------------------------------
