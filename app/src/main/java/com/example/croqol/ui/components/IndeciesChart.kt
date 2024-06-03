package com.example.croqol.ui.components

import android.graphics.Typeface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.decoration.rememberHorizontalLine
import com.patrykandpatrick.vico.compose.cartesian.fullWidth
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.core.cartesian.HorizontalLayout
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.compose.common.of
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.Shape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun IndeciesChart(
    data: Map<String, Double>,
    overallIndex: Float,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember { CartesianChartModelProducer.build() }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            modelProducer.tryRunTransaction {
                columnSeries { series(data.values) }
                updateExtras { it[labelListKey] = data.keys.toList() }
            }
        }
    }
    Chart(modelProducer = modelProducer, overallIndex = overallIndex, modifier = modifier)
}

@Composable
private fun Chart(
    modelProducer: CartesianChartModelProducer,
    overallIndex: Float,
    modifier: Modifier = Modifier
) {
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                ColumnCartesianLayer.ColumnProvider.series(
                    rememberLineComponent(
                        color = MaterialTheme.colorScheme.primary,
                        thickness = 16.dp,
                        shape = remember { Shape.rounded(allPercent = 40) }
                    )
                )
            ),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(
                valueFormatter = { x, chartValues, _ ->
                    chartValues.model.extraStore[labelListKey][x.toInt()]
                },
                itemPlacer = remember { AxisItemPlacer.Horizontal.default(spacing = 1, addExtremeLabelPadding = true) },
                labelRotationDegrees = 90f
            ),
            decorations = listOf(rememberComposeHorizontalLine(overallIndex))
        ),
        modelProducer = modelProducer,
        modifier = modifier,
        marker = rememberMarker(),
        horizontalLayout = HorizontalLayout.fullWidth()
    )
}

@Composable
private fun rememberComposeHorizontalLine(horizontalLineY: Float): HorizontalLine {
    val color = MaterialTheme.colorScheme.tertiary
    return rememberHorizontalLine(
        y = { horizontalLineY },
        line = rememberLineComponent(color, HORIZONTAL_LINE_THICKNESS_DP.dp),
        labelComponent =
        rememberTextComponent(
            background = rememberShapeComponent(Shape.Pill, color),
            padding =
            Dimensions.of(
                HORIZONTAL_LINE_LABEL_HORIZONTAL_PADDING_DP.dp,
                HORIZONTAL_LINE_LABEL_VERTICAL_PADDING_DP.dp,
            ),
            margins = Dimensions.of(HORIZONTAL_LINE_LABEL_MARGIN_DP.dp),
            typeface = Typeface.MONOSPACE,
        ),
    )
}

private const val HORIZONTAL_LINE_THICKNESS_DP = 2f
private const val HORIZONTAL_LINE_LABEL_HORIZONTAL_PADDING_DP = 8f
private const val HORIZONTAL_LINE_LABEL_VERTICAL_PADDING_DP = 2f
private const val HORIZONTAL_LINE_LABEL_MARGIN_DP = 4f
val labelListKey = ExtraStore.Key<List<String>>()