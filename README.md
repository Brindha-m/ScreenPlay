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

****----------- Commit 1 ------------****

1. Import the required Dependencies change in ` gradle.kts ` in both app and project modules.
2. In `manifest.xml` add
   
    ```
     <uses-permission android:name="android.permission.INTERNET"/>
  
      <application
          android:name=".ScreenPlay"/>

   ```
3. Create a kotlin class ` ScreenPlay ` which extends : Application() and annotate it with ` @HiltAndroidApp ` .
           


****----------- Commit 2 ------------****
1. Alter the ui/themes for fonts and bg settings.
2. `res/drawable` for image. `res/font` for custom fonts. `res/raw` for lottie animations.


****----------- Commit 3 ------------****


