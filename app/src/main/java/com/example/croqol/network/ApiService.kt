package com.example.croqol.network

import retrofit2.http.GET

interface ApiService {

    @GET("fact")
    suspend fun getCatFact(): CatFact

}