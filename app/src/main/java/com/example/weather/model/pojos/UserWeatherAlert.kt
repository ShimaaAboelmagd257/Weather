package com.example.weather.model.pojos

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class UserWeatherAlert(
    var timeFrom: Long,
    var timeTo: Long,
    var endDate: Long,
    @PrimaryKey
    var title: String
) : Parcelable