package com.example.croqol.data

import com.example.croqol.network.ApiService
import com.example.croqol.network.CatFact
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

interface CatFactRepository {
    suspend fun getCatFact(): CatFact
}

@Singleton
class NetworkCatFactRepository @Inject constructor (
    private val apiService: ApiService
) : CatFactRepository {
    override suspend fun getCatFact() = apiService.getCatFact()
}