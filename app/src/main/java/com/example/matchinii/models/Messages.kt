package com.example.matchinii.models
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class Messages(
    val UserM1 : String ,
    val UserM2: String ,
    val messageUser1 :ArrayList<Map<String , String>> ,
    val RommeName : String
)
