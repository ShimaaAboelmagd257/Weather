package com.example.weather.model.remoteDatabase

import com.example.weather.model.pojos.ApiResponse


sealed class NetworkDelegate<T>(val data: ApiResponse ,
                                val message: String? = null) {
    class Success<T>( data: ApiResponse) : NetworkDelegate<T>(data)

}