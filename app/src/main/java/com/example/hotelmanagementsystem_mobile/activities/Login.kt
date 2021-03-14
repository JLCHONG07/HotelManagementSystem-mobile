package com.example.hotelmanagementsystem_mobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.hotelmanagementsystem_mobile.R

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signup=findViewById<TextView>(R.id.textViewSignUp)
        val login = findViewById<Button>(R.id.buttonLogin)

        signup.setOnClickListener {
            val intent= Intent(this,Signup::class.java)
            startActivity(intent)

        }


    }
}