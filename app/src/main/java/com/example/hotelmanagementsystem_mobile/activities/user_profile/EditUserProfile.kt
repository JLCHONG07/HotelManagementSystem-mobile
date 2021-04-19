package com.example.hotelmanagementsystem_mobile.activities.user_profile

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.BaseActivity
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.fragments.HomeFragment
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_edit_user_profile.*
import java.io.IOException

class EditUserProfile : BaseActivity() {

    private var mSelectedImageFileUri: Uri? = null
    private var mProfileImageUri: String = ""
    private lateinit var mUserDetails: User
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var IC: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_profile)
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Edit Profile"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)

        name = findViewById(R.id.et_current_password)
        email = findViewById(R.id.et_confirm_new_password)
        IC = findViewById(R.id.et_new_password)
        showProgressDialog("Loading...")
        FirestoreClass().loadUserData(this, HomeFragment())

        iv_profile_user_image.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Constants.showImageChooser(this)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }
        buttonUpdate.setOnClickListener {
            if (mSelectedImageFileUri != null) {
                uploadUserImage()
            } else {
                updateUserProfile()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Show Image chooser
                Constants.showImageChooser(this)
            }
        } else {
            Toast.makeText(
                this,
                "Oops, you just denied the permission for storage. You can also allow it from settings",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE && data!!.data != null) {
            mSelectedImageFileUri = data.data

            try {
                Glide
                    .with(this)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(iv_profile_user_image)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadUserImage() {
        showProgressDialog(resources.getString(R.string.please_wait))

        if (mSelectedImageFileUri != null) {
            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                "USER_IMAGE" + System.currentTimeMillis()
                        + "." + Constants.getFileExtension(this, mSelectedImageFileUri)
            )

            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener { taskSnapshot ->
                Log.i(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    Log.i("Downloadable Image URL", uri.toString())
                    mProfileImageUri = uri.toString()

                    //update User Profile data
                    updateUserProfile()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    this, exception.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun profileUpdateSuccess() {
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun updateUserProfile() {
        var userHashMap = HashMap<String, Any>()
        val name: String = et_current_password.text.toString().trim { it <= ' ' }
        val passportNumber: String = et_new_password.text.toString().trim { it <= ' ' }
        val email: String = et_confirm_new_password.text.toString().trim { it <= ' ' }

        if (validateForm(name, passportNumber, email)) {
            showProgressDialog(resources.getString(R.string.please_wait))
            if (mProfileImageUri.isNotEmpty() && mProfileImageUri != mUserDetails.image) {

                userHashMap[Constants.IMAGE] = mProfileImageUri
            }
            userHashMap[Constants.NAME] = name
            userHashMap[Constants.PASSPORT] = passportNumber
            userHashMap[Constants.EMAIL] = email
        }
        FirestoreClass().updateUserProfileData(this, userHashMap)

    }

    private fun validateForm(name: String, passportNumber: String, email: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                et_current_password.setError("Username is Required")
                false
            }
            TextUtils.isEmpty(passportNumber) -> {
                et_new_password.setError("IC/Passport Number is Required")
                false
            }
            TextUtils.isEmpty(email) -> {
                et_confirm_new_password.setError("Email is required")
                false
            }
            else -> {
                true
            }
        }
    }

    fun getUserDetails(user: User) {
        mUserDetails = user

        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(iv_profile_user_image)

        name.setText(user.name)
        email.setText(user.email)
        IC.setText(user.passportNumber)
        hideProgressDialog()
    }
}
