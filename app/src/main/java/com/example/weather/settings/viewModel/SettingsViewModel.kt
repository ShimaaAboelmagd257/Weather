package com.example.weather.settings.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.sharedPrefrence.SharedPrefrences
import com.example.weather.utility.helper.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
     val sharedPreferences: SharedPrefrences
) : ViewModel() {

    private val _selectedtempUnit = MutableLiveData<String>()
    val selectedtempUnit: LiveData<String> get() = _selectedtempUnit

    private val _selectespeedUnit = MutableLiveData<String>()
    val selectedspeedUnit: LiveData<String> get() = _selectespeedUnit

    private val _selectedLanguageSetting = MutableLiveData<String>()
    val selectedLanguageSetting: LiveData<String> get() = _selectedLanguageSetting

    private val _selectedLocationSetting = MutableLiveData<String>()
    val selectedLocationSetting: LiveData<String> get() = _selectedLocationSetting



    init {

        //_selectedIsEnabled.value = false
        _selectedLocationSetting.value = sharedPreferences.getString(Constants.LOCATION, "default_location_value")
        _selectedLanguageSetting.value = sharedPreferences.getString(Constants.LANGUAGE, "default_language_value")
        _selectedtempUnit.value = sharedPreferences.getString(Constants.TEMP_UNIT, "default_temp_unit_value")
        _selectespeedUnit.value = sharedPreferences.getString(Constants.SPEED_UNIT, "default_speed_unit_value")
        Log.d("WeatherSettingsVm", "Location: ${sharedPreferences.getString(Constants.LOCATION, "")}")
        Log.d("WeatherSettingsVm", "Language: ${sharedPreferences.getString(Constants.LANGUAGE, "")}")
        Log.d("WeatherSettingsVm", "Temp Unit: ${sharedPreferences.getString(Constants.TEMP_UNIT, "")}")
        Log.d("WeatherSettingsVm", "Speed Unit: ${sharedPreferences.getString(Constants.SPEED_UNIT, "")}")

    }

    fun updateNotificationSetting(isEnabled: Boolean) {
      //  _selectedIsEnabled.value = isEnabled
        viewModelScope.launch(Dispatchers.IO) {
                sharedPreferences.addBoolean(Constants.IsMap, isEnabled)
                Log.d("WeatherSettingsVm ","updateNotificationSetting to "+ isEnabled)

        }
    }
    //@BindingAdapter("onCheckedChanged" , "language")
    fun updateLanguageSetting( language: String) {
            viewModelScope.launch(Dispatchers.IO) {
                val currentLocation = selectedLanguageSetting.value
                val locationChanged = currentLocation != language
                _selectedLanguageSetting.postValue(language)

                if (locationChanged) {
                    sharedPreferences.addString(Constants.LANGUAGE, language)
                    Log.d("WeatherSettingsVm ","updateLanguageSetting to "+ language)

            }
        }
    }



    fun updateLocation(selectedLocation: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentLocation = selectedLocationSetting.value
            val locationChanged = currentLocation != selectedLocation
            _selectedLocationSetting.postValue(selectedLocation)
            if (locationChanged) {
                sharedPreferences.addString(Constants.LOCATION, selectedLocation)
                Log.d("WeatherSettingsVm", "Updated location in SharedPreferences: $selectedLocation")

            }
        }
    }




      fun updateSharedPreferencesLocation(location: String) {
        synchronized(sharedPreferences) {
            sharedPreferences.addString(Constants.LOCATION, location)
            Log.d("WeatherSettingsVm", "Updated location in SharedPreferences: $location")
        }
    }


   // @BindingAdapter("onCheckedChanged" , "tempreture")
    fun updateTempUnit( tempreture: String) {
            viewModelScope.launch(Dispatchers.IO) {
                val currentLocation = selectedtempUnit.value
                val locationChanged = currentLocation != tempreture
                _selectedtempUnit.postValue(tempreture)
                if (locationChanged) {
                    sharedPreferences.addString(Constants.TEMP_UNIT, tempreture)
                    Log.d("WeatherSettingsVm ","updateTempUnit to "+ tempreture)
                }
            }

    }

  //  @BindingAdapter("onCheckedChanged" , "speed")
    fun updateSpeedUnit( speed: String) {
            viewModelScope.launch(Dispatchers.IO) {
                val currentLocation = selectedspeedUnit.value
                val locationChanged = currentLocation != speed
                _selectespeedUnit.postValue(speed)
                if (locationChanged) {
                    sharedPreferences.addString(Constants.SPEED_UNIT, speed)
                    Log.d("WeatherSettingsVm ","updateSpeedUnit to "+ speed)

            }
        }
    }





}
/* fun handleLanguageChange(isAr: Boolean, isEn: Boolean, isFr: Boolean) {
        _languageChangeLiveData.value = Triple(isAr, isEn, isFr)
    }
    fun handleAllowNotification(isAllowed: Boolean){
        isAllowCheckBoxChecked.value = isAllowed
    }*/

/*  fun handleLocationChange(
      isMap: Boolean,
      isGps: Boolean,
      location: String,
      callback: (String) -> Unit
  ) {
      viewModelScope.launch {
          withContext(Dispatchers.IO) {
              updateLocationSetting(location)

              // Simulate some delay (replace this with your actual logic)
              delay(2000)
          }

          withContext(Dispatchers.Main) {
              _locationChangeLiveData.value = Pair(isMap, isGps)
              callback(location) // Invoke the callback with the updated location
          }
      }
  }*/



/*suspend fun handleLocationChange(isMap: Boolean, isGps: Boolean, location: String): String {
    return withContext(Dispatchers.IO) {
        updateLocationSetting(location)

        // Simulate some delay (replace this with your actual logic)
        delay(2000)

        location // Return the updated location
    }.also {
        withContext(Dispatchers.Main) {
            _locationChangeLiveData.value = Pair(isMap, isGps)
        }
    }
}*/

/*  fun handleLocationChange(isMap: Boolean, isGps: Boolean ,location: String):String {
      viewModelScope.launch {
          withContext(Dispatchers.IO) {
           updateLocationSetting(location)                // ...

              // Simulate some delay (replace this with your actual logic)
              delay(2000)
              location
          }

          withContext(Dispatchers.Main) {
              _locationChangeLiveData.value = Pair(isMap, isGps)
          }
      }
  }*/
/*fun onLocationRadioButtonChanged( isMap: Boolean,
                                  isGps: Boolean, location: String) {
    if (isMap && isGps) {
        handleLocationChange(isMap,isGps,location)
    }
}*/

/* fun observeCheckBoxChanges() {
       isMapCheckBoxChecked.observe(viewLifecycleOwner) { isChecked ->
           if (isChecked) {
               handleLocationChange(R.id.mapCheckBox)
           }
       }*/
/* fun handleLocationChange(isMap: Boolean, isGps: Boolean) {
     isMapCheckBoxChecked.value = isMap
     isGpsCheckBoxChecked.value = isGps
 }
  fun handleTempChange(isCelsius: Boolean, isFahrenheit: Boolean, isKelvin: Boolean) {
      isCelsiusCheckBoxChecked.value = isCelsius
      isFarnheitCheckBoxChecked.value = isFahrenheit
      isKelvinCheckBoxChecked.value = isKelvin
  }
  fun handleSpeedChange(isMeter: Boolean, isMile: Boolean, isKm: Boolean) {
      isMeterCheckBoxChecked.value = isMeter
      isMileCheckBoxChecked.value = isMile
      isKmCheckBoxChecked.value = isKm
  }*/
/*/*
    var isMapCheckBoxChecked = MutableLiveData<Boolean>()
    var isGpsCheckBoxChecked = MutableLiveData<Boolean>()
    var isEnCheckBoxChecked = MutableLiveData<Boolean>()
    var isArCheckBoxChecked = MutableLiveData<Boolean>()
    var isFrCheckBoxChecked = MutableLiveData<Boolean>()
    var isMileCheckBoxChecked = MutableLiveData<Boolean>()
    var isKmCheckBoxChecked = MutableLiveData<Boolean>()
    var isMeterCheckBoxChecked = MutableLiveData<Boolean>()*/
   /* var isCelsiusCheckBoxChecked = MutableLiveData<Boolean>()
    var isFarnheitCheckBoxChecked = MutableLiveData<Boolean>()
    var isKelvinCheckBoxChecked = MutableLiveData<Boolean>()*/
   private val locationChangeSemaphore = Semaphore(1)*/

/*  isMapCheckBoxChecked.value = false
        isGpsCheckBoxChecked.value = false*/
/*  isCelsiusCheckBoxChecked.value = false
  isFarnheitCheckBoxChecked.value = false
  isKelvinCheckBoxChecked.value = false*/

/*fun updateLocationSetting(location: String) {
     viewModelScope.launch(Dispatchers.IO) {

             Log.d("WeatherSettings", "Before SharedPreferences update: ${sharedPreferences.getString(Constants.UNIT, "")}")
             sharedPreferences.addString(Constants.UNIT, location)
             Log.d("WeatherSettings", "After SharedPreferences update: ${sharedPreferences.getString(Constants.UNIT, "")}")
         }

 }
*/

/*suspend fun updateLocationSetting(location: String) {
    withContext(Dispatchers.IO) {
        sharedPreferences.addString(Constants.IsMap, location)
        Log.d("WeatherVmSettings", "updateLocationSetting to $location")
    }
    withContext(Dispatchers.Main) {
        _selectedLocationSetting.value = location
    }
}*/

/*
    fun updateLocationSetting(location: String) {
      //_selectedLocationSetting.value = location

        CoroutineScope(Dispatchers.IO).launch{
            try {
                synchronized(sharedPreferences){
                    Log.d("WeathervmSettings", "Before SharedPreferences update: ${sharedPreferences.getString(Constants.LOCATION, "")}")
                    sharedPreferences.addString(Constants.LOCATION, location)
                    Log.d("WeathervmSettings", "After SharedPreferences update: ${sharedPreferences.getString(Constants.LOCATION, "")}")
                }

            } catch (e: Exception) {
                // Handle the exception (e.g., log or show an error message)
                Log.e("WeatherSettings", "An error occurred: $e")
            }
        }
    }
*/
/*@BindingAdapter("onCheckedChanged")
fun updateLocationSetting(location: String) {
    viewModelScope.launch(Dispatchers.IO) {
        try {

            updateSharedPreferencesLocation(location)

        } catch (e: Exception) {
            // Handle exceptions if needed
            Log.e("SettingsViewModel", "An error occurred: $e")
        }
    }
}*/
/*fun handleLocationChange(
      isMap: Boolean,
      isGps: Boolean
  )*//*: String*//* {
          viewModelScope.launch(Dispatchers.Main) {
              try {
                  val currentLocationChange = _locationChangeLiveData.value
                  if (currentLocationChange == null || currentLocationChange.first != isMap || currentLocationChange.second != isGps) {


                      _locationChangeLiveData.value = Pair(isMap, isGps)
                     // _selectedLocationSetting.postValue(newLocation)
                      Log.d("WeatherVmSettings", "handlelocationChange to is map $isMap , is gps $isGps")
                      // _selectedLocationSetting.postValue(location)
                  }
              } catch (e: Exception) {
                  Log.e("WeatherVmSettings", "An error occurred: $e")
              }
          }
     // return location
  }

    fun handleTempChange(isCelsius: Boolean, isFahrenheit: Boolean, isKelvin: Boolean) {
        _tempChangeLiveData.value = Triple(isCelsius, isFahrenheit, isKelvin)
    }

    // Implement the new handle method
    fun handleSpeedChange(isMeter: Boolean, isMile: Boolean, isKm: Boolean) {
        _speedChangeLiveData.value = Triple(isMeter, isMile, isKm)
    }*/


/*  var isAllowCheckBoxChecked = MutableLiveData<Boolean>()

    private val _locationChangeLiveData = MutableLiveData<Pair<Boolean, Boolean>>()
    val locationChangeLiveData: LiveData<Pair<Boolean, Boolean>> = _locationChangeLiveData

    private val _tempChangeLiveData = MutableStateFlow(Triple(false,false,false))
    val tempChangeLiveData: StateFlow<Triple<Boolean, Boolean, Boolean>> = _tempChangeLiveData

    // Add LiveData for the new handle method
    private val _speedChangeLiveData = MutableLiveData<Triple<Boolean, Boolean, Boolean>>()
    val speedChangeLiveData: LiveData<Triple<Boolean, Boolean, Boolean>> = _speedChangeLiveData

    private val _languageChangeLiveData = MutableLiveData<Triple<Boolean, Boolean, Boolean>>()
    val languageChangeLiveData: LiveData<Triple<Boolean, Boolean, Boolean>> = _languageChangeLiveData*/

/*@BindingAdapter("onItemSelected", "location")
        fun updateLocationSetting(autoCompleteTextView: AutoCompleteTextView, listener: AdapterView.OnItemSelectedListener?, location: String) {
            autoCompleteTextView.onItemSelectedListener = listener
            autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
                val selectedLocation = autoCompleteTextView.adapter.getItem(position).toString()
                viewModelScope.launch(Dispatchers.IO) {
                    val currentLocation = selectedLocationSetting.value
                    val locationChanged = currentLocation != selectedLocation
                    _selectedLocationSetting.postValue(selectedLocation)
                    if (locationChanged) {
                        updateSharedPreferencesLocation(selectedLocation)
                        _selectedIsEnabled.postValue(true)
                    }
                }
            }
            }*/
/*  fun updateLocationSetting(selectedLocation: String) {
      viewModelScope.launch(Dispatchers.IO) {
          val currentLocation = selectedLocationSetting.value
          val locationChanged = currentLocation != selectedLocation
          _selectedLocationSetting.postValue(selectedLocation)
          if (locationChanged) {
              updateSharedPreferencesLocation(selectedLocation)
              _selectedIsEnabled.postValue(true)
          }
      }
  }*/
/*val onItemSelectedListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedLocation = parent?.adapter?.getItem(position).toString()
        viewModelScope.launch(Dispatchers.IO) {
            val currentLocation = selectedLocationSetting.value
            val locationChanged = currentLocation != selectedLocation
            _selectedLocationSetting.postValue(selectedLocation)
            if (locationChanged) {
                updateSharedPreferencesLocation(selectedLocation)
                _selectedIsEnabled.postValue(true)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Handle case when nothing is selected (if needed)
    }
}*/
/*  fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
      val selectedLocation = parent?.adapter?.getItem(position).toString()
      viewModelScope.launch(Dispatchers.IO) {
          val currentLocation = selectedLocationSetting.value
          val locationChanged = currentLocation != selectedLocation
          _selectedLocationSetting.postValue(selectedLocation)
          if (locationChanged) {
              updateSharedPreferencesLocation(selectedLocation)
              _selectedIsEnabled.postValue(true)
          }
      }
  }*/
/* fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedLocation = (parent?.adapter?.getItem(position) as? String) ?: ""
        viewModelScope.launch(Dispatchers.IO) {
            val currentLocation = selectedLocationSetting.value
            val locationChanged = currentLocation != selectedLocation
            _selectedLocationSetting.postValue(selectedLocation)
            if (locationChanged) {
                updateSharedPreferencesLocation(selectedLocation)
                _selectedIsEnabled.postValue(true)
            }
        }
    }*/

/* @BindingAdapter("onItemSelected")
 fun setOnItemSelectedListener(autoCompleteTextView: AutoCompleteTextView, listener: AdapterView.OnItemSelectedListener?) {
     autoCompleteTextView.onItemSelectedListener = listener
 }
*/
/* @BindingAdapter("onItemSelected")
 fun setOnItemSelectedListener(autoCompleteTextView: AutoCompleteTextView, onItemSelected: ((parent: AdapterView<*>, view: View?, position: Int, id: Long) -> Unit)?) {
     autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
         onItemSelected?.invoke(parent, view, position, id)
     }
 }*/
//}