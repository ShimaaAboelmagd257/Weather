package com.example.weather.model.remoteDatabase

import com.example.weather.model.pojos.ApiResponse

interface RemoteSource {

    suspend fun getApiCallResponse(
        lat: Double?,
        lon: Double?,
        units: String?,
        lang: String?
    ): ApiResponse
}