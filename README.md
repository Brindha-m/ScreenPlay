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




### ScreenShots
#

| Home Screenshot | Search Screen | Detail Screenshot | Watch later (Room) Screen |
|:---:|:---:|:---:|:---:|
| ![Home Screen](https://github.com/Brindha-m/ScreenPlay/assets/72887609/4b890b09-72b8-4745-8bd7-bbfe20bb6251)| ![Search](https://github.com/Brindha-m/ScreenPlay/assets/72887609/1f495de8-e31f-40d9-b814-297d34b0bf19)| ![Detail](https://github.com/Brindha-m/ScreenPlay/assets/72887609/5de5187f-9fb8-4cf5-a407-9a705b6abe10)| ![Room](https://github.com/Brindha-m/ScreenPlay/assets/72887609/14393f26-9b5f-4536-b063-df51886742cb)| 



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
        
4. Pagination into repository.

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
                      ).cachedIn(viewModelScope)
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

### Coroutines

- Coroutines, introduced in Kotlin, provide a more lightweight and structured approach to `concurrency`.

- In java we have threads (Runnable interfaces and thread class), Threads are heavyweight.
  
- Coroutines are built on top of threads but offer a higher-level programming model that allows for easier `asynchronous` programming.

- Coroutines can `suspend and resume `their execution, allowing for efficient handling of long-running operations without blocking threads.

**Example:** 

- Imagine you have a weather application that displays the current temperature of a location.

- When the user opens the application, it needs to fetch the latest temperature from a remote server and display it on the screen.

- However, fetching the temperature involves a network call, which can be a time-consuming operation.

- To prevent ` blocking the main thread and keep the user interface responsive `, you can use coroutines.

-------------------------------------------------------------------------------------------------------------------------

### Serializable and Parcelable in the context of Android


- Both Serializable and Parcelable are the interfaces that allows `objects` to be converted into a `byte stream` for storage or transmission.

- Parcelable is an Android-specific interface.
  

| Feature       | Serializable | Parcelable   |
|---------------|--------------|--------------|
| Performance   | Slower       | Faster       |
| Implementation| Simple       | Complex      |
| Data Size     | Larger       | Smaller      |
| Flexibility   | Limited      | Extensive    |
| External Libraries | Not required | Not required |


Example: 

   - Imagine you have an Android app that allows users to create and save notes.
   
   - You want to serialize a Note object to save it to a file or transmit it over a network. 
   
   - You can implement the Serializable interface in the Note class, which would enable you to write the Note object directly to a file or send it as a byte stream.


-------------------------------------------------------------------------------------------------------------------------


### - Start with ApiServices define the endpoints.

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
     
     
-------------------------------------------------------------------------------------------------------------------------

## Configuration to Kotlin DSL(Domain Specific Language) from Groovy

1. Switch to project view, and time to create a directory named `buildSrc` and add a `build.gradle.kts ` file.

2. Sync the project.

3. Now inside ` project -> gradle ` directory create a file ` libs.versions.toml`

4. In this file, we'll define versions, libraries, bundles and plugins.

   ```
   
   [versions]
   {...
        room = "3.0"
        kotlin = "1.8"
        compose_material_3 = "1.1.0"

   ...}


   [libraries]
   
    room_compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
    room_ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }

 
   [bundles]
   room = ["room_ktx", "room_compiler"]
   
   ```

5. And now, in our build.gradle.kts ( app module)
   ```
        dependencies {

             ksp(libs.room.compiler)
             implementation(libs.bundles.room)

        }
   
   ```

-------------------------------------------------------------------------------------------------------------------------

## Figma to andriod for compose ui utilizing relay

TODOS


Comparison of key differences between `MutableStateFlow`, `StateFlow`, `State`, and other concepts in Kotlin Coroutines:


| Concept                | Mutability | Usage                       | Use Cases                  |
|------------------------|------------|-----------------------------|----------------------------|
| MutableStateFlow       | Mutable    | To emit and update state    | ViewModel, Mutable state   |
| StateFlow              | Immutable  | To observe state changes    | ViewModel, Immutable state |
| State (Android Jetpack)| Immutable  | To observe ViewModel state | ViewModel, Immutable state |
| LiveData               | Mutable    | To observe state changes    | ViewModel, Mutable state   |
| Flow                   | Immutable  | To emit and observe values | Asynchronous operations   |


