package com.example.hotelmanagementsystem_mobile.activities

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_signup.*

class Signup : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signin=findViewById<TextView>(R.id.textViewSignIn)

        signin.setOnClickListener {
            val intent= Intent(this,Login::class.java)
            startActivity(intent)
        }

        buttonSignUp.setOnClickListener {
            registerUser()
        }
    }

    fun userRegisterSuccess() {
        Toast.makeText(this, "You have successfully registered an account", Toast.LENGTH_LONG).show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, Login::class.java))
        finish()
    }

    private fun registerUser() {
        val name : String = et_sign_up_username.text.toString().trim { it <= ' ' }
        val passportNumber : String = et_sign_up_ic_passport.text.toString().trim { it <= ' ' }
        val email : String = et_sign_up_email.text.toString().trim { it <= ' ' }
        val password : String = et_sign_up_password.text.toString().trim { it <= ' ' }
        val accountType : String = "C"

        if(validateForm(name, passportNumber, email, password)) {
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    task ->
                    if(task.isSuccessful) {
                        val firebaseUser : FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid, accountType, name, registeredEmail, passportNumber)
                        FirestoreClass().registerUser(this, user)
                    } else {
                        Toast.makeText(this, "Registration failed !", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun validateForm(name: String, passportNumber: String, email: String, password: String) : Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                et_sign_up_username.setError("Please enter your name")
                false
            }
            TextUtils.isEmpty(passportNumber) -> {
                et_sign_up_ic_passport.setError("Please enter your IC/passport number")
                false
            }
            TextUtils.isEmpty(email) -> {
                et_sign_up_email.setError("Please enter your email")
                false
            }
            TextUtils.isEmpty(password) -> {
                et_sign_up_password.setError("Please enter your password")
                false
            }
            password.length <= 8 -> {
                et_sign_up_password.setError("Password must be 8 characters or more")
                false
            }
            else -> {
                true
            }
        }
    }
}