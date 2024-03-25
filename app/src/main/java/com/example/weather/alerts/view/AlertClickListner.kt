package com.example.weather.alerts.view

import com.example.weather.model.pojos.UserWeatherAlert

interface AlertClickListner {

    fun onDeleteItemClicked(userWeatherAlert: UserWeatherAlert)
}