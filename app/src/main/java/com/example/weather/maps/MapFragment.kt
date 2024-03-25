package com.example.weather.maps

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weathear.R
import com.example.weathear.databinding.FragmentMapBinding
import com.example.weather.favorites.viewModel.FavoritesViewModel
import com.example.weather.favorites.viewModel.FavoritesViewModelFactory
import com.example.weather.model.localDatabase.ConcreteLocalSource
import com.example.weather.model.remoteDatabase.WeatherClient
import com.example.weather.model.repository.Repository
import com.example.weather.model.sharedPrefrence.SharedPrefrences
import com.example.weather.utility.helper.Constants
import com.example.weather.utility.locationConnection.LocationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*


class MapFragment : Fragment()  {


    private var address: String = ""
    lateinit var binding: FragmentMapBinding
    lateinit var sharedPrefrences: SharedPrefrences
    lateinit var supportMapFragment: SupportMapFragment
    lateinit var geocoder: Geocoder
    lateinit var viewModel: FavoritesViewModel
    lateinit var locationViewmodel: LocationViewModel
    lateinit var viewModelFactory: FavoritesViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPrefrences = SharedPrefrences(requireContext())
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        supportMapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        viewModelFactory = FavoritesViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())))
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(FavoritesViewModel::class.java)
        locationViewmodel = ViewModelProvider(requireActivity()).get(LocationViewModel::class.java)
        supportMapFragment.getMapAsync { myGoogleMap ->
            myGoogleMap.setOnMapClickListener { location ->
                handleMapClick(location, myGoogleMap)
            }
        }
    }


    private fun handleMapClick(location: LatLng, googleMap: GoogleMap) {
        clearMap(googleMap)
        addMarker(googleMap, location)
        moveCamera(googleMap, location)
        handleGeocoding(location)
        handleDoneClick(location.latitude,location.longitude)
    }

    private fun clearMap(googleMap: GoogleMap) {
        googleMap.clear()
    }

    private fun addMarker(googleMap: GoogleMap, location: LatLng) {
        googleMap.addMarker(MarkerOptions().position(location))
    }

    private fun moveCamera(googleMap: GoogleMap, location: LatLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10.0f))
    }


    private fun handleGeocoding(location: LatLng) {
        val lat = location.latitude
        val lon = location.longitude
        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.d("WeathearMapFragment", "Clicked Location: Lat=$lat, Lon=$lon")
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                val city = addresses?.getOrNull(0)?.locality
                val country = addresses?.getOrNull(0)?.countryName
                address = "$country/$city"
            } catch (e: IOException) {
                Log.e("WeatherMapFragment", "Geocoding error: ${e.message}")
            }
        }
    }

    private fun handleDoneClick(lat: Double, lon: Double) {
        binding.doneButton.setOnClickListener {
            val args = arguments?.let {MapFragmentArgs.fromBundle(it) }
            val isFromFav = args?.isFav ?: false
            if (isFromFav) {
                setUserFavLocation(lat,lon)
               // saveLocationToFavorites(lat,lon)
                val action =
                   MapFragmentDirections.actionMapFragmentToFragmentFav()
                Navigation.findNavController(requireView()).navigate(action)
            }
            else {
                setUserLocation(lat, lon)
                val action =
                    MapFragmentDirections.actionMapFragmentToFragmentHome()
                Navigation.findNavController(requireView()).navigate(action)
            }
        }
    }
    private fun setUserLocation(lat: Double, lon: Double) {
       locationViewmodel.updateDesiredLocation(lat,lon)
        Log.d("WeatherMapFragment", "setUserLocation to maps" + lat + lon)

    }
    private fun setUserFavLocation(lat: Double, lon: Double) {

        viewModel.addFavoriteLocation(lat,lon)
      //  locationViewModel.addFavoriteLocation(lat, lon)
        Log.d("WeatherMapFragment", "setUserFavLocation to maps" + lat + lon)

    }

    private fun saveLocationToFavorites(lat: Double, lon: Double) {
        sharedPrefrences.addFloat(Constants.MAP_LATH, lat.toFloat())
        sharedPrefrences.addFloat(Constants.MAP_LONH, lon.toFloat())
    }
}

/*private fun observeUserFavLocation(){
        locationViewModel.favLocationLiveData.observe(viewLifecycleOwner) { location ->
            setUserFavLocation(location.lat, location.lon)
            Log.d("WeathearMapFragment", "observeUserCurrentLocation lon + lot " + location.lat+ location.lon)


        }
    }

      private fun observeUserCurrentLocation() {
          locationViewModel.desiredLocationLiveData.observe(viewLifecycleOwner) { location ->
              setUserLocation(location.lat, location.lon)
              Log.d("WeathearMapFragment", "observeUserCurrentLocation lon + lot " + location.lat+ location.lon)

          }
      }*/
/*
    private fun showAddLocationConfirmationDialog(lat: Double, lon: Double) {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.apply {
            setMessage("Are you sure you want to add $address to favorites?")
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                saveLocationToFavorites(lat, lon)
            }
            setNegativeButton(getString(R.string.cancel)) { _, _ -> }.create().show()
        }
        Log.d("WeatherMapFragment", "showAddLocationConfirmationDialog" + lat + lon)
    }
*/

/* private fun saveLocationToFavorites(lat: Double, lon: Double) {
     sharedPrefrences.addFloat(Constants.MAP_LATH , lat.toFloat())
     sharedPrefrences.addFloat(Constants.MAP_LONH , lon.toFloat())
   // findNavController().navigate(MapFragmentDirections.actionMapFragmentToFragmentFav())
     Log.d("WeatherMapFragment", "saveLocationToFavorites" + lat + lon)

 }
 private fun setupMapClickListener() {
     myGoogleMap.setOnMapClickListener { location ->
         handleMapClick(location, myGoogleMap)
     }
 }*/
/* override fun onMapReady( map: GoogleMap) {
     myGoogleMap = map
     val args = arguments?.let { MapFragmentArgs.fromBundle(it) }
     val isFromFav = args?.isFav ?: false
     if (isFromFav) {
         observeUserFavLocation()
     }
     else {
         observeUserCurrentLocation()
     }
     Log.d("WeatherMapFragment", "onMapReady")
 }*/
/*  private fun updateSelectedLocation(location: LatLng) {
        selectedLocation = Location("").apply {
            longitude = location.longitude
            latitude = location.latitude
            Log.d("WeathearMapFragment", "handleMapClick lon + lot " + longitude + latitude)
        }

      *//*  if (!isFromFav) {
            // Update the ViewModel with the desired location only when not from favorites
            locationViewModel.updateDesiredLocation(location.latitude, location.longitude)
        } else{
            locationViewModel.updateFavLocation(location.latitude,location.longitude)
        }*//*
    }
*/
/*private fun observeLocation() {
        val args = arguments?.let { MapFragmentArgs.fromBundle(it) }
        val isFromFav = args?.isFav ?: false

        if (isFromFav) {
            observeUserFavLocation()
        } else {
            observeUserCurrentLocation()
        }
    }*/
/*   locationViewModel.updateFavLocation(lat,lon)
       Log.d("WeathearMapFragment", "setUserFavLocation to favs" + lat + lon)
       locationViewModel.insertFavLocation(lat,lon)
       Log.d("WeathearMapFragment", "insertFavLocation to favs ROOM" + lat + lon)
*/
//  lateinit var locationViewModel: LocationViewModel
// lateinit var locationFactory: LocationFactory
/*lateinit var  repo: RepoInterface
private val locationViewModel: LocationViewModel by viewModels { LocationFactory(requireActivity().application, repo) }
*/
