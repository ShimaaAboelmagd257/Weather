package com.example.weather.favorites.view

import com.example.weather.model.pojos.UserLocation

interface FavClickListner {
    fun onItemClicked(userLocation: UserLocation)
    fun onDeleteItemClicked(weatherResponse: UserLocation)
}
