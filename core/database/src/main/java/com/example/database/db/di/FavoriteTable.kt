package com.example.database.db.di

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("favorite_table")
data class FavoriteTable(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val username:String
)