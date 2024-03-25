package com.example.weather.utility.networkconnection

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.os.Handler
import android.os.Looper

class NetworkStateManager(context: Context) : NetworkCallback() {
    private val networkStateListeners: MutableList<NetworkStateListner>
    private val handler: Handler

   /* companion object {
        val isNetworkAvailable: MutableLiveData<Boolean> = MutableLiveData()
    }*/

   /* init {
        isNetworkAvailable.postValue(isConnected())
    }*/

    private val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        ?: throw IllegalStateException("ConnectivityManager is null")

    init {
        networkStateListeners = ArrayList()
        handler = Handler(Looper.getMainLooper())
    }

    override fun onAvailable(network: Network) {
        handler.post {
            notifyNetworkAvailable()
        }
    }

    override fun onLost(network: Network) {
        val isNetworkAvailable = performNetworkOperations()
        if (!isNetworkAvailable) {
            handler.post {
                notifyNetworkUnavailable()
            }
        }
    }

    private fun performNetworkOperations(): Boolean {
        return try {
            // Your network operations here
            true
        } catch (e: Exception) {
            // Handle any exceptions or errors
            e.printStackTrace()
            return false
        }
    }

    private fun notifyNetworkAvailable() {
        for (listener in networkStateListeners) {
            listener.onNetworkAvailable()
        }
    }

    private fun notifyNetworkUnavailable() {
        for (listener in networkStateListeners) {
            listener.onNetworkUnavailable()
        }
    }
    fun isConnected(): Boolean {
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
            .isConnected
    }
   /* fun isNetworkAvailable(): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val activeNetwork =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

                when {
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    // Add more transports as needed

                    else -> false
                }
            } else {
                val networkInfo = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected == true
            }
        } catch (e: Exception) {
            // Handle any exceptions or errors
            e.printStackTrace()
            return false
        }
    }*/
}

