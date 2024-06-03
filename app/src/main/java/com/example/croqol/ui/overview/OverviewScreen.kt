package com.example.croqol.ui.overview

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.croqol.ui.components.CroQoLSlider
import com.example.croqol.ui.components.IndeciesChart
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun OverviewScreen (
    viewModel: OverviewViewModel
) {
    val overviewUiState by viewModel.overviewUiState
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()
    Surface(
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            TextFieldWithIcon(
                label = "Grad",
                value = overviewUiState.searchCity,
                onValueChange = { city -> viewModel.updateSearchCity(city) },
                submit = { viewModel.getCityIndices(context) { scope.launch { bringIntoViewRequester.bringIntoView() } } },
                isError = overviewUiState.indexDataUiState is IndexDataUiState.Error,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                IconButton(onClick = { viewModel.getCityIndices(context) { scope.launch { bringIntoViewRequester.bringIntoView() } } }) {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
                }
            }
            if (overviewUiState.indexDataUiState is IndexDataUiState.Error) {
                Text((overviewUiState.indexDataUiState as IndexDataUiState.Error).error)
            }
            allSliders.forEachIndexed { index, slider ->
                CroQoLSlider(
                    label = slider.label,
                    value = overviewUiState.getSliderValue(slider.croQoLSlider),
                    onValueChange = { value -> viewModel.updateSlider(slider.croQoLSlider, value) },
                    icon = slider.icon,
                    expanded = overviewUiState.isExpandedMap[index] ?: false,
                    flipExpanded = {
                        viewModel.updateExpandedSlider(index)
                    }
                ) {
                    Column {
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            slider.info,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
            when (overviewUiState.indexDataUiState) {
                is IndexDataUiState.Error -> {
                    Text(text = (overviewUiState.indexDataUiState as IndexDataUiState.Error).error)
                }
                is IndexDataUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp)
                    )
                }
                is IndexDataUiState.Nothing -> {}
                is IndexDataUiState.Success -> {
                    val indexData = (overviewUiState.indexDataUiState as IndexDataUiState.Success).indexData
                    Surface(
                        shadowElevation = 16.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier
                            .bringIntoViewRequester(bringIntoViewRequester)
                            .padding(8.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            if (overviewUiState.isMarkerSet) {
                                val cameraPositionState = rememberCameraPositionState {
                                    position = CameraPosition.fromLatLngZoom(overviewUiState.markerLatLng, 10f)
                                }
                                cameraPositionState.position = CameraPosition.fromLatLngZoom(overviewUiState.markerLatLng, 10f)
                                GoogleMap(
                                    modifier = Modifier
                                        .height(216.dp)
                                        .padding(vertical = 16.dp),
                                    cameraPositionState = cameraPositionState
                                ) {
                                    Marker(
                                        state = MarkerState(position = overviewUiState.markerLatLng),
                                        title = overviewUiState.currentIndexedCity
                                    )
                                }
                            }
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Indeks kvalitete života za",
                                    style = MaterialTheme.typography.titleLarge,

                                    )
                                Text(
                                    text = overviewUiState.currentIndexedCity,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                            Surface(
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onTertiaryContainer),
                                shadowElevation = 16.dp,
                                shape = CircleShape,
                                modifier = Modifier
                                    .padding(bottom = 16.dp)
                                    .size(96.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "${indexData.overallIndex().toInt()}",
                                        style = MaterialTheme.typography.headlineLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Log.d("OverviewScreen", indexData.toDataMap().toString())
                            IndeciesChart(
                                data = indexData.toDataMap(),
                                overallIndex = indexData.overallIndex().toFloat(),
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .padding(bottom = 16.dp)
                            )
                            LazyRow {

                            }
                        }
                    }
                    if (viewModel.isFirstTimeSearch) {
                        LaunchedEffect(viewModel.isFirstTimeSearch) {
                            scope.launch {
                                bringIntoViewRequester.bringIntoView()
                                viewModel.updateIsFirstTimeSearch(false)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TextFieldWithIcon (
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    submit: () -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    icon: @Composable () -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        label = { Text(text = label) },
        value = value,
        onValueChange = onValueChange,
        leadingIcon = icon,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onDone = { submit() }
        ),
        isError = isError
    )
}

const val ANIMATION_DURATION = 500