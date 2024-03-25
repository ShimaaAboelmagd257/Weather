# Cloudy

Cloudy is an Android weather application built using Kotlin and follows the MVVM (Model-View-ViewModel) architecture pattern. It allows users to check current weather conditions and forecasts for various locations.

## Features

- **Current Weather**: View the current weather conditions including temperature, humidity, wind speed, and more for a specified location.
- **Weather Forecast**: Check the weather forecast for the upcoming days to plan ahead.
- **Save Locations**: Save your favorite locations to easily access weather information for those places.
- **Set Alerts**: Set alerts for specific weather conditions or time periods to stay informed.
- **Settings Customization**:
  - **Units**: Customize units for temperature, wind speed, and other weather parameters according to your preference.
  - **Languages**: Choose from a variety of languages for the app interface and weather information.
  - **Location**: Configure location settings to automatically detect your current location or manually set a default location.
## APIs Used

   - **OpenWeatherMap API**:  It is updated every 10 minute. in order to receive the most accurate and up-to-date weather data.

## Libraries Used

   - **Android Jetpack Libraries** : Utilized Jetpack components including ViewModel, LiveData, and Room for a robust and efficient architecture.
   -  **Retrofit**: Used Retrofit for making network requests to fetch weather data from the OpenWeatherMap API.
   - **Gson**: Gson is used for JSON parsing to convert JSON responses from the API into Kotlin data objects.
  -  **Room Persistence Library** : Employed Room for local data storage to cache weather data and provide offline access.

##  Installation

To install Cloudy, follow these steps:

    Clone this repository.
    Open the project in Android Studio.
    Build and run the app on an Android device or emulator.
