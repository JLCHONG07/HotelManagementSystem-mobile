package com.example.hotelmanagementsystem_mobile.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.facilities_booking.Categories
import com.example.hotelmanagementsystem_mobile.fragments.AccountFragment
import com.example.hotelmanagementsystem_mobile.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class Homepage : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainFragment = HomeFragment.newInstance("", "")
        val accountFragment = AccountFragment.newInstance("", "")
        setCurrentFragment(mainFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(mainFragment)
                R.id.account->setCurrentFragment(accountFragment)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper,fragment)
            commit()
        }

    override fun onBackPressed() {
        doubleBackToExit()
    }
}