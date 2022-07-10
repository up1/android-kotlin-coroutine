package com.example.myapplication.ui.single

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.ApiUser
import com.example.myapplication.data.api.ApiService
import com.example.myapplication.util.Resource
import kotlinx.coroutines.*

class SingleViewModel(
    private val apiService: ApiService
) : ViewModel() {

    private val users = MutableLiveData<Resource<List<ApiUser>>>()

    init {
        fetchUsersFromApiWithSupervisor()
    }

    private fun fetchUsersFromApi() {
        viewModelScope.launch {
            users.postValue(Resource.loading(null))
            try {
                // Catch error in coroutine
                coroutineScope {
                    val users01 = async { apiService.getUsers() }
                    val moreUsers = async { apiService.getMoreUsers() }
                    val errors = async { apiService.getUsersWithError() }

                    val all = mutableListOf<ApiUser>()
                    all.addAll(users01.await())
                    all.addAll(moreUsers.await())
                    all.addAll(errors.await())
                    users.postValue(Resource.success(all))
                }
            } catch (e: Exception) {
                users.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getAllUsers(): LiveData<Resource<List<ApiUser>>> {
        return users
    }


    // Working with CoroutineExceptionHandler
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        users.postValue(Resource.error(exception.toString(), null))
    }

    private fun fetchUsersFromApiWithHandler() {
        viewModelScope.launch(exceptionHandler) {
            users.postValue(Resource.loading(null))
            // Catch error in coroutine
            coroutineScope {
                val users01 = async { apiService.getUsers() }
                val moreUsers = async { apiService.getMoreUsers() }
                val errors = async { apiService.getUsersWithError() }

                val all = mutableListOf<ApiUser>()
                all.addAll(users01.await())
                all.addAll(moreUsers.await())
                all.addAll(errors.await())
                users.postValue(Resource.success(all))
            }
        }
    }

    // Working with supervisor
    private fun fetchUsersFromApiWithSupervisor() {
        viewModelScope.launch {
            users.postValue(Resource.loading(null))
            try {
                // Catch error in coroutine
                supervisorScope {
                    val usersDeferred = async { apiService.getUsers() }
                    val moreUsersDeferred = async { apiService.getMoreUsers() }
                    val errorsDeferred = async { apiService.getUsersWithError() }

                    val all = mutableListOf<ApiUser>()

                    val users01 = try {
                        usersDeferred.await()
                    } catch (e: Exception) {
                        emptyList()
                    }

                    val moreUsers = try {
                        moreUsersDeferred.await()
                    } catch (e: Exception) {
                        emptyList()
                    }

                    val errors = try {
                        errorsDeferred.await()
                    } catch (e: Exception) {
                        emptyList()
                    }

                    all.addAll(users01)
                    all.addAll(moreUsers)
                    all.addAll(errors)
                    users.postValue(Resource.success(all))
                }
            } catch (e: Exception) {
                users.postValue(Resource.error(e.toString(), null))
            }
        }
    }

}