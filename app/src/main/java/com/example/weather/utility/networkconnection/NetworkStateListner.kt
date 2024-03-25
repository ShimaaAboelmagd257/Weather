package com.example.weather.utility.networkconnection

interface NetworkStateListner {
    fun onNetworkAvailable()
    fun onNetworkUnavailable()
}