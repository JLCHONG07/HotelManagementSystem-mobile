package com.example.hotelmanagementsystem_mobile

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_check_in.*

class CheckIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_in)

        setupActionBar()

        val typeface = Typeface.createFromAsset(assets, "BerkshireSwash-Regular.ttf")
        tv_check_in_title.typeface = typeface
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
}