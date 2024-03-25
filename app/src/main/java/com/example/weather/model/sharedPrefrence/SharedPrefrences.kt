package com.example.weather.model.sharedPrefrence

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefrences @Inject constructor(private val context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
    }

    fun addString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }
    fun addFloat(key: String, value: Float) {
        sharedPreferences.edit().putFloat(key, value).apply()
    }
    fun getFloat(key: String, value: Float) :Float {
        return sharedPreferences.getFloat(key, value)
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue)!!
    }

    fun addBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun deleteData(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}