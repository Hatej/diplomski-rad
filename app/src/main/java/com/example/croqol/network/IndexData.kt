package com.example.croqol.network

data class IndexData(
    val safetyIndex: Double = Math.random() * 100,
    val financialIndex: Double = Math.random() * 100,
    val climateIndex: Double = Math.random() * 100,
    val environmentIndex: Double = Math.random() * 100,
    val trafficIndex: Double = Math.random() * 100,
    val healthcareIndex: Double = Math.random() * 100,
) {
    fun overallIndex() = ((
            safetyIndex + financialIndex + climateIndex + environmentIndex + trafficIndex + healthcareIndex
            ) / 6.0)

    fun toDataMap() = mutableMapOf(
        "Klima" to climateIndex,
        "Okruženje" to environmentIndex,
        "Sigurnost" to safetyIndex,
        "Cijena" to financialIndex,
        "Promet" to trafficIndex,
        "Zdravstvo" to healthcareIndex
    )
}