package com.example.weather.alerts.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weathear.databinding.AlertRowBinding
import com.example.weather.model.pojos.UserWeatherAlert
import com.example.weather.utility.helper.Constants
import java.text.SimpleDateFormat
import java.util.*

class AlertsAdapter(var context: Context, var listner: AlertClickListner)
    :ListAdapter<UserWeatherAlert, AlertsAdapter.myViewHolder>(AlertDiffutil()) {


    class myViewHolder(private  val binding: AlertRowBinding): RecyclerView.ViewHolder( binding.root) {
        fun bind(userWeatherAlert: UserWeatherAlert, listner: AlertClickListner, context: Context) {
            binding.apply {
              //  dateFrom.text = Constants.convertLongToDayDateAlert(userWeatherAlert.startDate)
               // dateTo.text = Constants.convertLongToDayDateAlert(userWeatherAlert.endDate)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val currentDate = dateFormat.format(Date()) // Get the current date in "dd/MM/yyyy" format
                dateTo.text = currentDate
                //dateTo.text = Calendar.DATE.toString()
                hourFrom.text = Constants.convertLongToTimePicker(userWeatherAlert.timeFrom)
                hourTo.text = Constants.convertLongToTimePicker(userWeatherAlert.timeTo)

                ivDelete.setOnClickListener {
                    listner.onDeleteItemClicked(userWeatherAlert)
                }
            }
        }



        companion object {
            fun from(parent: ViewGroup): myViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AlertRowBinding.inflate(layoutInflater, parent, false)
                return myViewHolder(binding)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        return myViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        holder.bind(getItem(position),listner,context)
    }

    class AlertDiffutil : DiffUtil.ItemCallback<UserWeatherAlert>(){
        override fun areItemsTheSame(oldItem: UserWeatherAlert, newItem: UserWeatherAlert): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UserWeatherAlert, newItem: UserWeatherAlert): Boolean {
            return oldItem == newItem
        }

    }
}