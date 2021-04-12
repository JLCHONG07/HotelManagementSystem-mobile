package com.example.hotelmanagementsystem_mobile.firebase

import android.util.Log
import com.example.hotelmanagementsystem_mobile.activities.Login
import com.example.hotelmanagementsystem_mobile.activities.Signup
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

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

    fun getCurrentUserId() : String {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""

        if(currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }
}