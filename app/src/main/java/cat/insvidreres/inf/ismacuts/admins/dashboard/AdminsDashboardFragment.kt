package cat.insvidreres.inf.ismacuts.admins.dashboard

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.databinding.FragmentAdminsDashboardBinding
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.charts.*
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.*
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.time.Duration.Companion.days

class AdminsDashboardFragment : Fragment() {

    private lateinit var binding: FragmentAdminsDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminsDashboardBinding.inflate(inflater)
        val chart = binding.chart
        configureChart(chart)

        val entries = mutableListOf<BarEntry>()

        val calendar = Calendar.getInstance()
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val daysArray = Array(daysInMonth) { i ->
            calendar.set(Calendar.DAY_OF_MONTH, i + 1)
            dateFormatter.format(calendar.time)
        }
        println("daysArray size ${daysArray.size}")
        // Add your bar entries here
        for (i in 0..daysArray.size) {
            entries.add(BarEntry(i.toFloat(), (Math.random() * 1000).toFloat()))
        }

        val dataSet = BarDataSet(entries, "")
        dataSet.colors = ColorTemplate.JOYFUL_COLORS.toList()
        val barDataSet = BarData(dataSet)
        barDataSet.barWidth = 0.7f
        chart.data = barDataSet

        chart.animateXY(1000, 2000)
        chart.invalidate()

        return binding.root
    }

    private fun configureChart(chart: BarChart) {
        chart.description.isEnabled = false
        chart.axisRight.setDrawLabels(false)
        chart.axisLeft.axisMinimum = 0f
        chart.axisLeft.axisLineWidth = 1f
        chart.axisLeft.labelCount = 8

        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)

        val calendar = Calendar.getInstance()
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val dateFormatter = SimpleDateFormat("dd", Locale.getDefault())
        val daysArray = Array(daysInMonth) { i ->
            calendar.set(Calendar.DAY_OF_MONTH, i + 1)
            dateFormatter.format(calendar.time)
        }
        // Set maximum visible bars
        chart.setVisibleXRangeMaximum(5f)

        // Zoom in to show only 5 or 6 bars at a time
        val scale = (5f / 6f) * (daysArray.size / 5f) // Total bars / Visible bars
        chart.zoom(scale, 1f, 0f, 0f)


        val xAxisFormatter = IndexAxisValueFormatter(daysArray)
        chart.xAxis.valueFormatter = xAxisFormatter
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
    }
}