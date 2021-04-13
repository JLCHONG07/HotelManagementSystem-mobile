package com.example.hotelmanagementsystem_mobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )

        // post a time delay to show the splash screen and navigate to intro activity
        Handler(Looper.getMainLooper()).postDelayed({

            var currentUserID = FirestoreClass().getCurrentUserId()
            if(currentUserID.isNotEmpty()) {
                startActivity(Intent(this, Homepage::class.java))
            } else {
                startActivity(Intent(this, Login::class.java))
            }
            finish()
        }, 2500)
    }
}