package com.example.weather.favorites.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weathear.databinding.FragmentFavoritesBinding
import com.example.weather.favorites.viewModel.FavoritesViewModel
import com.example.weather.favorites.viewModel.FavoritesViewModelFactory
import com.example.weather.model.localDatabase.ConcreteLocalSource
import com.example.weather.model.pojos.UserLocation
import com.example.weather.model.remoteDatabase.WeatherClient
import com.example.weather.model.repository.Repository
import com.example.weather.model.sharedPrefrence.SharedPrefrences
import com.example.weather.utility.helper.Constants
import com.example.weather.utility.helper.Constants.setLocale
import com.example.weather.utility.locationConnection.LocationViewModel
import com.example.weather.utility.networkconnection.NetworkStateManager
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() , FavClickListner  {


    lateinit var viewModel: FavoritesViewModel
    lateinit var viewModelFactory: FavoritesViewModelFactory
    lateinit var favoritesAdapter: FavoritesAdapter
    lateinit var binding: FragmentFavoritesBinding
    lateinit var networkStateListner: NetworkStateManager
    val sharedPrefrences: SharedPrefrences by lazy {
       SharedPrefrences(requireContext())
   }
    lateinit var  locationViewmodel: LocationViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      requireContext().setLocale(getLanguageLocale())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater,container,false)
         return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = FavoritesViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(),
                ConcreteLocalSource.getInstance(requireContext())
            )
        )
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(FavoritesViewModel::class.java)
        networkStateListner = NetworkStateManager(requireContext())

        locationViewmodel = ViewModelProvider(requireActivity()).get(LocationViewModel::class.java)
       // sharedPrefrences = SharedPrefrences(requireContext())
        favoritesAdapter = FavoritesAdapter(requireContext(), this)

        initFavRecycler()
        addToFav()
        handleBackBtn()
        getAllFav()

        }
       private fun getAllFav(){
           lifecycleScope.launch {
               viewModel.favLocationsLiveData.collect { favoriteLocations ->
                   favoritesAdapter.submitList(favoriteLocations) // Submit the mutable list to the adapter
                   Log.d("WeatherFav", "submitList size " + favoriteLocations.size)
               }
           }
       }

          private fun handleBackBtn() {
              binding.backButton.setOnClickListener {
                  val action = FavoritesFragmentDirections.actionFragmentFavToFragmentHome()
                  Navigation.findNavController(requireView()).navigate(action)
              }
          }


        private fun addToFav() {
            binding.addFavLocation.setOnClickListener {
                if (networkStateListner.isConnected()) {
                    Log.d("WeathearFav", "Button clicked")
                    val action = FavoritesFragmentDirections.actionFragmentFavToMapFragment(true)
                    Navigation.findNavController(requireView()).navigate(action)
                } else
                    Toast.makeText(
                        requireContext(),
                        "Please check your Network connection",
                        Toast.LENGTH_SHORT
                    ).show()

            }
        }



        private fun initFavRecycler() {
          binding.favrecyclerView.apply {
              adapter = favoritesAdapter
              setHasFixedSize(true)
              layoutManager = LinearLayoutManager(requireContext())
          }
        }

        override fun onItemClicked(userLocation: UserLocation) {
            Log.d("WeathearFav", "onItemClicked:")
            if (networkStateListner.isConnected()) {
                sharedPrefrences.addString(Constants.LOCATION, "Fav")
                viewModel.displayavLocation(userLocation.lat, userLocation.lon)
                val action = FavoritesFragmentDirections.actionFragmentFavToFragmentHome()
                findNavController().navigate(action)
            } else
                Toast.makeText(requireContext(), "Please check your Network connection", Toast.LENGTH_SHORT).show()

        }

        override fun onDeleteItemClicked(weatherResponse: UserLocation) {
              viewModel.deleteFaveWeather(weatherResponse)
              favoritesAdapter.notifyDataSetChanged()
          }

        private fun getLanguageLocale(): String {
          return sharedPrefrences.getString(Constants.LANGUAGE, "en")
        }


        }
/* lifecycleScope.launch {
      viewModel.favoriteLocations.collect{ favoriteLocations ->
          favoriteLocations.toMutableList()
          favoritesAdapter.submitList(favoriteLocations)
          Log.d("WeatherFav", "submitList size " + favoriteLocations.size)

      }
*/

/*  locationViewModel.favLocationLiveData.observe(viewLifecycleOwner) { favoriteLocations ->
favoritesAdapter.submitList(favoriteLocations)
Log.d("WeatharFav", "submitList size " + favoriteLocations.size)

}
*/
/* lifecycleScope.launch {
viewModel.favLocationsLiveData.collect { favLocations ->
  Log.d("WeatherFavFragment", "List Size: ${favLocations.size}")
favoritesAdapter.submitList(favLocations)
  favoritesAdapter.notifyDataSetChanged()
}
}*/

/*   viewModel.favLocationsLiveData.observe(viewLifecycleOwner) { favLocations ->
// Update your RecyclerView adapter with the list of favorite locations
favoritesAdapter.submitList(favLocations)
Log.d("WeathearFav", "submitList size " + favLocations.size)

}*/
/* repo =  Repository.getInstance(
    WeatherClient.getInstance(), ConcreteLocalSource.getInstance(requireContext()))
val locationViewModel: LocationViewModel by viewModels {
    LocationFactory(requireActivity().application, repo)
}*/
/*  private fun getAllFavstWeather() {
        locationViewmodel.favLocationLiveData.observe(viewLifecycleOwner) { userLocation ->
            userLocation?.let {
                if (viewModel.isLocationChanged(it.last())) {
                   // viewModel.insertFav(it.last())
                    favoritesAdapter.submitList(it)
                } else {
                    Log.d("WeatherFav", "no Location add , skipping insertion")
                }
            }
        }
      }*/