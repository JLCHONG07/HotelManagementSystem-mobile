package com.example.hotelmanagementsystem_mobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.hotelmanagementsystem_mobile.R

class Signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signin=findViewById<TextView>(R.id.textViewSignIn)

        signin.setOnClickListener {
            val intent= Intent(this,Login::class.java)
            startActivity(intent)

        }
    }
}