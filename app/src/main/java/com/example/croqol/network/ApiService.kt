package com.example.croqol.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("city/{city}")
    fun postCityIndex(
        @Path("city") city: String,
        @Body indexWeight: IndexWeight
    ): Call<IndexData>

}