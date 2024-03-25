package com.example.weather.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.model.repository.RepoInterface

class HomeViewModelFactory(private val repo: RepoInterface): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java))
        {
            HomeViewModel(repo) as T
        }else{
            throw IllegalAccessException("View Model Class not found")
        }
    }
}