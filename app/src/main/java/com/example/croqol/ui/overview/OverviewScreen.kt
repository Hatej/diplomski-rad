package com.example.croqol.ui.overview

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.example.croqol.ui.theme.CroQoLTheme

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun OverviewScreen (
    modifier: Modifier = Modifier,
    viewModel: OverviewViewModel
) {
    val overviewUiState by viewModel.overviewUiState
    Surface(
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            val croatia = LatLng(44.927, 16.383)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(croatia, 6f)
            }
            GoogleMap(
                modifier = Modifier
                    .height(216.dp)
                    .fillMaxWidth(),
                cameraPositionState = cameraPositionState
            ) {
            }
            TextFieldWithIcon(
                label = "Grad",
                value = overviewUiState.searchCity,
                onValueChange = { city -> viewModel.updateSearchCity(city) },
                submit = { viewModel.getCatFact() },
                modifier = Modifier.padding(16.dp)
            ) {
                IconButton(onClick = { viewModel.getCatFact() }) {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
                }
            }
            CatFactText(catFactUiState = overviewUiState.catFactUiState, modifier = Modifier.padding(16.dp))
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
                    HorizontalDivider(
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp)
                    )
                    Text(
                        slider.info,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
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
        )
    )
}

@Composable
fun CroQoLSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    expanded: Boolean = false,
    flipExpanded: () -> Unit = {},
    expandedContent: @Composable () -> Unit = {}
) {
    Surface(
        shadowElevation = 16.dp,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 4.dp)
            ) {
                Text(label,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp, end = 8.dp)
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = ""
                    )
                    IconButton(
                        onClick = flipExpanded,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(top = 4.dp)
                    ) {
                        val rotation by animateFloatAsState(
                            targetValue = if (expanded) 0f else -180f,
                            animationSpec = tween(500), label = ""
                        )
                        Icon(
                            imageVector = Icons.Default.ExpandLess,
                            contentDescription = "",
                            modifier = Modifier.rotate(rotation)
                        )
                    }
                }
                Slider(
                    value = value,
                    onValueChange = onValueChange,
                    valueRange = 0f..100f,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                )
            }
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(animationSpec = tween(ANIMATION_DURATION)) +
                        expandVertically(animationSpec = tween(ANIMATION_DURATION)),
                exit = fadeOut(animationSpec = tween(ANIMATION_DURATION)) +
                        shrinkVertically(animationSpec = tween(ANIMATION_DURATION))
            ) {
                expandedContent()
            }
        }
    }
}

@Composable
fun CatFactText (
    catFactUiState: CatFactUiState,
    modifier: Modifier = Modifier
) {
    when (catFactUiState) {
        is CatFactUiState.Nothing -> Unit
        is CatFactUiState.Error -> Text("There was an error!", modifier = modifier)
        is CatFactUiState.Loading -> CircularProgressIndicator(
            modifier = modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        is CatFactUiState.Success -> Text(catFactUiState.catFact.fact, modifier = modifier)
    }
}

@Preview
@Composable
fun SliderPreview() {
    CroQoLTheme {
        CroQoLSlider(label = "Slider", value = 50f, onValueChange = {}, icon = Icons.Default.Money)
    }
}

const val ANIMATION_DURATION = 500