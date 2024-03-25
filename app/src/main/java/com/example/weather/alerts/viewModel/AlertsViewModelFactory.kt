package com.example.weather.alerts.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.model.repository.RepoInterface



class AlertsViewModelFactory(private val repo: RepoInterface): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertsViewModel ::class.java))
        {
            AlertsViewModel(repo) as T
        }else{
            throw IllegalAccessException("View Model Class not found")
        }
    }
}
