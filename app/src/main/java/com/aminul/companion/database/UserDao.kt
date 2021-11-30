package com.aminul.companion.database

import androidx.room.*



@Dao
interface UserDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertUser(item: List<User>)

//    @Query("SELECT * FROM MyDB WHERE level IN (:userId)")
//    fun getUser(userId: Double): List<User>

    @Query("SELECT * FROM MyDB WHERE level IN (:userId)")
    fun getUserList(userId: List<Double>): List<User>
}