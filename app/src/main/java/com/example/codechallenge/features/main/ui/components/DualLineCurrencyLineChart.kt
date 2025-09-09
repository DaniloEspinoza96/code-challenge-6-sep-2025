import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.codechallenge.features.main.domain.model.CurrencyEntry
import com.example.codechallenge.features.main.domain.model.CurrencyHistory
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DualLineCurrencyLineChart(
    historyA: CurrencyHistory?,
    historyB: CurrencyHistory?,
    aLineColor: Color,
    bLineColor: Color
    ) {
    val scrollState = rememberVicoScrollState(initialScroll = Scroll.Absolute.End)
    val zoomState = rememberVicoZoomState(initialZoom = Zoom.Content)

    val entriesA: List<CurrencyEntry> = historyA?.entries ?: emptyList()
    val entriesB: List<CurrencyEntry> = historyB?.entries ?: emptyList()

    val iso = remember { DateTimeFormatter.ISO_LOCAL_DATE }

    val aSorted = remember(entriesA) { entriesA.sortedBy { it.date } }
    val bSorted = remember(entriesB) { entriesB.sortedBy { it.date } }

    val aX = remember(aSorted) { aSorted.map { LocalDate.parse(it.date, iso).toEpochDay().toFloat() } }
    val aY = remember(aSorted) { aSorted.map { it.value.toFloat() } }

    val bX = remember(bSorted) { bSorted.map { LocalDate.parse(it.date, iso).toEpochDay().toFloat() } }
    val bY = remember(bSorted) { bSorted.map { it.value.toFloat() } }

    Column(
        modifier = Modifier.padding(horizontal = 32.dp)
    ){
        if (entriesA.isEmpty() && entriesB.isEmpty()) {
            Text("Sin datos")
            return
        }

        val modelProducer = remember { CartesianChartModelProducer() }
        LaunchedEffect(aX, aY, bX, bY) {
            modelProducer.runTransaction {
                lineSeries {
                    if (aX.isNotEmpty()) series(x = aX, y = aY)
                    if (bX.isNotEmpty()) series(x = bX, y = bY)
                }
            }
        }

        val xFmt = remember { DateTimeFormatter.ofPattern("dd/MM", Locale.getDefault()) }

        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberLineCartesianLayer(
                    lineProvider = LineCartesianLayer.LineProvider.series(
                        listOf(
                            LineCartesianLayer.rememberLine(
                                fill = LineCartesianLayer.LineFill.single(fill(aLineColor))
                            ),
                            LineCartesianLayer.rememberLine(
                                fill = LineCartesianLayer.LineFill.single(fill(bLineColor))
                            )
                        )
                    )
                ),
                startAxis = VerticalAxis.rememberStart(
                    valueFormatter = { _, y, _ -> String.format(Locale.getDefault(), "%.0f", y) },
                    guideline = null
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    valueFormatter = { _, x, _ -> LocalDate.ofEpochDay(x.toLong()).format(xFmt) },
                    guideline = null
                ),
            ),
            modelProducer = modelProducer,
            zoomState = zoomState,
            scrollState = scrollState
        )
    }
}