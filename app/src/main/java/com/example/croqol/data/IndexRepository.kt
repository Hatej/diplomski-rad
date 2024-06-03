package com.example.croqol.data

import com.example.croqol.network.ApiService
import com.example.croqol.network.IndexData
import com.example.croqol.network.IndexWeight
import retrofit2.Call
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

interface IndexRepository {
    fun postCityIndex(city: String, indexWeight: IndexWeight): Call<IndexData>
}

@Singleton
class NetworkIndexRepository @Inject constructor (
    private val apiService: ApiService
) : IndexRepository {
    override fun postCityIndex(
        city: String, indexWeight: IndexWeight
    ): Call<IndexData> = apiService.postCityIndex(city, indexWeight)
}