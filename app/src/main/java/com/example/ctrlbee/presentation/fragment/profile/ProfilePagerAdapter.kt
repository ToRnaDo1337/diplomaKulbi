package com.example.ctrlbee.presentation.fragment.profile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfilePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MediaFragment()
            1 -> StatisticsFragment()
            else -> MediaFragment()
        }
    }

}