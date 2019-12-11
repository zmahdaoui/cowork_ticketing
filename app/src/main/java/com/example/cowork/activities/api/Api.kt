package com.example.cowork.activities.api

import com.example.cowork.activities.models.*
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @POST("android/login")
    fun Login(@Body  data : LoginRequest):Call<LoginResponse>

    @GET("tech/mat/{location}")
    fun getTechMat(@Path("location") location: String):Call<MaterialResponse>

    @GET("tickets/new")
    fun getNewTicket():Observable<TicketResponse>

    @GET("tickets/client/new/{id}")
    fun getClientNewTicket(@Path("id") id: Int):Observable<TicketResponse>

    @GET("tickets/client/late/{id}")
    fun getClientLateTicket(@Path("id") id: Int):Observable<TicketResponse>

    @GET("tickets/pro/late/{id}")
    fun getProLateTicket(@Path("id") id: Int):Observable<TicketResponse>

    @GET("tickets/client/inprogress/{id}")
    fun getClientInProgressTicket(@Path("id") id: Int):Observable<TicketResponse>

    @GET("tickets/pro/inprogress/{id}")
    fun getProInProgressTicket(@Path("id") id: Int):Observable<TicketResponse>

    @GET("tickets/client/waiting/{id}")
    fun getClientAwaitingTicket(@Path("id") id: Int):Observable<TicketResponse>

    @GET("tickets/pro/waiting/{id}")
    fun getProAwaitingTicket(@Path("id") id: Int):Observable<TicketResponse>

    @GET("tickets/client/closed/{id}")
    fun getClientClosedTicket(@Path("id") id: Int):Observable<TicketResponse>

    @GET("tickets/pro/closed/{id}")
    fun getProClosedTicket(@Path("id") id: Int):Observable<TicketResponse>

    @GET("tickets/client/resolved/{id}")
    fun getClientResolvedTicket(@Path("id") id: Int):Observable<TicketResponse>

    @GET("tickets/pro/resolved/{id}")
    fun getProResolvedTicket(@Path("id") id: Int):Observable<TicketResponse>

    @POST("ticket/create")
    fun createTicket(@Body  data : Tickets):Call<TicketCreatedResponse>

    @PUT("tickets/update")
    fun updateTicket(@Body  data : Tickets):Call<TicketUpdatedResponse>
}