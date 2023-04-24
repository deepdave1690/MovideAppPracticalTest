package com.example.movideapppractical.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_cart")
data class
CartDetails(

    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    var movieId: String,

    @ColumnInfo(name = "total_tickets")
    var totalTickets : Int,

)
