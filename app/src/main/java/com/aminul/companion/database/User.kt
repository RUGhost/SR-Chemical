package com.aminul.companion.database

import androidx.room.*

@Entity(tableName = "MyDB")
data class User (
    @PrimaryKey
    val id: Int,
    var level: Double,
    var volume: Double
)