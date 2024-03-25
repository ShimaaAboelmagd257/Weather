package com.example.weather.settings.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.model.sharedPrefrence.SharedPrefrences

class SettingsViewModelFactory(private val sharedPrefrences: SharedPrefrences): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingsViewModel ::class.java))
        {
            SettingsViewModel(sharedPrefrences) as T
        }else{
            throw IllegalAccessException("View Model Class not found")
        }
    }
}
