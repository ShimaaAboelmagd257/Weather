package com.example.weather.alerts.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weathear.databinding.FragmentAlertsDialogBinding
import com.example.weather.alerts.viewModel.AlertsViewModel
import com.example.weather.alerts.viewModel.AlertsViewModelFactory
import com.example.weather.model.localDatabase.ConcreteLocalSource
import com.example.weather.model.pojos.UserWeatherAlert
import com.example.weather.model.remoteDatabase.WeatherClient
import com.example.weather.model.repository.Repository
import com.example.weather.model.sharedPrefrence.SharedPrefrences
import com.example.weather.utility.helper.Constants
import com.example.weather.utility.helper.Constants.setLocale
import nl.joery.timerangepicker.TimeRangePicker
import java.util.*

class AlertsDialog : Fragment() {

    
    lateinit var viewModel: AlertsViewModel
    lateinit var viewModelFactory: AlertsViewModelFactory
    lateinit var binding: FragmentAlertsDialogBinding
    val sharedPrefrences: SharedPrefrences by lazy {
        SharedPrefrences(requireContext())
    }

    // lateinit var networkStateManager: NetworkStateManager
    private var timeFrom: Long = 0
    private var timeTo: Long = 0
    private var endDate: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().setLocale(getLanguageLocale())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlertsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModelFactory = AlertsViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())
            )
        )
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(AlertsViewModel::class.java)


        pickerListner()
        alertSaving()
        alertCancelling()


    }
    private fun alertCancelling(){
        binding.cancelButton.setOnClickListener{
            navigationToAlerts()
        }
    }
    private fun alertSaving(){
        binding.saveButton.setOnClickListener {
            // Check if all necessary fields are filled
            //  if ( timeFrom != 0L && timeTo != 0L) {
            // Create UserWeatherAlert object
            val alert = UserWeatherAlert(
                endDate = endDate,
                timeFrom = timeFrom,
                timeTo = timeTo,
                title = binding.alarmTitle.text.toString() // Set title from EditText
            )
            // Insert alert into ViewModel
            viewModel.insertAlert(alert)
            Log.e("WeatherAlertDialog", "Alert saved id " + alert.title)
            Toast.makeText(requireContext(), "Alert saved", Toast.LENGTH_SHORT).show()
            navigationToAlerts()
            /*   } else {
                   Log.e("WeatherAlertDialog", "Incomplete alert data")
                   Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                       .show()
               }*/
        }
    }
    private fun pickerListner(){

        binding.picker.setOnTimeChangeListener(object : TimeRangePicker.OnTimeChangeListener {
            override fun onStartTimeChange(startTime: TimeRangePicker.Time) {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, startTime.hour)
                calendar.set(Calendar.MINUTE, startTime.minute)
                val timeInMillis = calendar.timeInMillis / 1000 // Convert milliseconds to seconds
                timeFrom = timeInMillis
                binding.fromTime.text = Constants.convertLongToTimePicker(timeFrom)

                Log.d("WeatherAlertDialog", "Start time: $timeFrom ${System.currentTimeMillis() / 1000}")
            }

            override fun onEndTimeChange(endTime: TimeRangePicker.Time) {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, endTime.hour)
                calendar.set(Calendar.MINUTE, endTime.minute)
                val timeInMillis = calendar.timeInMillis / 1000 // Convert milliseconds to seconds
                timeTo = timeInMillis
                binding.toTime.text = Constants.convertLongToTimePicker(timeTo)

                Log.d("WeatherAlertDialog", "End time: ${endTime.hour}")
            }

            override fun onDurationChange(duration: TimeRangePicker.TimeDuration) {
                Log.d("WeatherAlertDialog", "Duration: " + duration.hour)
                binding.duration.text = duration.hour.toString()
            }
        })
    }
    private fun navigationToAlerts(){
        val action = AlertsDialogDirections.actionFragmentDialogToFragmentAlerts()
        Navigation.findNavController(requireView()).navigate(action)
    }
    private fun getLanguageLocale(): String {
        return sharedPrefrences.getString(Constants.LANGUAGE, "en")
    }
}

