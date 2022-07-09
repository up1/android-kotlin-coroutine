package com.example.myapplication.ui.single

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.ApiUser
import com.example.myapplication.data.api.ApiService
import com.example.myapplication.util.Resource
import kotlinx.coroutines.launch

class SingleViewModel (
    private val apiService: ApiService
        ): ViewModel() {

    private val users = MutableLiveData<Resource<List<ApiUser>>>()

    init {
        fetchUsersFromApi()
    }

    private fun fetchUsersFromApi() {
        viewModelScope.launch {
            users.postValue(Resource.loading(null))
            try {
                val usersFromApi = apiService.getUsers()
                users.postValue(Resource.success(usersFromApi))
            } catch (e: Exception) {
                users.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getAllUsers(): LiveData<Resource<List<ApiUser>>> {
        return users
    }

}