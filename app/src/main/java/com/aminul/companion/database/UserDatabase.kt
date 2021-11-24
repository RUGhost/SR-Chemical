package com.aminul.companion.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object{
        @Volatile
        private var INSTANCE:UserDatabase? = null
        fun getInstance(context: Context): UserDatabase{
            val userInstance = INSTANCE
            if (userInstance != null){
                return userInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "UserDB"
                ).createFromAsset("database/MyDB.db").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}