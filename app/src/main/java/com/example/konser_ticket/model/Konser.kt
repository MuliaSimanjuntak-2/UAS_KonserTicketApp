package com.example.konser_ticket.model

import com.google.gson.annotations.SerializedName

data class Konser(
    @SerializedName("_id")
    val id: String?  = null,
    @SerializedName("title")
    val title: String,
    @SerializedName("artis")
    val artis: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("description")
    val description: String,

)
