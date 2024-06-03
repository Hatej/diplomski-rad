package com.example.croqol.ui.review

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LocationState {
    data object Success : LocationState
    data object Error : LocationState
    data object Loading : LocationState
    data object Nothing : LocationState
}

@HiltViewModel
class ReviewViewModel @Inject constructor(

) : ViewModel() {

    private val _reviewUiState = mutableStateOf(ReviewUiState())
    val reviewUiState = _reviewUiState

    fun updateTextFieldCity(input: String) {
        _reviewUiState.value = reviewUiState.value.copy(textFieldCity = input)
    }

    private fun updateLocationState(locationState: LocationState) {
        _reviewUiState.value = reviewUiState.value.copy(locationState = locationState)
    }
    @SuppressLint("MissingPermission", "NewApi")
    fun getCityFromLocation(context: Context) {
        updateLocationState(LocationState.Loading)
        val locationClient = LocationServices.getFusedLocationProviderClient(context)
        val geocoder = Geocoder(context)
        Log.d("ReviewViewModel", "Launching get location...")
        viewModelScope.launch {
            locationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                CancellationTokenSource().token,
            ).addOnSuccessListener { fetchedLocation ->
                Log.d("ReviewViewModel", "On Success Listener...")
                if (fetchedLocation != null) {
                    Log.d("ReviewViewModel", "Fetched location is null...")
                    val lat = fetchedLocation.latitude
                    val long = fetchedLocation.longitude
                    Log.d("ReviewViewModel", "lat: $lat lng: $long")
                    geocoder.getFromLocation(lat, long, 1) {
                        val city = it[0].locality
                        _reviewUiState.value = reviewUiState.value.copy(city = city)
                        geocoder.getFromLocationName(city, 1) {location ->
                            val locationInfo = location[0]
                            if (locationInfo.hasLatitude() && locationInfo.hasLongitude()) {
                                _reviewUiState.value = reviewUiState.value.copy(
                                    isMarkerSet = true,
                                    markerLatLang = LatLng(locationInfo.latitude, locationInfo.longitude)
                                )
                            }
                            Log.d("ReviewViewModel", it.toString())
                            Log.d("ReviewViewModel", it[0].toString())
                        }
                    }
                    updateLocationState(LocationState.Success)
                } else {
                    Log.d("ReviewViewModel", "Fetched location isn't null...")
                    updateLocationState(LocationState.Error)
                }
            }
                .addOnFailureListener { exception ->
                    updateLocationState(LocationState.Error)
                    exception.message?.let { Log.e("ReviewViewModel", it) }
                }
        }
    }

}