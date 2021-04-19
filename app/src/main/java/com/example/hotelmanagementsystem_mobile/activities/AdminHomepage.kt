package com.example.hotelmanagementsystem_mobile.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.fragments.AdminAccountFragment
import com.example.hotelmanagementsystem_mobile.fragments.AdminHomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class AdminHomepage : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_homepage)

        FirestoreClass().loadUserData(this, AdminHomeFragment())

        //Pass user details to the fragment
        setCurrentFragment(AdminHomeFragment())

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId) {
            R.id.home -> {
                setCurrentFragment(AdminHomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.account -> {
                setCurrentFragment(AdminAccountFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
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