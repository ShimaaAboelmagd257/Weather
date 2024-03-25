package com.example.weather.alerts.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.pojos.UserWeatherAlert
import com.example.weather.model.repository.RepoInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlertsViewModel(private var repo : RepoInterface):ViewModel() {

    private val _alertsLiveData = MutableStateFlow<List<UserWeatherAlert>>(emptyList())
    val alertsLiveData: StateFlow<List<UserWeatherAlert>> = _alertsLiveData

  /*  private val _alertLiveData = MutableLiveData<UserWeatherAlert>()
    val alertLiveData: LiveData<UserWeatherAlert>
        get() = _alertLiveData*/

    init {
        getAllAlerts()
    }

    fun getAllAlerts() {
        viewModelScope.launch {
            val currentTimeHours = System.currentTimeMillis() / 1000
         //   val currentTimeHours = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis())
            repo.getAllAlerts(currentTimeHours).collect { alerts ->
                _alertsLiveData.value = alerts
                Log.d("WeatherAlertsVM", "getAllAlerts size   " + alerts.size)
                Log.d("WeatherAlertsVM", "getAllAlerts for current time    " + currentTimeHours)


            }
        }
    }



    fun insertAlert(alert: UserWeatherAlert) {
        viewModelScope.launch {
            repo.insertAlert(alert) // Assuming this function inserts alerts
            Log.d("WeatherAlertsVM", "getAllAlerts size   " + alert.title)

        }
    }

    fun deletePastAlerts(){
        viewModelScope.launch {
            repo.deletePastAlerts(System.currentTimeMillis())
        }
    }

    fun deleteAlertItem(alert: UserWeatherAlert){
        viewModelScope.launch {
            repo.deleteAlertItem(alert)
        }

    }
}