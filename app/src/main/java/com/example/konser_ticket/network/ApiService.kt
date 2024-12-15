package com.example.konser_ticket.network

import com.example.konser_ticket.model.Konser
import com.example.konser_ticket.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("User")
    fun getAllUsers(): Call<List<User>>
    @POST("User")
    fun createUser(@Body user: User): Call<User>
    @POST("Konser")
    fun createKonser(@Body konser : Konser): Call<Konser>
    @GET("Konser")
    fun getAllKonser(): Call<List<Konser>>
    @POST("Konser/{id}")
    fun updateKonser(@Path("id") KonserId: String, @Body konser: Konser): Call<Konser>
    @DELETE("Konser/{id}")
    fun deleteKonser(@Path("id") KonserId: String): Call<Konser>


}