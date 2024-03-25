package com.example.weather.alerts.view

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weathear.databinding.FragmentAlertsBinding
import com.example.weather.alerts.viewModel.AlertsViewModel
import com.example.weather.alerts.viewModel.AlertsViewModelFactory
import com.example.weather.home.viewModel.HomeViewModel
import com.example.weather.home.viewModel.HomeViewModelFactory
import com.example.weather.model.localDatabase.ConcreteLocalSource
import com.example.weather.model.pojos.UserWeatherAlert
import com.example.weather.model.remoteDatabase.WeatherClient
import com.example.weather.model.repository.Repository
import com.example.weather.model.sharedPrefrence.SharedPrefrences
import com.example.weather.utility.networkconnection.NetworkStateManager
import com.example.weather.utility.overlayConnection.OverlayPermission
import com.example.weather.utility.workmanager.AlertWorker
import kotlinx.coroutines.launch

class AlertsFragment : Fragment(),AlertClickListner {

    lateinit var viewModel: AlertsViewModel
    lateinit var viewModelFactory: AlertsViewModelFactory
    lateinit var alertsAdapter: AlertsAdapter
    lateinit var binding: FragmentAlertsBinding
    lateinit var alerts: MutableList<UserWeatherAlert>
    lateinit var sharedPrefrences: SharedPrefrences
   lateinit var networkStateManager: NetworkStateManager
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var homeFactory: HomeViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlertsBinding.inflate(inflater, container, false)
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
        homeFactory = HomeViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(),ConcreteLocalSource.getInstance(requireContext())))
        homeViewModel = ViewModelProvider(requireActivity(), homeFactory).get(HomeViewModel::class.java)
        binding.addAlertFloating.setOnClickListener{
            AddingAlertClicked()
        }


        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(AlertsViewModel::class.java)
        sharedPrefrences = SharedPrefrences(requireContext())
        alertsAdapter = AlertsAdapter(requireContext(), this)

        initAlertsRecycler()
        getAllAlerts()
        //insertAlerts()
        handleBackBtn()
        startAlertWorkManager()

    }
    private fun startAlertWorkManager() {
        homeViewModel.weatherList.observe(viewLifecycleOwner) { response ->
            if (response.isNotEmpty()) {
                val latestResponse = response.first()
                val weatherAlerts = latestResponse?.alerts ?: emptyList()
                lifecycleScope.launch {

                    val userWeatherAlert = viewModel.alertsLiveData.value
                    if (userWeatherAlert != null) {
                        AlertWorker.setAlertWorkManager(
                            requireContext(),
                            userWeatherAlert,
                            weatherAlerts
                        )
                        Log.e(
                            "WeatherAlert",
                            "setAlertWorkManager userWeatherAlert size: ${userWeatherAlert.isNullOrEmpty()}"
                        )
                        Log.e(
                            "WeatherAlert",
                            "setAlertWorkManager weatherAlerts size: ${weatherAlerts.size}"
                        )

                    } else {
                        Log.e("WeatherAlert", "userWeatherAlert is null")
                    }
                }
            } else {
                Log.e("WeatherAlert", "response is empty")
            }
        }
    }
    private fun handleBackBtn(){
        binding.backBtn.setOnClickListener{
            val action = AlertsFragmentDirections.actionFragmentAlertsToFragmentHome()
            Navigation.findNavController(requireView()).navigate(action)
        }
    }
    private fun navigateToAlertsDialog() {
        findNavController().navigate(AlertsFragmentDirections.actionFragmentAlertsToFragmentDialog())
    }
    private fun observeNetworkState() {
        networkStateManager.isConnected()}
   /* private fun insertAlerts() {
        //viewModel.insertAlerts()
    }*/

    private fun getAllAlerts() {
        lifecycleScope.launch {
            viewModel.alertsLiveData.collect{ userAlerts ->
                alertsAdapter.submitList(userAlerts)
                Log.e("WeatherAlert", "getAllAlerts list" + userAlerts.size)


            }
        }
   }

    private fun initAlertsRecycler() {
        binding.recyclerViewAlert.apply {
            adapter = alertsAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDeleteItemClicked(userWeatherAlert: UserWeatherAlert) {
        viewModel.deleteAlertItem(userWeatherAlert)
    }
    private fun AddingAlertClicked() {
        if (OverlayPermission.checkOverlayPermission(requireContext())) {
             navigateToAlertsDialog()
        } else {
            OverlayPermission.requestOverlayPermission(this)
        }
    }
    /*private fun scheduleAlertWorkManager() {
        AlertWorker.setAlertWorkManager(requireContext())
    }*/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == OverlayPermission.PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                navigateToAlertsDialog()
            } else {
            }
        }
    }
}
/*val userWeatherAlert = UserWeatherAlert(
           timeFrom = System.currentTimeMillis(), // Example time
           timeTo = System.currentTimeMillis() + 3600000, // Example time
           endDate = System.currentTimeMillis() + 86400000, // Example time
           title = "Example Alert" // Example title
       )
*/
/*  alerts = mutableListOf()
lifecycleScope.launchWhenStarted {

scheduleAlertWorkManager()

      }*/
/*
lifecycleScope.launchWhenStarted {
    viewModel.alertInsertedSuccess.collect {
        if (it) {
            setDailyWorkManger()
        }
    }
}

lifecycleScope.launchWhenStarted {
    viewModel.deletedAlertId.collect { id ->
        Log.e("AlertDialogFragment", "observe delete alertId = $id")
        WorkManager.getInstance(requireContext()).cancelUniqueWork("$id")
    }
}*/