package com.example.cowork.activities.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: Int,
    val last_name: String,
    val first_name: String,
    val email: String,
    val birthday: String,
    val password: String,
    val client: String,
    val date_inscription: String):Parcelable