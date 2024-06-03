package com.example.croqol.ui.review

import com.google.android.gms.maps.model.LatLng
import javax.annotation.concurrent.Immutable

@Immutable
data class ReviewUiState(
    val city: String = "",
    val textFieldCity: String = "",
    val markerLatLang: LatLng = LatLng(44.927, 16.383),
    val isMarkerSet: Boolean = false,
    val locationState: LocationState = LocationState.Nothing
)
