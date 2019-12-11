package com.example.cowork.activities.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tickets (
    var id: Int?,
    var name: String,
    var date_creation: String?,
    var status: String?,
    var id_user: Int?,
    var user_name: String,
    var id_owner: Int?,
    var owner_name:String?,
    var type: String,
    var material_id: String,
    var description: String,
    var location: String,
    var open: String?,
    var resolved: String?,
    var late: String?
): Parcelable

data class TicketResponse(var status: Int, var result: List<Tickets>)

data class TicketCreatedResponse(var status: Int, var result: Tickets)

data class TicketUpdatedResponse(var status: Int, var result: Boolean)