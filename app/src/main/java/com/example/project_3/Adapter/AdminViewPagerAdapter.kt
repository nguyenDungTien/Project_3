package com.example.project_3.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.project_3.Fragment.AdminAccountFragment
import com.example.project_3.Fragment.AdminFeedbackFragment
import com.example.project_3.Fragment.AdminHomeFragment
import com.example.project_3.Fragment.AdminOrderFragment
import com.google.android.material.appbar.AppBarLayout

class AdminViewPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {
    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> AdminHomeFragment() // Replace Fragment1 with your desired Fragment for position 0
            1 -> AdminFeedbackFragment() // Replace AdminFragment with your desired Fragment for position 1
            2 -> AdminOrderFragment() // Replace Fragment3 with your desired Fragment for position 2
            3 -> AdminAccountFragment() // Replace Fragment4 with your desired Fragment for position 3
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}