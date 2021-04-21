package com.example.hotelmanagementsystem_mobile.activities.user_profile

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.BaseActivity
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.utils.Constants
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_user_password.*
import kotlinx.android.synthetic.main.activity_change_user_password.et_confirm_new_password
import kotlinx.android.synthetic.main.activity_change_user_password.et_current_password
import kotlinx.android.synthetic.main.activity_change_user_password.et_new_password
import kotlinx.android.synthetic.main.activity_edit_user_profile.*

class ChangeUserPassword : BaseActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_user_password)
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Change Password"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()

        buttonChange.setOnClickListener {
            changeUserPassword()
        }
    }

    //when user click on back button on title, go to previous page
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun changeUserPassword() {
        val current_password: String = et_current_password.text.toString().trim { it <= ' ' }
        val new_password: String = et_new_password.text.toString().trim { it <= ' ' }
        val confirm_new_password: String = et_confirm_new_password.text.toString().trim { it <= ' ' }

        if (validateForm(current_password, new_password, confirm_new_password)) {
            showProgressDialog(resources.getString(R.string.please_wait))
            val user = auth.currentUser
            if(user != null && user.email != null){
                //get the credential of current user
                val credential = EmailAuthProvider.getCredential(user.email!!, current_password)

                //prompt user to re-provide their sign-in credentials to reauthenticate the current password
                user?.reauthenticate(credential)
                    ?.addOnCompleteListener {
                        if(it.isSuccessful){
                            //update password
                            user?.updatePassword(new_password)
                                ?.addOnCompleteListener { task ->
                                    if(task.isSuccessful){
                                        hideProgressDialog()
                                        Toast.makeText(this, "Change Password Successfully.", Toast.LENGTH_LONG).show()
                                        setResult(Activity.RESULT_OK)
                                        finish()
                                    }
                                }
                        }
                        else {
                            hideProgressDialog()
                            Toast.makeText(this, "Current Password is Wrong.", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

    }

    //validate the form field
    private fun validateForm(current_password: String, new_password: String, confirm_new_password: String): Boolean {
        return when {
            TextUtils.isEmpty(current_password) -> {
                et_current_password.setError("Current Password is Required")
                false
            }
            TextUtils.isEmpty(new_password) -> {
                et_new_password.setError("New Password is Required")
                false
            }
            TextUtils.isEmpty(confirm_new_password) -> {
                et_confirm_new_password.setError("Confirm New Password is required")
                false
            }
            new_password.length < 8 -> {
                et_new_password.setError("New Password must be 8 characters or more")
                false
            }
            !(new_password.equals(confirm_new_password)) -> {
                et_confirm_new_password.setError("Confirm New Password must be same as New Password")
                false
            }
            else -> {
                true
            }
        }
    }

}