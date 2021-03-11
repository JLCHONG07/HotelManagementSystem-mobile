package com.example.hotelmanagementsystem_mobile.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.facilities_booking.Categories
import kotlinx.android.synthetic.main.activity_main.*

class Homepage : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val icon_facilities_booking=findViewById<ImageView>(R.id.icon_facilities_booking)

        icon_facilities_booking.setOnClickListener {
                val intent= Intent(this, Categories::class.java)
                startActivity(intent)
        }

        icon_check_in.setOnClickListener {
            val intent = Intent(this, CheckInActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        doubleBackToExit()
    }
}