package com.example.hotelmanagementsystem_mobile.activities

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.adapters.CheckInDetailsAdapter
import com.example.hotelmanagementsystem_mobile.models.*
import kotlinx.android.synthetic.main.activity_check_in.*

class CheckInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_in)

        setupActionBar()

        val typeface = Typeface.createFromAsset(assets, "BerkshireSwash-Regular.ttf")
        tv_check_in_title.typeface = typeface

        getCheckInDetails()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_check_in_activity)

        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_check_in_activity.setNavigationOnClickListener{onBackPressed()}
    }

    private fun getCheckInDetails() {
        var checkInDetailsList : ArrayList<CheckIn> = ArrayList()
        checkInDetailsList.add(CheckIn("RSVTN0001", 3, 2, 2, 4, "Quest Room", "2 February 2021, 2:30pm", "checkin0001"))

        rv_check_in_details.visibility = View.VISIBLE
        rv_check_in_details.layoutManager = LinearLayoutManager(this)
        rv_check_in_details.setHasFixedSize(true)

        val adapter = CheckInDetailsAdapter(this, checkInDetailsList)
        rv_check_in_details.adapter = adapter
    }
}