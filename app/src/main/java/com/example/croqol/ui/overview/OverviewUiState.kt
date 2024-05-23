package com.example.croqol.ui.overview

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector
import javax.annotation.concurrent.Immutable

@Immutable
data class OverviewUiState(
    val searchCity: String = "",
    val catFactUiState: CatFactUiState = CatFactUiState.Nothing,
    val safetySlider: Float = 100f,
    val costOfLivingSlider: Float = 100f,
    val waterQualitySlider: Float = 100f,
    val airQualitySlider: Float = 100f,
    val educationSlider: Float = 100f,
    val healthcareSlider: Float = 100f,
    val isExpandedMap: MutableMap<Int, Boolean> = List(allSliders.size) { index: Int -> index to false }.toMap().toMutableMap()
) {
    fun getSliderValue(croQoLSliders: CroQoLSliders) = when (croQoLSliders) {
        CroQoLSliders.Air -> airQualitySlider
        CroQoLSliders.CostOfLiving -> costOfLivingSlider
        CroQoLSliders.Education -> educationSlider
        CroQoLSliders.Healthcare -> healthcareSlider
        CroQoLSliders.Safety -> safetySlider
        CroQoLSliders.Water -> waterQualitySlider
    }
}

sealed interface CroQoLSliders {
    data object Water : CroQoLSliders
    data object Air : CroQoLSliders
    data object Safety : CroQoLSliders
    data object CostOfLiving : CroQoLSliders
    data object Education : CroQoLSliders
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
        label = "Kvaliteta Zraka",
        croQoLSlider = CroQoLSliders.Air,
        icon = Icons.Default.Air,
        info = "Kvaliteta Zraka Info"
    ),
    CroQoLSlider(
        label = "Kvaliteta Vode",
        croQoLSlider = CroQoLSliders.Water,
        icon = Icons.Default.WaterDrop,
        info = "Kvaliteta Vode Info"
    ),
    CroQoLSlider(
        label = "Sigurnost",
        croQoLSlider = CroQoLSliders.Safety,
        icon = Icons.Default.Security,
        info = "Sigurnost Info"
    ),
    CroQoLSlider(
        label = "Cijena Stanovanja",
        croQoLSlider = CroQoLSliders.CostOfLiving,
        icon = Icons.Default.Payments,
        info = "Cijena Stanovanja Info"
    ),
    CroQoLSlider(
        label = "Obrazovanje",
        croQoLSlider = CroQoLSliders.Education,
        icon = Icons.Default.School,
        info = "Obrazovanje Info"
    ),
    CroQoLSlider(
        label = "Zdravstvo",
        croQoLSlider = CroQoLSliders.Healthcare,
        icon = Icons.Default.HealthAndSafety,
        info = "Zdravstvo Info"
    )
)