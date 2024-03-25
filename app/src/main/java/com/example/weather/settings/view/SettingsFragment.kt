package com.example.weather.settings.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weathear.R
import com.example.weathear.databinding.SettingstestBinding
import com.example.weather.model.sharedPrefrence.SharedPrefrences
import com.example.weather.settings.viewModel.SettingsViewModel
import com.example.weather.settings.viewModel.SettingsViewModelFactory
import com.example.weather.utility.helper.Constants
import com.example.weather.utility.helper.Constants.setLocale
import com.example.weather.utility.locationConnection.LocationViewModel
import com.example.weather.utility.networkconnection.NetworkStateManager
import java.util.*


class SettingsFragment : Fragment() {


    lateinit var viewModel: SettingsViewModel
    lateinit var viewModelFactory: SettingsViewModelFactory
    lateinit var binding: SettingstestBinding
    lateinit var locationViewModel: LocationViewModel

    // @Inject
    val sharedPrefrences: SharedPrefrences by lazy {
        SharedPrefrences(requireContext())
    }
    lateinit var networkStateListner: NetworkStateManager

    private var mapOptionSelected = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         requireContext().setLocale(sharedPrefrences.getString(Constants.LANGUAGE, "en"))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.settingstest, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelFactory = SettingsViewModelFactory(SharedPrefrences(requireContext()))
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(SettingsViewModel::class.java)
        locationViewModel = ViewModelProvider(requireActivity()).get(LocationViewModel::class.java)
        networkStateListner = NetworkStateManager(requireContext())

        val languages = resources.getStringArray(R.array.Languages)
        val temp = resources.getStringArray(R.array.TempUnit)
        val speed = resources.getStringArray(R.array.WindUnit)
        val location = resources.getStringArray(R.array.Locations)

        val spinnerLanguages: Spinner = binding.spinnerLanguages
        val spinnerLocations: Spinner = binding.spinnerLocation
        val spinnerTemp: Spinner = binding.spinnerTempUnit
        val spinnerSpeed: Spinner = binding.spinnerSpeedUnit

        setupSpinner(spinnerLanguages, languages) { selectedItem ->
            if (selectedItem.isNotEmpty()) {
                viewModel.updateLanguageSetting(selectedItem)
                Log.d("WeatherSettings ", "updateLanguageSetting to " + selectedItem)

            }
        }
        setupSpinner(spinnerLocations, location) { selectedItem ->
            if (selectedItem.isNotEmpty()) {
                viewModel.updateLocation(selectedItem)
                Log.d("WeatherSettings ", "updateLocation to " + selectedItem)

            }
            if (networkStateListner.isConnected()) {
                if (selectedItem == "Map" && !mapOptionSelected) {
                    val action =
                        SettingsFragmentDirections.actionFragmentSettingsToMapFragment(false)
                    Navigation.findNavController(requireView()).navigate(action)
                    mapOptionSelected = true
                    Log.d("WeatherSettings ", "inside map navigation to " + selectedItem)

                }
                if(selectedItem == "GPS"){
                    if (locationViewModel.isLocationEnabled()) {
                        // Location is enabled, proceed with your logic here
                    } else {
                        Toast.makeText(requireContext(), "Please Turn on your location", Toast.LENGTH_LONG).show()
                    }
                }
            } else
                Toast.makeText(
                    requireContext(),
                    "Please check your Network connection",
                    Toast.LENGTH_SHORT
                ).show()
        }
        setupSpinner(spinnerTemp, temp) { selectedItem ->
            if (selectedItem.isNotEmpty()) {
                viewModel.updateTempUnit(selectedItem)
                Log.d("WeatherSettings ", "updateTempUnit to " + selectedItem)

            }
        }

        setupSpinner(spinnerSpeed, speed) { selectedItem ->
            if (selectedItem.isNotEmpty()) {
                viewModel.updateSpeedUnit(selectedItem)
                Log.d("WeatherSettings ", "updateSpeedUnit to " + selectedItem)

            }
        }
        handleBackBtn()
    }
    private fun handleBackBtn(){
        binding.backBtn.setOnClickListener{
            val action = SettingsFragmentDirections.actionFragmentsetToFragmentHome()
            Navigation.findNavController(requireView()).navigate(action)
        }
    }
    private fun setupSpinner(spinner: Spinner, dataArray: Array<String>, onItemSelectedAction: (selectedItem: String) -> Unit) {
        val adapter = ArrayAdapter(requireContext(), R.layout.drop_down_row, dataArray)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                onItemSelectedAction(selectedItem)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

        }
        }
    }

   /* fun getSelectedLoc(): String {
        return sharedPrefrences.getString(Constants.LOCATION, "")
    }
    fun getSelectedTempUnit(): String {
        return sharedPrefrences.getString(Constants.TEMP_UNIT, "")
    }
    fun getSelectedSpeedUnit(): String {
        return sharedPrefrences.getString(Constants.SPEED_UNIT, "")
    }
    fun getSelectedLang(): String {
        return sharedPrefrences.getString(Constants.LANGUAGE, "")
    }*/
}























   /* fun onHeaderClicked(group: View) {
        group.visibility = if (group.visibility == View.VISIBLE) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
        val otherGroups = listOf(
            binding.UnitsGroup,
            binding.langusgeGroup,
            binding.notificationGroup,
            binding.locationGroup
        )

        val currentGroupIndex = otherGroups.indexOf(group)

        for (i in currentGroupIndex + 1 until otherGroups.size) {
            val currentOtherGroup = otherGroups[i]
            currentOtherGroup.translationY = if (group.visibility == View.VISIBLE) {
                group.height.toFloat()
            } else {
                0f
            }
        }
    }


    private fun observeLocation() {
        viewModel.selectedIsEnabled.observe(viewLifecycleOwner) { isEnabled ->
            if (isEnabled) {
                val selectedLocation = viewModel.selectedLocationSetting.value
                if (selectedLocation == "Map") {
                    val action =
                        SettingsFragmentDirections.actionFragmentSettingsToMapFragment(false)
                    Navigation.findNavController(requireView()).navigate(action)
                    viewModel._selectedIsEnabled.postValue(false)
                }
            }
        }
    }
    fun observeLanguage() {
        viewModel.selectedLanguageSetting.observe(viewLifecycleOwner) { language ->
            language?.let {
                context?.setLocale(language)
                Log.d("WeatherSettingsFragment ","observeLanguage to "+ language)

            }
        }
    }*/































// binding.viewModel = viewModel
// sharedPrefrences = SharedPrefrences(requireContext())

/*  binding.location.setOnClickListener { onHeaderClicked(binding.locationGroup) }
binding.units.setOnClickListener { onHeaderClicked(binding.UnitsGroup) }
binding.language.setOnClickListener { onHeaderClicked(binding.langusgeGroup) }
binding.notification.setOnClickListener { onHeaderClicked(binding.notificationGroup) }*/


// observeLocation()
//observeLanguage()
/* val spinner: Spinner = binding.spinner
//  val autoCompleteTextView: AutoCompleteTextView = binding.autoCompleteTextView
val textInputLayout: TextInputLayout = binding.textInputLayout
 val languages  = resources.getStringArray(R.array.languages)
// Use custom layout for dropdown items
val adapter = ArrayAdapter(requireContext(), R.layout.drop_down_row , languages)
spinner.adapter = adapter
// autoCompleteTextView.setAdapter(adapter)
  // spinner.onItemSelectedListener = viewModel.onItemSelectedListener
spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedLocation = parent?.getItemAtPosition(position).toString()
        viewModel.onItemSelected(selectedLocation)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Handle case when nothing is selected (if needed)
    }
}*/
/* private fun observeLocation() {
     viewModel.selectedIsEnabled.observe(viewLifecycleOwner) { settingsInteracted ->
          if (settingsInteracted) {
              Log.d("WeatherSettingsFragment ","isAllowCheckBoxChecked to "+ settingsInteracted)
              //viewModel.selectedLocationSetting.removeObservers(viewLifecycleOwner)
              viewModel.selectedLocationSetting.observe(viewLifecycleOwner) { map ->
                  if (map == "Map" && view != null) {
                      *//*val radioButton: RadioButton? = view?.findViewById(R.id.mapCheckBox)
                        radioButton?.isChecked = false*//*
                       // Log.d("WeatherSettingsFragment ","isAllowCheckBoxChecked to "+ settingsInteracted)
                        val action =
                            SettingsFragmentDirections.actionFragmentSettingsToMapFragment(false)
                        Navigation.findNavController(requireView()).navigate(action)
                    }
                }
            }
      }
  }*/

  /*  private fun observeTempUnits() {
        viewModel.selectedtempUnit.observe(viewLifecycleOwner) { units ->
            when (units) {
                "celsius" -> {binding.celsiusRadio.isChecked = true
                viewModel.updateTempUnit("celsius")}
                "fahrenheit" -> {binding.fahrenheitRadio.isChecked = true
                viewModel.updateTempUnit("fahrenheit")}
                "kelvin" -> {binding.kelvinRadio.isChecked = true
                viewModel.updateTempUnit("kelvin")}
                else -> binding.celsiusRadio.isChecked = true

            }
            Log.d("WeatherSettings ","observeTempUnits to "+ units)
        }
    }*/

  /*  private fun observeNotification() {
        viewModel.selectedIsEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.allowNotification.isEnabled = isEnabled
            viewModel.updateNotificationSetting(isEnabled)
            Log.d("WeatherSettings ","observeNotification to "+ isEnabled)

        }
    }*/



    /*private fun observeSpeedUnits() {
        viewModel.selectedspeedUnit.observe(viewLifecycleOwner) { units ->
            // Use the correct IDs for the RadioButtons in your layout
            when (units) {
                "metric" -> {binding.meterRadio.isChecked = true
                viewModel.updateSpeedUnit("metric")}
                "imperial" -> {binding.mileRadio.isChecked = true
                viewModel.updateSpeedUnit("imperial")}
                "standard" -> {binding.kmRadio.isChecked = true
                viewModel.updateSpeedUnit("standard")}
                else -> binding.meterRadio.isChecked = true
            }
            Log.d("WeatherSettings ","observeSpeedUnits to "+ units)

        }
    }
    fun observeLanguage() {
        viewModel.selectedLanguageSetting.observe(viewLifecycleOwner) { languages ->
            languages?.let {
                when (it) {
                    "ar" -> { binding.arabicBox.isChecked = true
                      viewModel.updateLanguageSetting("ar")}
                    "en" -> {binding.englishBox.isChecked = true
                    viewModel.updateLanguageSetting("en")}
                    "fr" -> {
                        binding.frenchBox.isChecked = true
                        viewModel.updateLanguageSetting("fr")
                    }
                    else -> binding.englishBox.isChecked = true
                }
                requireContext().setLocale(languages)
            }
            Log.d("WeatherSettings ","observeSpeedUnits to "+ languages)

        }*/





    /*  fun observeLocation() {
          viewModel.selectedLocationSetting.observe(viewLifecycleOwner) { locations ->
              when (locations) {
                  "map" -> {
                      binding.mapCheckBox.isChecked = true
                    //  viewModel.updateLocationSetting("map")
                      val action =
                          SettingsFragmentDirections.actionFragmentSettingsToMapFragment(false)
                          Navigation.findNavController(requireView()).navigate(action)
                  }
                  "gps" -> { binding.gpsCheckBox.isChecked = true}
                         // viewModel.updateLocationSetting("gps") }

                  else -> {
                      binding.gpsCheckBox.isChecked = false
                      binding.mapCheckBox.isChecked = false
                  }

              }
              Log.d("WeatherSettings ","observeLocation to "+ locations)

          }

      }*/

    /*override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("radioButtonStateKey", binding.mapCheckBox.isChecked)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val savedState = savedInstanceState?.getBoolean("radioButtonStateKey", false) ?: false
        binding.mapCheckBox.isChecked = savedState
    }
    override fun onPause() {
        super.onPause()
     //   viewModel.selectedIsEnabled.value = null
    }*/
/*
    override fun onDestroyView() {
        super.onDestroyView()
         viewModel.selectedLocationSetting.removeObservers(viewLifecycleOwner)
        viewModel.selectedIsEnabled.removeObservers(viewLifecycleOwner)
        // Unregister other observers as needed
    }
*/
    /*override fun onDestroy() {
        super.onDestroy()
        viewModel.selectedLocationSetting.value?.let {
            Log.d("WeatherSettingsFragment", "selectedLocationSetting before navigation: $it")
        }

//        viewModel.selectedLocationSetting.removeObservers(viewLifecycleOwner)
  //      viewModel.selectedIsEnabled.removeObservers(viewLifecycleOwner)

        // Manually clear the checked state of the RadioButton
       // binding.mapCheckBox.isChecked = false

        viewModel.selectedLocationSetting.value?.let {
            Log.d("WeatherSettingsFragment", "selectedLocationSetting after navigation: $it")
        }*/
/*
        val radioButton: RadioButton? = view?.findViewById(R.id.mapCheckBox)
        radioButton?.isChecked = false
*/



   /* private fun changeLanguageLocaleTo(lan: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(lan)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
*/




/* viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
           viewModel.speedChangeLiveData.observe(viewLifecycleOwner) { (isMeter, isMile, isKm) ->
               if (isMeter || isMile || isKm) {
                   viewModel.handleSpeedChange(isMeter, isMile, isKm)
                   Log.d("WeatherSettings", "handleSpeedChange to $isMeter$isMile$isKm")
               }
           }
       }*/
/* viewModel.locationChangeLiveData.distinctUntilChanged().observe(viewLifecycleOwner) { (isMap, isGps) ->
     if (isMap || isGps) {
         Log.d("WeatherSettingsFragment", "isMap: $isMap, isGps: $isGps")
         viewModel.handleLocationChange(isMap, isGps)

         try {
             viewModel.selectedLocationSetting.observe(viewLifecycleOwner) { newLocation ->
                 Log.d("WeatherSettingsFragment", "selectedLocationSetting changed: $newLocation")
                     viewModel.updateLocationSetting(newLocation)
                     Log.d("WeatherSettingsFragment", "updated location to   $newLocation")

                     *//*   location = newLocation
                        if (updatedLocation != location) {
                            location = updatedLocation
                            viewModel.updateLocationSetting(updatedLocation)
                            Log.d("WeatherSettingsFragment", "updateLocationSetting to new  $updatedLocation")
                        }
*//*
                        }
                }catch (e: Exception) {
                        // Handle the exception (e.g., log or show an error message)
                        Log.e("WeatherSettingsFragment", "An error occurred: $e")
                    }

            }
        }*/



// Observe location changes
/* viewModel.locationChangeLiveData.distinctUntilChanged().observe(viewLifecycleOwner) { (isMap, isGps) ->
     if (isMap || isGps) {
         Log.d("WeatherSettingsFragment", "isMap: $isMap, isGps: $isGps")
         viewModel.handleLocationChange(isMap, isGps)
     }
 }*/

// Observe selected location changes
/*viewModel.selectedLocationSetting.distinctUntilChanged().observe(viewLifecycleOwner) { newLocation ->
    Log.d("WeatherSettingsFragment", "selectedLocationSetting changed: $newLocation")
    viewModel.updateLocationSetting(newLocation)
    Log.d("WeatherSettingsFragment", "updated location to $newLocation")
}*/

/* viewModel.locationChangeLiveData.observe(viewLifecycleOwner) { (isMap, isGps) ->
     if (isMap || isGps) {
         viewModel.selectedLocationSetting.observe(viewLifecycleOwner) { newLocation ->
             location = newLocation
             Log.d("WeatherSettingsFragment", "Location updated to: $newLocation")
             // Update your UI or perform other necessary actions here.

             // Move heavy operations to a background thread
             lifecycleScope.launch {
                 val newLocation = withContext(Dispatchers.Default) {
                     viewModel.handleLocationChange(isMap, isGps, location)
                 }

                 Log.d("WeatherSettingsFragment", "handleLocationChange from old  $isMap$isGps")

                 if (newLocation != location) {
                     location = newLocation
                     viewModel.updateLocationSetting(location)
                     Log.d("WeatherSettingsFragment", "handleLocationChange to new  $isMap$isGps")
                 }
             }
         }
     }
 }*/

/*
        viewModel.locationChangeLiveData.observe(viewLifecycleOwner) { (isMap, isGps) ->
            if (isMap || isGps) {
                viewModel.selectedLocationSetting.observe(viewLifecycleOwner) { newLocation ->
                    location = newLocation
                    Log.d("WeatherSettingsFragment", "Location updated to: $newLocation")
                    // Update your UI or perform other necessary actions here.
                    val newLocation = viewModel.handleLocationChange(isMap, isGps, location)
                    Log.d("WeatherSettingsFragment", "handleLocationChange from old  $isMap$isGps")

                    if (newLocation != location) {
                        location = newLocation
                        viewModel.updateLocationSetting(location)
                        Log.d("WeatherSettingsFragment", "handleLocationChange to new  $isMap$isGps")
                    }
                }

                // Move heavy operations to a background thread
               */
/* lifecycleScope.launch(Dispatchers.Default) {

                }*//*

            }
        }
*/

/* viewModel.locationChangeLiveData.observe(viewLifecycleOwner) { (isMap, isGps) ->
     if (isMap || isGps) {
         viewModel.selectedLocationSetting.observe(viewLifecycleOwner) { newLocation ->
             // This observer will be triggered whenever selectedLocationSetting changes.
             location = newLocation
             Log.d("WeatherSettingsFragment", "Location updated to: $newLocation")

             // Update your UI or perform other necessary actions here.
         }

         // Now location is updated, proceed with handleLocationChange
         val newLocation = viewModel.handleLocationChange(isMap, isGps, location)
         Log.d("WeatherSettingsFragment", "handleLocationChange from old  $isMap$isGps")

         if (newLocation != location) {
             location = newLocation
             viewModel.updateLocationSetting(location)
             Log.d("WeatherSettingsFragment", "handleLocationChange to new  $isMap$isGps")
             // Update your UI or perform other necessary actions
         }
     }
 }*/


/*
        viewModel.locationChangeLiveData.observe(viewLifecycleOwner) { (isMap, isGps) ->
            if (isMap || isGps) {
                viewModel.handleLocationChange(isMap, isGps, location)
            }
        }
*/

/*   viewModel.locationChangeLiveData.observe(viewLifecycleOwner) { (isMap, isGps) ->
       if (!isHandlingLocationChange && (isMap || isGps)) {
           // Assuming handleLocationChange returns the new location
           isHandlingLocationChange = true
           val newLocation = viewModel.handleLocationChange(isMap, isGps, location)
           Log.d("WeatherSettingsFragment", "handleLocationChange from old  $isMap$isGps")

           if (newLocation != location) {
               location = newLocation
               Log.d("WeatherSettingsFragment", "handleLocationChange to new  $isMap$isGps")
               // Update your UI or perform other necessary actions
           }
           isHandlingLocationChange = false
       }
   }
*/
/*  viewModel.locationChangeLiveData.observe(viewLifecycleOwner) { (isMap, isGps) ->
      if (isMap || isGps) {
          // Assuming handleLocationChange returns the new location
          val newLocation = viewModel.handleLocationChange(isMap, isGps, location)

          if (newLocation != location) {
              location = newLocation
              Log.d("WeatherSettingsFragment", "handleLocationChange to $isMap$isGps")
              // Update your UI or perform other necessary actions
          }
      }
  }*/
/*viewModel.locationChangeLiveData.observe(viewLifecycleOwner) { (isMap, isGps) ->
    val newLocation = if (isMap || isGps) {
        // Assuming handleLocationChange returns the new location
        viewModel.handleLocationChange(isMap, isGps, location)
    } else {
        "Gps"
    }

    if (newLocation != location) {
        // Assuming handleLocationChange returns the new location
        viewModel.handleLocationChange(isMap, isGps, newLocation)
        location = newLocation
        Log.d("WeatherSettings", "handleLocationChange to $isMap$isGps")
    }
}*/
/* viewModel.locationChangeLiveData.observe(viewLifecycleOwner) { (isMap, isGps) ->
     val newLocation =  if(isMap || isGps) {
         viewModel.handleLocationChange(isMap, isGps, location)
     } else {

     }
     if (newLocation != currentLocation) {
             viewModel.handleLocationChange(isMap, isGps, newLocation)
             currentLocation = newLocation
             Log.d("WeatherSettings", "handleLocationChange to $isMap$isGps")
         }
 }

 viewModel.locationChangeLiveData.observe(viewLifecycleOwner) { (isMap, isGps ) ->
     if(isMap || isGps) {
         viewModel.handleLocationChange(isMap, isGps , location )
         Log.d("WeatherSettings ", "handleLocationChange to " + isMap + isGps)
     }
 }
}*/

/* viewLifecycleOwner.lifecycleScope.launchWhenStarted {
          viewModel.tempChangeLiveData.collect { (isMeter, isMile, isKm) ->
              if (isMeter || isMile || isKm) {
                  viewModel.handleTempChange(isMeter, isMile, isKm)
                  Log.d("WeatherSettings ", "handleTempChange to " + isMeter + isMile+isKm)
                  observeTempUnits()

              }
          }
      }*/
/* val mapRadioButton: RadioButton? = binding.mapCheckBox
mapRadioButton?.isChecked = false*/

//observeLanguage()
// observeSpeedUnits()
//   observeNotification()