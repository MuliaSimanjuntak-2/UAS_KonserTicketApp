package com.example.konser_ticket.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    val id: String? ,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("role")
    val role: String,
)
