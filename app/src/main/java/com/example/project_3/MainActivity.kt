package com.example.project_3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project_3.Fragment.*
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var mBottomNavigationView:BottomNavigationView
    private lateinit var homeFragment:UserHomeFragment
    private lateinit var cartFragment: UserCartFragment
    private lateinit var feedbackFragment: UserFeedbackFragment
    private lateinit var contactFragment: UserContactFragment
    private lateinit var accountFragment: UserAccountFragment
    private lateinit var badgeDrawable: BadgeDrawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        unitUi()
        supportFragmentManager.beginTransaction().replace(R.id.view_pager,homeFragment).commit()
        unitListener()
    }



    private fun unitUi() {
        mBottomNavigationView=findViewById(R.id.bottom_nav)
        homeFragment= UserHomeFragment()
        cartFragment= UserCartFragment()
        feedbackFragment= UserFeedbackFragment()
        contactFragment= UserContactFragment()
        accountFragment= UserAccountFragment()
        badgeDrawable=mBottomNavigationView.getOrCreateBadge(R.id.action_cart)
        badgeDrawable.isVisible = true
        badgeDrawable.number=8


    }
    private fun unitListener() {
        mBottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.view_pager,homeFragment).commit()
                    true
                }
                R.id.action_feedback -> {
                    supportFragmentManager.beginTransaction().replace(R.id.view_pager,feedbackFragment).commit()
                    true
                }
                R.id.action_contact -> {
                    supportFragmentManager.beginTransaction().replace(R.id.view_pager,contactFragment).commit()
                    true
                }
                R.id.action_account -> {
                    supportFragmentManager.beginTransaction().replace(R.id.view_pager,accountFragment).commit()
                    true
                }
                R.id.action_cart -> {
                    supportFragmentManager.beginTransaction().replace(R.id.view_pager,cartFragment).commit()
                    true
                }
                else -> false
            }
        }
    }



}