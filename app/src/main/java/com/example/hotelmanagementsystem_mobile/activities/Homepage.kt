package com.example.hotelmanagementsystem_mobile.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.fragments.AccountFragment
import com.example.hotelmanagementsystem_mobile.fragments.HomeFragment
import com.example.hotelmanagementsystem_mobile.models.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class Homepage : BaseActivity() {

    private lateinit var mUserName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setCurrentFragment(HomeFragment())

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    fun updateUserDetails(user: User) {
        mUserName = user.name
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