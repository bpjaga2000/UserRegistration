package com.example.userregistration.api

import com.example.userregistration.bean.BaseBean
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(ApiConstants.api)
    suspend fun getData(): Response<BaseBean?>
}