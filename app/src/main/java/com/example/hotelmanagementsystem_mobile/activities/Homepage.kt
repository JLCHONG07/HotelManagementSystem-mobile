package com.example.hotelmanagementsystem_mobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.hotelmanagementsystem_mobile.R
import kotlinx.android.synthetic.main.activity_main.*

class Homepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val icon_facilities_booking=findViewById<ImageView>(R.id.icon_facilities_booking)

        icon_facilities_booking.setOnClickListener {
                val intent= Intent(this, Categories::class.java)
                startActivity(intent)
        }

        icon_check_in.setOnClickListener {
            startActivity(Intent(this, CheckInActivity::class.java))
        }
    }


}