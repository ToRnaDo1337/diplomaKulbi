package com.example.ctrlbee.presentation.fragment.profile

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ctrlbee.R
import com.example.ctrlbee.databinding.FragmentProfileStatisticsBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF


class StatisticsFragment : Fragment(R.layout.fragment_profile_statistics) {

    private val viewBinding: FragmentProfileStatisticsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initActions()
    }

    private fun initActions() = with(viewBinding) {
        configurePieChart()
        setPieChartData()
    }

    private fun configurePieChart() = with(viewBinding) {
        pieChart.apply {
            setUsePercentValues(true)
            setDrawEntryLabels(false)
            description.isEnabled = false
            dragDecelerationFrictionCoef = 0.95F
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(110)
            rotationAngle = 0f
            isRotationEnabled = true
            isHighlightPerTapEnabled = true
            animateY(1400, Easing.EaseInOutQuad)
            spin(2000, 0F, 360F, Easing.EaseInOutQuad)

            pieChart.legend.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                orientation = Legend.LegendOrientation.VERTICAL
                setDrawInside(false)
                textSize = 16f
                xOffset = 8f
                yOffset = 70f
            }
        }
    }

    private fun setPieChartData() = with(viewBinding) {
        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(18.toFloat(), "Working"))
        entries.add(PieEntry(82.toFloat(), "Other"))

        val dataSet = PieDataSet(entries, "")
        dataSet.apply {
            setDrawIcons(false)
            sliceSpace = 3f
            iconsOffset = MPPointF(0F, 40F)
            selectionShift = 5f
        }

        val colors = arrayListOf(
            ContextCompat.getColor(requireContext(), R.color.mainYellow),
            ContextCompat.getColor(requireContext(), R.color.subYellow)
        )
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.apply {
            setValueFormatter(PercentFormatter(pieChart))
            setValueTextSize(14f)
            setValueTextColor(Color.BLACK)
        }

        pieChart.data = data
        pieChart.highlightValues(null)
        pieChart.invalidate()
    }

}