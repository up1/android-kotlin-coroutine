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
                val users01 = apiService.getUsers()
                val moreUsers = apiService.getMoreUsers()
                val all = mutableListOf<ApiUser>()
                all.addAll(users01)
                all.addAll(moreUsers)
                users.postValue(Resource.success(all))
            } catch (e: Exception) {
                users.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getAllUsers(): LiveData<Resource<List<ApiUser>>> {
        return users
    }

}