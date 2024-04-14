 package cat.insvidreres.inf.ismacuts.admins.dashboard

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.room.util.getColumnIndex
import cat.insvidreres.inf.ismacuts.R
import cat.insvidreres.inf.ismacuts.admins.AdminsSharedViewModel
import cat.insvidreres.inf.ismacuts.databinding.FragmentAdminsDashboardBinding
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.charts.*
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.*
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.time.Duration.Companion.days

class AdminsDashboardFragment : Fragment() {

    private lateinit var binding: FragmentAdminsDashboardBinding
    private val viewModel: AdminsDashboardViewModel by viewModels()
    private val adminViewModel: AdminsSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminsDashboardBinding.inflate(inflater)
        val chart = binding.chart
        configureChart(chart)
        var currentMonth = Calendar.getInstance().get(Calendar.MONTH)

        //Handle back button
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.resetRevenueDataList()
            findNavController().navigateUp()
        }

        viewModel.loadProfessionalName(adminViewModel.adminEmail)
        viewModel.professionalName.observe(viewLifecycleOwner) {
            binding.adminDashboardGreetingTV.text = "Greetings, ${it}"
        }

        binding.dashboardMonthRevenueTV.text = "Revenue for ${loadMonth(currentMonth)}"

        viewModel.loadCurrentMonthRevenueData(adminViewModel.adminEmail, currentMonth) {
            Toast.makeText(
                requireContext(),
                "No data found for currentMonth ${loadMonth(currentMonth)}",
                Toast.LENGTH_SHORT)
                .show()
        }
        viewModel.revenueData.observe(viewLifecycleOwner) { revenueList ->
            if (revenueList.isNotEmpty()) {
                updateChart(chart, revenueList)
            }
        }

        binding.dashboardPreviousMonthButton.setOnClickListener {
            if (currentMonth > 0 && currentMonth <= Calendar.getInstance().get(Calendar.MONTH)) {
                currentMonth--


                viewModel.loadCurrentMonthRevenueData(adminViewModel.adminEmail, currentMonth) {
                    Toast.makeText(
                        requireContext(),
                        "No data found for currentMonth ${loadMonth(currentMonth)}",
                        Toast.LENGTH_SHORT)
                        .show()
                }
                binding.dashboardMonthRevenueTV.text = "Revenue for ${loadMonth(currentMonth)}"
            } else {
                Toast.makeText(
                    requireContext(),
                    "You can't see data past ${loadMonth(currentMonth)}",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.dashboardNextMonthButton.setOnClickListener {
            if (currentMonth < 11 && currentMonth < Calendar.getInstance().get(Calendar.MONTH)) {
                currentMonth++


                viewModel.loadCurrentMonthRevenueData(adminViewModel.adminEmail, currentMonth) {
                    Toast.makeText(
                        requireContext(),
                        "No data found for currentMonth ${loadMonth(currentMonth)}",
                        Toast.LENGTH_SHORT)
                        .show()
                }
                binding.dashboardMonthRevenueTV.text = "Revenue for ${loadMonth(currentMonth)}"
            } else {
                Toast.makeText(
                    requireContext(),
                    "You can't see data from the future ${loadMonth(currentMonth)}",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return binding.root
    }

    private fun loadMonth(currentMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, currentMonth)
        val dateFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        return dateFormat.format(calendar.time)
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

        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
    }

    private fun updateChart(chart: BarChart, revenueList: List<RevenueData>) {
        val entries = mutableListOf<BarEntry>()
        val labels = mutableListOf<String>()

        revenueList.forEachIndexed { index, revenueData ->
            entries.add(BarEntry(index.toFloat(), revenueData.revenue.toFloat()))
            labels.add(revenueData.dayNumber.toString())
        }

        val dataSet = BarDataSet(entries, "Revenue")
        dataSet.colors = ColorTemplate.JOYFUL_COLORS.toList()
        val barDataSet = BarData(dataSet)
        barDataSet.barWidth = 0.3f
        chart.data = barDataSet

        val xAxisFormatter = IndexAxisValueFormatter(labels)
        chart.xAxis.valueFormatter = xAxisFormatter
        chart.xAxis.granularity = 1f

        chart.data.setValueTextSize(14f)
        chart.data.setValueFormatter(LargeValueFormatter())

        val visibleBarCount = 4 // Maximum of 4 bars visible
        val scale = if (entries.size > visibleBarCount) {
            val visibleRange = visibleBarCount.toFloat() / entries.size.toFloat()
            val bufferScale = 0.5f / visibleRange // Add a buffer to ensure the first bar is fully visible
            visibleRange + bufferScale
        } else {
            1f // No need to zoom if there are fewer bars than the visible count
        }
        chart.zoom(scale, 1f, 0f, 0f)

        chart.animateXY(1000, 2000)
        chart.invalidate()
    }

}