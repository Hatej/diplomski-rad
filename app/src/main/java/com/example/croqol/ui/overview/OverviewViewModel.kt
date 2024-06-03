package com.example.croqol.ui.overview

import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.croqol.data.IndexRepository
import com.example.croqol.network.CatFact
import com.example.croqol.network.IndexData
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

sealed interface IndexDataUiState {
    data class Success(val indexData: IndexData) : IndexDataUiState
    data object Nothing : IndexDataUiState
    data object Loading : IndexDataUiState
    data class Error(val error: String) : IndexDataUiState
}

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val indexRepository: IndexRepository
) : ViewModel() {

    private val _overviewUiState = mutableStateOf(OverviewUiState())
    val overviewUiState = _overviewUiState
    var isFirstTimeSearch by mutableStateOf(true)

    fun updateSearchCity(input: String) {
        _overviewUiState.value = overviewUiState.value.copy(searchCity = input)
    }

    fun updateIsFirstTimeSearch(input: Boolean) {
        isFirstTimeSearch = input
    }

    fun updateSlider(slider: CroQoLSliders, input: Float) {
        _overviewUiState.value = when (slider) {
            CroQoLSliders.Climate -> overviewUiState.value.copy(climateSlider = input)
            CroQoLSliders.Environment -> overviewUiState.value.copy(environmentSlider = input)
            CroQoLSliders.Financial -> overviewUiState.value.copy(financialSlider = input)
            CroQoLSliders.Traffic -> overviewUiState.value.copy(trafficSlider = input)
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getCityIndices(context: Context, scrollTo: () -> Unit) {

        _overviewUiState.value = overviewUiState.value.copy(
            currentIndexedCity = overviewUiState.value.searchCity
        )

        viewModelScope.launch {
            _overviewUiState.value = overviewUiState.value.copy(indexDataUiState = IndexDataUiState.Loading)
            indexRepository.postCityIndex(
                city = overviewUiState.value.currentIndexedCity,
                indexWeight = overviewUiState.value.getIndexWeight()
            ).enqueue(object: Callback<IndexData> {

                override fun onResponse(call: Call<IndexData>, response: Response<IndexData>) {
                    val geocoder = Geocoder(context)
                    geocoder.getFromLocationName(overviewUiState.value.currentIndexedCity, 1) { location ->
                        val locationInfo = location[0]
                        if (locationInfo.hasLatitude() && locationInfo.hasLongitude()) {
                            _overviewUiState.value = _overviewUiState.value.copy(
                                isMarkerSet = true,
                                markerLatLng = LatLng(locationInfo.latitude, locationInfo.longitude)
                            )
                        }
                    }
                    response.body()
                        ?.let { _overviewUiState.value = overviewUiState.value.copy(indexDataUiState =  IndexDataUiState.Success(it)) }
                    scrollTo()
                }

                override fun onFailure(call: Call<IndexData>, t: Throwable) {
                     t.message?.let {_overviewUiState.value = overviewUiState.value.copy(
                         indexDataUiState = IndexDataUiState.Error(it)) }
                    scrollTo()
                }
            })
        }

    }
}