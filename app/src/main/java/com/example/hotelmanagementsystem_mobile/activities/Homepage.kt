package com.example.hotelmanagementsystem_mobile.activities

import android.os.Bundle
import android.os.Parcel
import androidx.fragment.app.Fragment
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.fragments.AccountFragment
import com.example.hotelmanagementsystem_mobile.fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class Homepage : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirestoreClass().loadUserData(this, HomeFragment())

        //Pass user details to the fragment
        setCurrentFragment(HomeFragment())

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        when(item.itemId) {
            R.id.home -> {
                setCurrentFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.account -> {
                setCurrentFragment(AccountFragment())
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