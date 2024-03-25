package com.example.weather.home.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weathear.R
import com.example.weathear.databinding.HourlyWeatherRowBinding
import com.example.weather.model.pojos.HourlyForecast
import com.example.weather.utility.helper.Constants

class HourlyWeatherAdapter : ListAdapter<HourlyForecast, HourlyWeatherAdapter.myViewHolder>(HourlyDiffutil()) {


    class myViewHolder(private  val binding: HourlyWeatherRowBinding): RecyclerView.ViewHolder( binding.root) {
        fun bind(hourlyForecast: HourlyForecast) {
            binding.apply {
             hourlyTime.text = Constants.convertLongToTime(hourlyForecast.dt)
                val strFormat: String = binding.root.context.getString(
                    R.string.hourly,
                    hourlyForecast.temp?.toInt(),
                    Constants.getTemperatureUnit(binding.root.context)
                )
                hourlyTemp.text = strFormat
                Glide
                    .with(binding.root)
                    .load("https://openweathermap.org/img/wn/${hourlyForecast.weather?.get(0)?.icon ?: ""}.png?fbclid=IwAR2Nk0UQ5anrxUCLubc6bRZTqN65qD2TE2Rk0EvU6-609jRf_HuHPAnP-YE")
                    .into(hourlyIcon)
            }
        }
        companion object {
            fun from(parent: ViewGroup): HourlyWeatherAdapter.myViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HourlyWeatherRowBinding.inflate(layoutInflater, parent, false)
                return HourlyWeatherAdapter.myViewHolder(binding)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherAdapter.myViewHolder {
        return HourlyWeatherAdapter.myViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HourlyWeatherAdapter.myViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HourlyDiffutil : DiffUtil.ItemCallback<HourlyForecast>(){
        override fun areItemsTheSame(oldItem: HourlyForecast, newItem: HourlyForecast): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: HourlyForecast, newItem: HourlyForecast): Boolean {
            return oldItem == newItem
        }

    }
}