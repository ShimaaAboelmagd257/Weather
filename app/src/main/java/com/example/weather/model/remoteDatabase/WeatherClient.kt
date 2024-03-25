package com.example.weather.model.remoteDatabase

import com.example.weather.model.pojos.ApiResponse

class WeatherClient private constructor(): RemoteSource {


   private val weatherService: WeatherService by lazy {
        RetrofitHelper.retrofitInstance.create(WeatherService::class.java)
    }

    companion object {
        @Volatile
        private var INSTANCE: WeatherClient? = null

        fun getInstance(): WeatherClient {
            return INSTANCE ?: synchronized(this) {
                val instance = WeatherClient()
                INSTANCE = instance
                instance
            }
        }
    }

        override suspend fun getApiCallResponse(
            lat: Double?,
            lon: Double?,
            units: String?,
            lang: String?
        ): ApiResponse {
            return weatherService.apiCallResponse(lat, lon, units, lang)
        }
    }