package com.example.croqol.ui.overview

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.croqol.data.CatFactRepository
import com.example.croqol.network.CatFact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.IllegalStateException
import javax.inject.Inject

sealed interface CatFactUiState {
    data class Success(val catFact: CatFact) : CatFactUiState
    data object Nothing : CatFactUiState
    data object Loading : CatFactUiState
    data object Error : CatFactUiState
}

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val catFactRepository: CatFactRepository
) : ViewModel() {

    private val _overviewUiState = mutableStateOf(OverviewUiState())
    val overviewUiState = _overviewUiState

    val availableCities = listOf("Zagreb", "Varaždin", "Čakovec", "Split", "Rijeka")

    fun updateSearchCity(input: String) {
        _overviewUiState.value = overviewUiState.value.copy(searchCity = input)
    }

    fun updateSlider(slider: CroQoLSliders, input: Float) {
        _overviewUiState.value = when (slider) {
            CroQoLSliders.Water -> overviewUiState.value.copy(waterQualitySlider = input)
            CroQoLSliders.Air -> overviewUiState.value.copy(airQualitySlider = input)
            CroQoLSliders.CostOfLiving -> overviewUiState.value.copy(costOfLivingSlider = input)
            CroQoLSliders.Education -> overviewUiState.value.copy(educationSlider = input)
            CroQoLSliders.Healthcare -> overviewUiState.value.copy(healthcareSlider = input)
            CroQoLSliders.Safety -> overviewUiState.value.copy(safetySlider = input)
        }
    }

    fun updateExpandedSlider(slider: Int) {
        val isExpandedMap = overviewUiState.value.isExpandedMap.toMutableMap()
        isExpandedMap[slider] = !(isExpandedMap[slider] ?: false)
        _overviewUiState.value = overviewUiState.value.copy(
            isExpandedMap = isExpandedMap
        )
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getCatFact() {
        viewModelScope.launch {
            _overviewUiState.value = overviewUiState.value.copy(catFactUiState = CatFactUiState.Loading)
            _overviewUiState.value = overviewUiState.value.copy(catFactUiState = try {
                    CatFactUiState.Success(catFactRepository.getCatFact())
                } catch (e: IOException) {
                    CatFactUiState.Error
                } catch (e: HttpException) {
                    CatFactUiState.Error
                }
            )
        }
    }
}