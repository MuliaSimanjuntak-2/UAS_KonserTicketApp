package com.example.konser_ticket.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "konser_table")
data class KonserRoom(

    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "artis")
    val artis: String,
    @ColumnInfo(name = "location")
    val location: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "price")
    val price: String,
    @ColumnInfo(name = "description")
    val description: String, )

