package com.example.croqol.ui.overview

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.OtherHouses
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.example.croqol.network.IndexData
import com.example.croqol.network.IndexWeight
import com.google.android.gms.maps.model.LatLng
import javax.annotation.concurrent.Immutable

@Immutable
data class OverviewUiState(
    val searchCity: String = "",
    val indexDataUiState: IndexDataUiState = IndexDataUiState.Nothing,
    val safetySlider: Float = 100f,
    val financialSlider: Float = 100f,
    val trafficSlider: Float = 100f,
    val climateSlider: Float = 100f,
    val environmentSlider: Float = 100f,
    val healthcareSlider: Float = 100f,
    val isExpandedMap: MutableMap<Int, Boolean> = List(allSliders.size) { index: Int -> index to false }.toMap().toMutableMap(),
    val currentIndexedCity: String = "",
    val isMarkerSet: Boolean = false,
    val markerLatLng: LatLng = LatLng(44.927, 16.383)
) {
    fun getSliderValue(croQoLSliders: CroQoLSliders) = when (croQoLSliders) {
        CroQoLSliders.Climate -> climateSlider
        CroQoLSliders.Financial -> financialSlider
        CroQoLSliders.Traffic -> trafficSlider
        CroQoLSliders.Healthcare -> healthcareSlider
        CroQoLSliders.Safety -> safetySlider
        CroQoLSliders.Environment -> environmentSlider
    }

    fun getIndexWeight() = IndexWeight(
        climateIndexWeight = climateSlider.toDouble(),
        financialIndexWeight = financialSlider.toDouble(),
        trafficIndexWeight = trafficSlider.toDouble(),
        environmentIndexWeight = environmentSlider.toDouble(),
        safetyIndexWeight = safetySlider.toDouble(),
        healthcareIndexWeight = healthcareSlider.toDouble()
    )

}

sealed interface CroQoLSliders {
    data object Climate : CroQoLSliders
    data object Environment : CroQoLSliders
    data object Safety : CroQoLSliders
    data object Financial : CroQoLSliders
    data object Traffic : CroQoLSliders
    data object Healthcare : CroQoLSliders
}

data class CroQoLSlider(
    val label: String,
    val croQoLSlider: CroQoLSliders,
    val icon: ImageVector,
    val info: String
)

val allSliders = mutableListOf(
    CroQoLSlider(
        label = "Klima",
        croQoLSlider = CroQoLSliders.Climate,
        icon = Icons.Default.Air,
        info = LoremIpsum(50).values.joinToString(separator = " ")
    ),
    CroQoLSlider(
        label = "Okruženje",
        croQoLSlider = CroQoLSliders.Environment,
        icon = Icons.Default.OtherHouses,
        info = LoremIpsum(100).values.joinToString(separator = " ")
    ),
    CroQoLSlider(
        label = "Sigurnost",
        croQoLSlider = CroQoLSliders.Safety,
        icon = Icons.Default.Security,
        info = LoremIpsum(30).values.joinToString(separator = " ")
    ),
    CroQoLSlider(
        label = "Cijena Stanovanja",
        croQoLSlider = CroQoLSliders.Financial,
        icon = Icons.Default.Payments,
        info = LoremIpsum(20).values.joinToString(separator = " ")
    ),
    CroQoLSlider(
        label = "Promet",
        croQoLSlider = CroQoLSliders.Traffic,
        icon = Icons.Default.DirectionsCar,
        info = LoremIpsum(50).values.joinToString(separator = " ")
    ),
    CroQoLSlider(
        label = "Zdravstvo",
        croQoLSlider = CroQoLSliders.Healthcare,
        icon = Icons.Default.HealthAndSafety,
        info = LoremIpsum(50).values.joinToString(separator = " ")
    )
)