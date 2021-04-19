package com.example.hotelmanagementsystem_mobile.activities.user_profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.BaseActivity
import com.example.hotelmanagementsystem_mobile.activities.Homepage
import com.example.hotelmanagementsystem_mobile.activities.Login
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_signup.*

class ForgotPassword : BaseActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = FirebaseAuth.getInstance()

        buttonReset.setOnClickListener {
            resetUserPassword()
        }

        textViewLogin.setOnClickListener {
            val intent= Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    private fun resetUserPassword() {
        val email : String = et_forgot_email.text.toString().trim { it <= ' ' }

        if(validateForm(email)) {
            showProgressDialog(resources.getString(R.string.please_wait))
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener {task ->
                    hideProgressDialog()
                    if(task.isSuccessful) {
                        Toast.makeText(this, "Reset Link Sent to your Email", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, Login::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Please enter a valid Email", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun validateForm(email: String) : Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                et_forgot_email.setError("Email is Required")
                false
            }
            else -> {
                true
            }
        }
    }
}