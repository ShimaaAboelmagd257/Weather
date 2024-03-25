package com.example.weather.home.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weathear.R
import com.example.weathear.databinding.WeeklyWeatherRowBinding
import com.example.weather.model.pojos.DailyForecast
import com.example.weather.utility.helper.Constants

class DailyWeatherAdapter : ListAdapter<DailyForecast, DailyWeatherAdapter.myViewHolder>(DailyDiffutil()) {


    class myViewHolder(private  val binding: WeeklyWeatherRowBinding):RecyclerView.ViewHolder( binding.root) {
        fun bind(dailyForecast: DailyForecast) {
            binding.apply {
                dailyName.text = Constants.convertLongToDayName(dailyForecast.dt)
                dailyDesc.text = dailyForecast.weather.get(0).description
                val strFormat: String = binding.root.context.getString(
                    R.string.max_min_unit, dailyForecast.temp?.max?.toInt(),
                    dailyForecast.temp?.min?.toInt(),
                    Constants.getTemperatureUnit(binding.root.context)
                )
                maxMinUnit.text = strFormat
                Glide
                    .with(binding.root)
                    .load("https://openweathermap.org/img/wn/${dailyForecast.weather?.get(0)?.icon ?: ""}.png?fbclid=IwAR2Nk0UQ5anrxUCLubc6bRZTqN65qD2TE2Rk0EvU6-609jRf_HuHPAnP-YE")
                    .into(dailyIcon)
            }
        }
            companion object {
                fun from(parent: ViewGroup): myViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding = WeeklyWeatherRowBinding.inflate(layoutInflater, parent, false)
                    return myViewHolder(binding)
                }
            }
        }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        return myViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class DailyDiffutil : DiffUtil.ItemCallback<DailyForecast>(){
        override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
            return oldItem == newItem
        }

    }
}