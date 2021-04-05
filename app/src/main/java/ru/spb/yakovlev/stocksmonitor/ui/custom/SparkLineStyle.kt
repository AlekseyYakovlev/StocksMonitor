package ru.spb.yakovlev.stocksmonitor.ui.custom

import com.github.mikephil.charting.charts.LineChart
import javax.inject.Inject


class SparkLineStyle @Inject constructor(
) {
    fun styleChart(lineChart: LineChart) = lineChart.apply {
        axisRight.isEnabled = false
        axisLeft.apply {
            isEnabled = false
        }
        xAxis.apply {
            isEnabled = false
        }
        setTouchEnabled(true)
        isDragXEnabled = true
        isDragYEnabled = false
        setScaleEnabled(false)
        setPinchZoom(false)
        setViewPortOffsets(0f, 0f, 0f, 0f)
        description = null
        legend.isEnabled = false
        animateX(1000)
    }

}