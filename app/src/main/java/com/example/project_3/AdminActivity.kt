package com.example.project_3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.project_3.Adapter.AdminViewPagerAdapter
import com.example.project_3.Fragment.AdminAccountFragment
import com.example.project_3.Fragment.AdminFeedbackFragment
import com.example.project_3.Fragment.AdminHomeFragment
import com.example.project_3.Fragment.AdminOrderFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class AdminActivity : AppCompatActivity() {
    private lateinit var mBottomNavigationView: BottomNavigationView
    private lateinit var mViewPager: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        unitUi()
        setUpViewPager()
        unitListener()
    }




    private fun unitUi() {
        mBottomNavigationView = findViewById(R.id.bottom_nav)
        mViewPager=findViewById(R.id.view_pager)
    }

    private fun unitListener() {
        mBottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_home -> {
                   Toast.makeText(this,"Home",Toast.LENGTH_SHORT).show()
                    mViewPager.currentItem=0
                    true
                }
                R.id.action_feedback -> {
                    Toast.makeText(this,"FeedBack",Toast.LENGTH_SHORT).show()
                    mViewPager.currentItem=1
                    true
                }
                R.id.action_order -> {
                    Toast.makeText(this,"Order",Toast.LENGTH_SHORT).show()
                    mViewPager.currentItem=2
                    true
                }
                R.id.action_account -> {
                    Toast.makeText(this,"Account",Toast.LENGTH_SHORT).show()
                    mViewPager.currentItem=3
                    true
                }
                else -> false
            }
        }

    }

    private fun setUpViewPager() {
        val mAdminViewPagerAdapter=AdminViewPagerAdapter(supportFragmentManager,FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        mViewPager.adapter=mAdminViewPagerAdapter
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // This method is called when the current page is scrolled
                // You can implement your desired logic here
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        mBottomNavigationView.menu.findItem(R.id.action_home).isChecked = true
                    }
                    1 -> {
                        mBottomNavigationView.menu.findItem(R.id.action_feedback).isChecked = true
                    }
                    2 -> {
                        mBottomNavigationView.menu.findItem(R.id.action_order).isChecked = true
                    }
                    3 -> {
                        mBottomNavigationView.menu.findItem(R.id.action_account).isChecked = true
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                // This method is called when the scroll state changes (idle, dragging, settling)
                // You can implement your desired logic here
            }
        })
    }


}


