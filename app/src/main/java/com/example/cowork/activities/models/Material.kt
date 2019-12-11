package com.example.cowork.activities.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Material (
    val id: Int?,
    val type: String,
    val location: String?,
    var number: Int?
): Parcelable

data class MaterialResponse(val status: Int, val result: List<Material>)