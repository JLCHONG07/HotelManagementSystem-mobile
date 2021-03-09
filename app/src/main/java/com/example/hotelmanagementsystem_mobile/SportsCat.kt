package com.example.hotelmanagementsystem_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_sports_cat.*

class SportsCat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sports_cat)
        
      val actionBar: ActionBar?=supportActionBar
       actionBar!!.setDisplayHomeAsUpEnabled(true)
      actionBar!!.setDisplayHomeAsUpEnabled(true)
        var intent=intent
      val test=intent.getStringExtra("Sports")

       txtVIewTest.text=test
    }
}