package com.example.croqol.network

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class IndexWeight(
    @SerializedName("ClimateIndex")
    val climateIndexWeight: Double,
    @SerializedName("EnvironmentIndex")
    val environmentIndexWeight: Double,
    @SerializedName("TrafficIndex")
    val trafficIndexWeight: Double,
    @SerializedName("FinancialIndex")
    val financialIndexWeight: Double,
    @SerializedName("SafetyIndex")
    val safetyIndexWeight: Double,
    @SerializedName("HealthCareIndex")
    val healthcareIndexWeight: Double
)
