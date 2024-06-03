package com.example.croqol.ui.components

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.croqol.ui.overview.ANIMATION_DURATION
import com.example.croqol.ui.theme.CroQoLTheme

@Composable
fun CroQoLSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    icon: ImageVector,
    expanded: Boolean = false,
    flipExpanded: () -> Unit = {},
    expandedContent: @Composable () -> Unit = {}
) {
    Surface(
        shadowElevation = 16.dp,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(label,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            ) {
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
                        .padding(end = 8.dp)
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

@Preview
@Composable
fun SliderPreview() {
    CroQoLTheme {
        CroQoLSlider(label = "Slider", value = 50f, onValueChange = {}, icon = Icons.Default.Money)
    }
}