# ScreenPlay

### Clean Architecture - **MVVM ( Model ViewModel View)**
# 
     
      ╔══════════════════════╗                ╔══════════════════════╗                   ╔══════════════════════╗    
      ║     ** VIEW **       ║                ║   ** VIEW MODEL **   ║                   ║     ** MODEL **      ║     
      ║    --------------    ║ ------------→  ║     --------------   ║  ------------→    ║    --------------    ║    
      ║     UI Components    ║                ║        Survive       ║                   ║      Repositories    ║     
      ║  Activity/ Fragments ║                ║     Config Change    ║                   ║ (Data classes, APIs) ║   
      ╚══════════════════════╝                ╚══════════════════════╝                   ╚══════════════════════╝   
                                                   Business Logic                                 Repositories
                                                    ⭕ State                                     /            \
                                                    ⭕ Live Data                            Remote           Local  
                                                                                          Data Source       Data Source
                                                                                        
                                                                                        ✔️ Retrofit          ➡️ Room (Large and complex datasets)
                                                                                                              ➡️ DataStore (Small or Simple datasets)

      

## Flow of Screen Play
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

2. In `manifest.xml` add,
   
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

2. Create a sealed Resourcestates - `Loading, Success and Error`.

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

3. Make use of sealed class `ResourceState` here - **Success, Error, Loading** that was created in the `utils` package.

4. Use `when` instead of ` if-else `

   ```
          when (filmType) {
                    FilmType.MOVIE -> apiService.getMovieGenre()
                    FilmType.TVSHOW -> apiService.getTvShowGenres()
                }
   ```
-------------------------------------------------------------------------------------------------------------------------

####  👉🏻 Commit 9
1. Create a ` pagination ` package.
2. Pagination class,

   - needed params  ex : apiService and filmType (movie, tv shows)

   - extends  : PagingSource<Int, Film> Film is a data model. Note: extends in kotlin is ' : '

   - overrides 2 functions
      - fun getRefreshKey()
      - fun load()
        
4. Paginantion into repository.

5. Pagination implementation inside repository. 

-------------------------------------------------------------------------------------------------------------------------

####  👉🏻 Commit 10
1. ViewModel

2. Making use of @Inject constructor this time with Repository snd extends : ViewModel()

3. Mutable - Changeable(realtime updation) list of datum but read-only json data ex : _nowPlayingFilm.
   
    - private val _nowPlayingFilm = mutableStateOf<Flow<PagingData<Film>>>(emptyFlow())

    - val nowPlayingFilm : MutableState<Flow<PagingData<Film>>> = _nowPlayingFilm
  
4. Inside ViewModel Class
   
   ```
        init {
             /** call the required response type from apiService **/
             nowPlayingFilmNetwork(filmType, genreId)
        }
   
   ```

 -  Make use of  **viewModelScope.launch** { }.cacheIn(viewModelScope)
   
        ```
               fun nowPlayingFilmNetwork(filmType: FilmType, genreId: Int?) {
                  viewModelScope.launch {
                      _nowPlayingFilm.value = filterItem(
                          genreId,
                          homeRepository.nowPlayingFilm(filmType)
                      )**.cachedIn(viewModelScope)**
                  }
             }
          
          ```
    

-------------------------------------------------------------------------------------------------------------------------

####  👉🏻 Navigation Compose made easyy

1. Import the necessary dependencies for the " Ksp Compose Destinations"

2. In view, before @Composable annotation just add @Destination

3. Rebuild the Project

4. Automatically kotlin package will be created in your android project.

5. In mainActivity ` DestinationsNavHost(navGraph = NavGraphs.root)`  


-------------------------------------------------------------------------------------------------------------------------

####  👉🏻 Commit 12

1. Search Functionality

-------------------------------------------------------------------------------------------------------------------------

####  👉🏻 Commit 13

1. Room DB
2. Create a data model class

    ```
       @Entity(tableName = "watch_list_table")
       data class MyListMovie (
         @PrimaryKey
         val mediaId: Int,
         val imagePath: String,
         val title: String,
         val releaseDate: String,
         val rating: Double,
         val addedOn: String
      )
     
    ```
   
4. Dao (Interface)

    ```
     @Dao
     interface MovieDao {
         @Insert(onConflict = REPLACE)
         suspend fun insertMovieInList(myListMovie: MyListMovie)
     
         @Query("DELETE FROM watch_list_table WHERE mediaId = :mediaId")
         suspend fun removeFromList(mediaId: Int)
     
         @Query("SELECT EXISTS (SELECT 1 FROM watch_list_table WHERE mediaId = :mediaId)")
         suspend fun exists(mediaId: Int): Int
     
         @Query("SELECT * FROM watch_list_table ORDER BY addedOn DESC")
         suspend fun getAllWatchListData(): Flow<List<MyListMovie>>
     }
       
     
    ```
6. Create a Abstract class for room database

   ```
        @AutoMigration(from = 1, to = 2)
        @Database(version = 1, entities = [MyListMovie::class], exportSchema = false)
          abstract class MovieDatabase(): RoomDatabase() {
              abstract fun movieDao(): MovieDao
          }
     
    ```

   <img width="725" alt="image" src="https://github.com/Brindha-m/ScreenPlay/assets/72887609/a709e700-7b32-477a-a1c8-a1e06b24c124">


-------------------------------------------------------------------------------------------------------------------------

`Coroutines`

Imagine you have a weather application that displays the current temperature of a location. When the user opens the application, it needs to fetch the latest temperature from a remote server and display it on the screen. However, fetching the temperature involves a network call, which can be a time-consuming operation. To prevent blocking the main thread and keep the user interface responsive, you can use coroutines.

-------------------------------------------------------------------------------------------------------------------------

## - Start with ApiServices with the endpoints.

**@Inject constructor apiService** is common for all the repo, vm, and views.

| REPOSITORY --->  | VIEWMODEL ---> | VIEW (Composables) |
|------------------|----------------|-------------------|
| ApiService  | repository | viewmodel |


| REPOSITORY |

      
      class HomeRepository @Inject
          constructor(private val apiService: ApiService) { } 
     

| VIEWMODEL | 

     
     
     @HiltViewModel
          class HomeViewModel @Inject constructor(
              private val homeRepository: HomeRepository,
              private val genreFilmRepository: GenreFilmRepository
          ) : ViewModel()  { }
     
     

| VIEW (Composables) |  

     
     @Composable
        fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel(), navigator: DestinationsNavigator) { }
     
     

