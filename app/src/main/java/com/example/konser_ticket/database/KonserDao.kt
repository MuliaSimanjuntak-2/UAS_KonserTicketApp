package com.example.konser_ticket.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface KonserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(konser: KonserRoom)
    @Delete
    fun delete(konser: KonserRoom)
    @Query("SELECT * from konser_table")
    fun getAllKonser(): LiveData<List<KonserRoom>>
    @Query("SELECT * FROM konser_table WHERE id = :id LIMIT 1")
    fun getKonserById(id: String): KonserRoom?
}