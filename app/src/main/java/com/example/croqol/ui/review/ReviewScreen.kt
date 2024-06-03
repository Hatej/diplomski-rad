package com.example.croqol.ui.review

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("MissingPermission")
@Composable
fun ReviewScreen(
    viewModel: ReviewViewModel
) {
    val reviewUiState by viewModel.reviewUiState
    val context = LocalContext.current
    PermissionBox(
        permission = Manifest.permission.ACCESS_COARSE_LOCATION,
        description = "Potrebno Je Dopuštenje Za Pristup Lokaciji"
    ) {
        if (viewModel.reviewUiState.value.city == "" ) {
            LaunchedEffect(Unit) {
                viewModel.getCityFromLocation(context)
            }
        }
        CurrentLocationContent(
            reviewUiState = reviewUiState,
            updateTextFieldCity = { input -> viewModel.updateTextFieldCity(input) },
            retryLocation = { viewModel.getCityFromLocation(context) }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION]
)
@Composable
fun CurrentLocationContent(
    reviewUiState: ReviewUiState,
    updateTextFieldCity: (String) -> Unit,
    retryLocation: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shadowElevation = 16.dp,
            shape = MaterialTheme.shapes.large
        ) {
            when (reviewUiState.locationState) {
                is LocationState.Success -> {
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(
                            reviewUiState.markerLatLang,
                            10f
                        )
                    }
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(reviewUiState.markerLatLang, 10f)
                    GoogleMap(
                        modifier = Modifier
                            .height(216.dp)
                            .padding(vertical = 16.dp),
                        cameraPositionState = cameraPositionState
                    ) {
                        Marker(
                            state = MarkerState(position = reviewUiState.markerLatLang),
                            title = reviewUiState.city
                        )
                    }
                }
                LocationState.Error -> {
                    Column {
                        Text(
                            text = "Greška prilikom dohvata lokacije! Molimo vas unesite lokaciju sami ili pritisnite na gumb da bi pokušali ponovno.",
                            modifier = Modifier.padding(8.dp)
                        )
                        Row(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            TextField(
                                value = reviewUiState.textFieldCity,
                                onValueChange = updateTextFieldCity,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Button(onClick = { retryLocation() }) {
                                Text(text = "Pokušaj ponovo")
                            }
                        }
                    }
                }
                LocationState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(32.dp)
                    )
                }
                LocationState.Nothing -> {}
            }
        }
    }
}