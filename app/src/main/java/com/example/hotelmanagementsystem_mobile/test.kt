package com.example.hotelmanagementsystem_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hotelmanagementsystem_mobile.ui.main.TestFragment

class test : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TestFragment.newInstance())
                .commitNow()
        }
    }
}