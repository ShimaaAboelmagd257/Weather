package com.example.weather.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.*
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.weathear.R
import com.example.weathear.databinding.FragmentHomeBinding
import com.example.weather.home.viewModel.HomeViewModel
import com.example.weather.model.sharedPrefrence.SharedPrefrences

import com.example.weather.favorites.viewModel.FavoritesViewModel
import com.example.weather.favorites.viewModel.FavoritesViewModelFactory
import com.example.weather.home.viewModel.HomeViewModelFactory
import com.example.weather.model.localDatabase.ConcreteLocalSource
import com.example.weather.model.pojos.ApiResponse
import com.example.weather.model.pojos.CurrentWeather
import com.example.weather.model.pojos.DailyForecast
import com.example.weather.model.remoteDatabase.WeatherClient
import com.example.weather.model.repository.Repository
import com.example.weather.utility.helper.Constants
import com.example.weather.utility.helper.Constants.convertLongToTimePicker
import com.example.weather.utility.helper.Constants.getAddress
import com.example.weather.utility.helper.Constants.getSpeedUnit
import com.example.weather.utility.helper.Constants.getTemperatureUnit
import com.example.weather.utility.helper.Constants.setLocale
import com.example.weather.utility.locationConnection.LocationViewModel
import com.example.weather.utility.networkconnection.NetworkStateManager
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

      lateinit var favoritesViewModel: FavoritesViewModel
      lateinit var favViewModelFactory: FavoritesViewModelFactory

    private lateinit var viewModel: HomeViewModel
    private lateinit var locationViewModel: LocationViewModel

    private lateinit var homeFactory: HomeViewModelFactory
    private  var bindingHome: FragmentHomeBinding? = null

    private val binding get() = bindingHome!!
   // @Inject
   val sharedPrefrences: SharedPrefrences by lazy {
       SharedPrefrences(requireContext())
   }

      private  lateinit  var lottieAnimationView: LottieAnimationView
      private lateinit var lottieAnimationClouds: LottieAnimationView
    private val hourlyWeatherAdapter by lazy { HourlyWeatherAdapter()  }
    private val dailyWeatherAdapter by lazy { DailyWeatherAdapter() }
    lateinit var networkStateListner: NetworkStateManager

      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          requireContext().setLocale(getLanguageLocale())
      }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        bindingHome = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeFactory = HomeViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(),ConcreteLocalSource.getInstance(requireContext())))
        viewModel = ViewModelProvider(requireActivity(), homeFactory).get(HomeViewModel::class.java)
        locationViewModel = ViewModelProvider(requireActivity()).get(LocationViewModel::class.java)
        //sharedPrefrences = SharedPrefrences(requireContext())
        networkStateListner = NetworkStateManager(requireContext())

        favViewModelFactory = FavoritesViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())
            )
        )
        favoritesViewModel = ViewModelProvider(
            requireActivity(),
            favViewModelFactory
        ).get(FavoritesViewModel::class.java)

        initDailyRecycler()
        initHourlyRecycler()
       // displayAnnimation()
        displayAnnimationClouds()
        checkLocation()
        loadCachedData()
        displayCachedData()
        handleMenuBtn()
        handleSaveBtn()
        onBackPressed()
    }
      private fun displayAnnimationClouds(){
          lottieAnimationClouds = binding.cloudsBackground
          lottieAnimationClouds.setAnimation("stars.json")
          lottieAnimationClouds.playAnimation() // Start the animation
      }

        private fun onBackPressed(){
            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            }

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        }

       private fun handleSaveBtn() {
          val saveButton = binding.saveBtn
          saveButton.setOnClickListener {
              val action = HomeFragmentDirections.actionFragmentHomeToFragmentFav()

              // Set up animation options
              val navOptions = NavOptions.Builder()
                  .setEnterAnim(R.anim.slidein)
                  .setExitAnim(R.anim.slideout)
                  .setPopEnterAnim(R.anim.slidein)
                  .setPopExitAnim(R.anim.slideout)
                  .build()

              Navigation.findNavController(saveButton).navigate(action,navOptions)
          }
      }
      private fun handleMenuBtn(){
          val menuButton = binding.menuButton
          menuButton.setOnClickListener {
              Log.d("menuButtonListner", "Before invoking the  fun menu")

              showPopupMenu(menuButton)
          }
      }
      private fun showPopupMenu(menuButton:View) {
          // val menuButton: ImageView = findViewById(R.id.menuButton)
          val navController = findNavController()
          Log.d("PopupMenu", "Before inflating menu")
          //  menuButton.setOnClickListener {
          val popupMenu = PopupMenu(requireContext(), menuButton)
          popupMenu.menuInflater.inflate(R.menu.menu_main, popupMenu.menu)

          // Set the menu item click listener
          popupMenu.setOnMenuItemClickListener { menuItem ->
              //val navController: NavController = Navigation.findNavController(this, R.id.homefr)
              when (menuItem.itemId) {
                  R.id.fragmentset -> {
                      // Handle the fragmentset item click
                      val action =
                         HomeFragmentDirections.actionFragmentHomeToFragmentset()
                      Navigation.findNavController(menuButton).navigate(action)
                     true
                  }
                  R.id.fragmentAlerts -> {
                      val action = HomeFragmentDirections.actionFragmentHomeToFragmentAlerts()
                      Navigation.findNavController(menuButton).navigate(action)
                      true
                  }

                  else -> false
              }

          }
          Log.d("PopupMenu", "After inflating menu")

          // Show the PopupMenu
          popupMenu.show()
          //   }
      }
      /* )*/

      private fun loadCachedData(){
          viewModel.loadCachedWeatherData()

      }
      private fun displayCachedData(){
          viewModel.weatherList.observe(viewLifecycleOwner) { response ->
              if (response.isNotEmpty()) {
                  val latestResponse = response.first()
                  hourlyWeatherAdapter.submitList(latestResponse?.hourly)
                  dailyWeatherAdapter.submitList(latestResponse?.daily)
                  initUi(latestResponse)
                  sharedPrefrences.addString(Constants.LAT, latestResponse?.lat.toString())
                  sharedPrefrences.addString(Constants.LON, latestResponse?.lon.toString())
                  Log.d("WeatherHomeFragment", "displayCashedData: ${response.size} ${latestResponse?.lat} ${latestResponse?.lon}")


              }
          }
      }
    /*  private fun displayAnnimation(){
          lottieAnimationView = binding.animationDayTime
          lottieAnimationView.setAnimation("day.json")
          lottieAnimationView.playAnimation() // Start the animation
      }*/
   private fun checkLocation() {
       if (networkStateListner.isConnected()) {
           Log.d("WeatherHomeFragment", "checkNetwork: " + networkStateListner.isConnected())
           when (getSelectedLoc()) {
               "Map" -> observeDesiredLocation()
               "GPS" -> observeCurrentLocation()
               "Fav" -> observeFavLocation()
           }

       }
    }
     /* // When fetching weather data in the home screen, pass location data to WorkManager
      private fun fetchWeatherDataAndUpdateWorkManager(lat: Double, lon: Double) {
          // Pass location data to WorkManager
          AlertWorker.setAlertWorkManager(requireContext(), lat, lon)

          // Fetch weather data for display on home screen
          viewModel.getApiCallResponse(lat, lon, getUnits(), getLanguageLocale())
      }

      // Modify observe methods to pass location data to fetchWeatherDataAndUpdateWorkManager
      private fun observeFavLocation() {
          favoritesViewModel.favLocationLiveData.observe(viewLifecycleOwner) { favLocation ->
              if (favLocation != null) {
                  fetchWeatherDataAndUpdateWorkManager(favLocation.lat, favLocation.lon)
                  Log.d("WeatherHomeFragment", "displayCashedData: ${favLocation.lat} ${favLocation.lon}")
              }
          }
      }

      private fun observeCurrentLocation() {
          locationViewModel.locationLiveData.observe(viewLifecycleOwner) { currentLocation ->
              if (currentLocation != null && (currentLocation.lat != getLat() || currentLocation.lon != getLon())) {
                  fetchWeatherDataAndUpdateWorkManager(currentLocation.lat, currentLocation.lon)
                  Log.d("WeatherHomeFragment", "displayCashedData: ${currentLocation.lat} ${currentLocation.lon}")
              }
          }
      }

      private fun observeDesiredLocation() {
          locationViewModel.desiredLocationLiveData.observe(viewLifecycleOwner) { desiredLocation ->
              if (desiredLocation != null) {
                  fetchWeatherDataAndUpdateWorkManager(desiredLocation.lat, desiredLocation.lon)
                  Log.d("WeatherHomeFragment", "displayCashedData: ${desiredLocation.lat} ${desiredLocation.lon}")
              }
          }
      }*/

       fun observeFavLocation() {
        favoritesViewModel.favLocationLiveData.observe(viewLifecycleOwner) { favLocation ->
            if(favLocation != null){
            viewModel.getApiCallResponse(favLocation.lat, favLocation.lon, getUnits(), getLanguageLocale())
            Log.d("WeatherHomeFragment", "displayCashedData: ${favLocation.lat} ${favLocation.lon}")
            }
        }
    }
      private fun observeCurrentLocation(){
          locationViewModel.locationLiveData.observe(viewLifecycleOwner) { currentLocation ->
              if (currentLocation != null && (currentLocation.lat != getLat() || currentLocation.lon != getLon())) {
                  viewModel.getApiCallResponse(currentLocation.lat, currentLocation.lon, getUnits(), getLanguageLocale())

                  Log.d("WeatherHomeFragment", "displayCashedData: ${currentLocation.lat} ${currentLocation.lon}")

              }
          }
      }
      private fun observeDesiredLocation(){
          locationViewModel.desiredLocationLiveData.observe(viewLifecycleOwner){ desiredLocation ->
              if (desiredLocation != null){
              viewModel.getApiCallResponse(desiredLocation.lat, desiredLocation.lon, getUnits(), getLanguageLocale())
              Log.d("WeatherHomeFragment", "displayCashedData: ${desiredLocation.lat} ${desiredLocation.lon}")
}
          }
      }

    private fun initHourlyRecycler() {
        binding.hourlyWeatherRv.apply {
           adapter = hourlyWeatherAdapter
           setHasFixedSize(true)
           layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
       }
    }

    private fun initDailyRecycler() {
        binding.weeklyWeatherRv.apply {
            adapter = dailyWeatherAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

      private fun initUi(apiResponse: ApiResponse?) {
          binding.apply {
              apiResponse?.current?.let { currentWeather ->
                  setTodayInfo(currentWeather)
                  setTodayForecast(apiResponse.daily?.get(0))
                  tvAddress.text = getAddress(requireContext(), apiResponse.lat ?: 0.0, apiResponse.lon ?: 0.0)
                  sharedPrefrences.addString(Constants.COUNTRY_NAME,tvAddress.text.toString())
              }
          }
      }

      private fun setTodayInfo(currentWeather: CurrentWeather) {
          binding.apply {
              val calendar = Calendar.getInstance()
              val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
              val dayName = getDayName(dayOfWeek)
              today.text = dayName
              tvTemp.text = formatTemperature(currentWeather.temp)
              headTemp.text = formatTemperature(currentWeather.temp)
              tvPressureDeg.text = formatPressure(currentWeather.pressure)
              tvWindDeg.text = formatWind(currentWeather.wind_speed)
              tvHumidityDeg.text = formatHumidity(currentWeather.humidity)
              skyStatus.text = formatCloud(currentWeather.clouds)
              tvUvDeg.text = formatUV(currentWeather.uvi)
              tvVisibilityDeg.text = formatVisibility(currentWeather.visibility)
              setTime.text = convertLongToTimePicker(  currentWeather.sunset)
              riseTime.text = convertLongToTimePicker( currentWeather.sunrise)
          }
      }

      private fun setTodayForecast(todayForecast: DailyForecast?) {
          binding.apply {
              todayForecast?.let {
                  val todayMinTemp = it.temp.min
                  val todayMaxTemp = it.temp.max
                  val strFormat = getString(
                      R.string.max_min_unit,
                      todayMinTemp.toInt(),
                      todayMaxTemp.toInt(),
                      getTemperatureUnit(requireContext())
                  )
                  minMaxTemp.text = strFormat
              }
          }
      }


      private fun formatTemperature(temperature: Double?): String {
        return getString(
            R.string.temp_deg,
            temperature?.toInt() ?: 0,
            getTemperatureUnit(requireContext())
        )
    }


    private fun formatPressure(pressure: Double?): String {
        return getString(
            R.string.pressure_deg,
            pressure ?: 0.0,
            getString(R.string.hpa)
        )
    }

    private fun formatWind(windSpeed: Double?): String {
        return getString(
            R.string.wind_deg,
            windSpeed?.toInt() ?: 0,
            getSpeedUnit(requireContext())
        )
    }

    private fun formatHumidity(humidity: Double?): String {
        return getString(
            R.string.humidity_deg,
            humidity?.toInt() ?: 0,
            "%"
        )
    }

    private fun formatCloud(clouds: Double?): String {
        return getString(
            R.string.cloud_deg,
            clouds?.toInt() ?: 0,
            "%"
        )
    }

    private fun formatUV(uvi: Double?): String {
        return getString(
            R.string.uv_deg,
            uvi?.toInt() ?: 0,
            " "
        )
    }

    private fun formatVisibility(visibility: Int?): String {
        return getString(
            R.string.visibility_deg,
            visibility ?: 0,
            " "
        )
    }

    private fun getLanguageLocale(): String {
        return sharedPrefrences.getString(Constants.LANGUAGE, "en")
    }

    private fun getUnits(): String {
        return sharedPrefrences.getString(Constants.SPEED_UNIT, "metric")
    }

      private fun getLat(): Double {
          return sharedPrefrences.getString(Constants.LAT, "0.0")?.toDouble() ?: 0.0
      }

      private fun getLon(): Double {
          return sharedPrefrences.getString(Constants.LON, "0.0")?.toDouble() ?: 0.0
      }
      private fun getSelectedLoc(): String {
          return sharedPrefrences.getString(Constants.LOCATION, "Gps")
      }


      private fun getDayName(dayOfWeek: Int): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek)
        return sdf.format(calendar.time)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        bindingHome = null
    }
}
/*  @SuppressLint("SuspiciousIndentation")
   private fun observeWeatherResponce() {
       Log.d("WeatherHomeFragment", "Before observing weatherList")
       viewModel.weatherList.observe(viewLifecycleOwner) { response ->
           Log.d("WeatherHomeFragment", "Inside observer - weatherList size: ${response.size}")
           if (response.isNotEmpty()) {
                   val latestResponse = response.first()
                       hourlyWeatherAdapter.submitList(latestResponse.hourly)
                       dailyWeatherAdapter.submitList(latestResponse.daily)
                       initUi(latestResponse)
                       sharedPrefrences.addString(Constants.LAT, latestResponse.lat.toString())
                       sharedPrefrences.addString(Constants.LON, latestResponse.lon.toString())
               Log.d("WeatherHomeFtragment", "observeWeatherResponce succeeded" +  latestResponse.lat.toString()+latestResponse.lon.toString())

           }
                   }
       Log.d("WeatherHomeFragment", "After observing weatherList")
               }*/
