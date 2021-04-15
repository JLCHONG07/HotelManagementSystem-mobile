package com.example.hotelmanagementsystem_mobile.firebase

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.hotelmanagementsystem_mobile.activities.Homepage
import com.example.hotelmanagementsystem_mobile.activities.Login
import com.example.hotelmanagementsystem_mobile.activities.Signup
import com.example.hotelmanagementsystem_mobile.fragments.HomeFragment
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject

class FirestoreClass {
    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: Signup, userInfo: User) {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisterSuccess()
            }.addOnFailureListener{
                    e ->
                Log.e(activity.javaClass.simpleName, "Error register user !")
            }
    }

    fun loadUserData(activity: Activity, fragment: Fragment) {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)!!

                when(activity) {
                    is Login -> {
                        if(loggedInUser != null) {
                            activity.signInSuccess()
                        }
                    }
                }

                when(fragment) {
                    is HomeFragment -> {
                        if(loggedInUser != null) {
                            fragment.updateUserDetails(loggedInUser)
                        }
                    }
                }
            }.addOnFailureListener{
                    e ->
                when(activity) {
                    is Login -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName, "Error register user !")
            }
    }

    fun getCurrentUserId() : String {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""

        if(currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }
}