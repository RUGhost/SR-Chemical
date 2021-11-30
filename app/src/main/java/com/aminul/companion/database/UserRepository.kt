package com.aminul.companion.database

class UserRepository(private val userDao: UserDao) {
//        suspend fun addUser(item: List<User>){
//        userDao.insertUser(item)
//    }

//    fun getUserData(userId: Double): List<User> {
//        return userDao.getUser(userId)
//    }

    fun getUserDataList(userId: List<Double>): List<User> {
        return userDao.getUserList(userId)
    }
}