package com.example.weather.model.remoteDatabase

import com.example.weather.model.pojos.ApiResponse
import com.example.weather.utility.helper.Constants
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {

    @GET("onecall")
    suspend fun apiCallResponse(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("units") units: String?,
       // @Query("exclude")exclude:String="minutely",
        @Query("lang") language: String?,
        @Query("appid") appid: String = Constants.API_KEY
    ) : ApiResponse

}