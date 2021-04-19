package com.example.hotelmanagementsystem_mobile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore

class SplashScreen : AppCompatActivity() {
    private val mFirestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )

        // post a time delay to show the splash screen and navigate to intro activity

        var currentUserID = FirestoreClass().getCurrentUserId()
        var accountType = ""
        if (currentUserID.isNotEmpty()) {
            Log.i("account", "onStart")
            mFirestore.collection(Constants.USERS)
                .document(currentUserID)
                .get()
                .addOnSuccessListener { document ->
                    val loggedInUser = document.toObject(User::class.java)!!
                    accountType = loggedInUser.accountType
                    Log.i("account", accountType)
                    if (accountType.equals("A")) {
                        startActivity(Intent(this, AdminHomepage::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this, Homepage::class.java))
                        finish()
                    }
                }
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, Login::class.java))
                finish()
            }, 2500)
        }

    }
}