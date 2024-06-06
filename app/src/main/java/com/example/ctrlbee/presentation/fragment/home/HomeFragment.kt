package com.example.ctrlbee.presentation.fragment.home

import android.icu.util.Calendar
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ctrlbee.R
import com.example.ctrlbee.core.Constants.Companion.TO_DO_LIST_DATE
import com.example.ctrlbee.data.repository.SharedPreferencesRepo
import com.example.ctrlbee.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject


class HomeFragment: Fragment(R.layout.fragment_home) {

    @Inject lateinit var sharedPreferencesRepo: SharedPreferencesRepo
    private val viewBinding: FragmentHomeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initActions()
    }

    private fun initActions() = with(viewBinding) {
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)

            val fragment = ToDoListFragment()
            fragment.arguments = Bundle().apply {
                putString(TO_DO_LIST_DATE, formattedDate)
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("Product Details")
                .commit()
        }
    }

}