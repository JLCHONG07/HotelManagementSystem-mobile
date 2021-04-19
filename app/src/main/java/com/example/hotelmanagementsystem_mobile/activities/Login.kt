package com.example.hotelmanagementsystem_mobile.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.user_profile.ForgotPassword
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.fragments.HomeFragment
import com.example.hotelmanagementsystem_mobile.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login : BaseActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signup=findViewById<TextView>(R.id.textViewSignUp)
        val login = findViewById<Button>(R.id.buttonLogin)

        auth = FirebaseAuth.getInstance()

        signup.setOnClickListener {
            val intent= Intent(this,Signup::class.java)
            startActivity(intent)
        }

        login.setOnClickListener {
            signInRegisteredUser()
        }

        textViewForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }
    }

    fun signInSuccess(user: User) {
        hideProgressDialog()
        if(user.accountType.equals("A")) {
            startActivity(Intent(this, AdminHomepage::class.java))
        }
        else{
            startActivity(Intent(this, Homepage::class.java))
        }
        finish()
    }

    private fun signInRegisteredUser() {
        val email : String = et_log_in_email.text.toString().trim { it <= ' ' }
        val password : String = et_log_in_password.text.toString().trim { it <= ' ' }

        if(validateForm(email, password)) {
            showProgressDialog(resources.getString(R.string.please_wait))
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {task ->
                    hideProgressDialog()
                    if(task.isSuccessful) {
                        FirestoreClass().loadUserData(this, HomeFragment())
                    } else {
                        Toast.makeText(this, "Login failed! Please enter a valid email and password", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun validateForm(email: String, password: String) : Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                et_log_in_email.setError("Email is Required")
                false
            }
            TextUtils.isEmpty(password) -> {
                et_log_in_password.setError("Password is Required")
                false
            }
            else -> {
                true
            }
        }
    }
}