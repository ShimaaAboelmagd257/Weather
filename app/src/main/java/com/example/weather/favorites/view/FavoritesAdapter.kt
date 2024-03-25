package com.example.weather.favorites.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weathear.databinding.FavRowBinding
import com.example.weather.model.pojos.UserLocation
import com.example.weather.utility.helper.Constants

class FavoritesAdapter(
    var context: Context,
    var listner: FavClickListner
) :ListAdapter<UserLocation, FavoritesAdapter.myViewHolder>(FavDiffutil()) {


    class myViewHolder(private  val binding: FavRowBinding): RecyclerView.ViewHolder( binding.root) {
        fun bind(userLocation: UserLocation, listner: FavClickListner, context: Context) {
            binding.apply {
                tvLocation.text = Constants.getAddress(context, userLocation.lat, userLocation.lon)
                imageFavDelete.setOnClickListener { listner.onDeleteItemClicked(userLocation) }
                favCardView.setOnClickListener {
                    listner.onItemClicked(userLocation)
                }
            }
        }
        companion object {
            fun from(parent: ViewGroup): myViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavRowBinding.inflate(layoutInflater, parent, false)
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

    class FavDiffutil : DiffUtil.ItemCallback<UserLocation>(){
        override fun areItemsTheSame(oldItem: UserLocation, newItem: UserLocation): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UserLocation, newItem: UserLocation): Boolean {
            return oldItem == newItem
        }

    }
}