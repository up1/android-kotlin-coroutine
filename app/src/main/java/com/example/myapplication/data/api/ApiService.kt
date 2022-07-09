package com.example.myapplication.data.api

import com.example.myapplication.data.model.ApiUser
import retrofit2.http.GET

interface ApiService {

    @GET("users")
    suspend fun getUsers(): List<ApiUser>

    @GET("more-users")
    suspend fun getMoreUsers(): List<ApiUser>

    @GET("error")
    suspend fun getUsersWithError(): List<ApiUser>

}

class ApiWithRetrofit(private val apiService: ApiService) : ApiService {
    override suspend fun getUsers() = apiService.getUsers()
    override suspend fun getMoreUsers() = apiService.getMoreUsers()
    override suspend fun getUsersWithError() = apiService.getUsersWithError()
}
