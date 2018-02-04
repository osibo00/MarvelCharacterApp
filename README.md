# MarvelCharacterApp

This app retrieves, displays and then stores Marvel Characters in a SQL database. The app requires 
an API key to work. It is really easy to get one, and it will only take a couple of minutes. 
https://developer.marvel.com/ After the repository is cloned, a gradle.properties file needs to created in the 
Android Studio project folder when in project view. Alternatively you could add a gradle.properties file in 
the Gradle.Scripts folder when in Android view. It will need to contain two variables. 
API_KEY = "yourApiKeyHere" and PRIVATE_KEY = "yourPrivateKeyhere", with both written in this format. Then you 
should sync the project With Gradle. Uses Retrofit, RecyclerView, Glide, Palette, and Android 
Architecture Components; Room and Lifecycle.
