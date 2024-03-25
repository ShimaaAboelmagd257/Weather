package com.example.weather.favorites.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.model.repository.RepoInterface

class FavoritesViewModelFactory(private val repo: RepoInterface): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoritesViewModel ::class.java))
        {
            FavoritesViewModel(repo) as T
        }else{
            throw IllegalAccessException("View Model Class not found")
        }
    }
}
