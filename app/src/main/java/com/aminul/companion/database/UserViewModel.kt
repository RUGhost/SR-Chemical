package com.aminul.companion.database

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application):AndroidViewModel(application){
    private val _readAllData = MutableLiveData<List<User>>()
    var readAllData: LiveData<List<User>> = _readAllData
    private val repository: UserRepository
    init {
        val userDao = UserDatabase.getInstance(application).userDao()
        repository = UserRepository(userDao)
    }

//    fun getUser(userId: Double){
//        viewModelScope.launch(Dispatchers.IO) {
//            _readAllData.postValue(repository.getUserData(userId))
//        }
//    }

    fun getUserList(userId: List<Double>){
        viewModelScope.launch(Dispatchers.IO) {
            _readAllData.postValue(repository.getUserDataList(userId))
        }
    }
//    fun addUser(item: List<User>){
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.addUser(item)
//        }
//    }
}
class UserViewModelFactory(private val application: Application):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)){
            return UserViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}