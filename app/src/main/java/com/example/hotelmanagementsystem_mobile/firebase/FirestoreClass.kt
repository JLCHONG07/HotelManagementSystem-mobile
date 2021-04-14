package com.example.hotelmanagementsystem_mobile.firebase

import android.app.Activity
import android.util.Log
import android.widget.TextView
import com.example.hotelmanagementsystem_mobile.activities.Homepage
import com.example.hotelmanagementsystem_mobile.activities.Login
import com.example.hotelmanagementsystem_mobile.activities.Signup
import com.example.hotelmanagementsystem_mobile.models.TimeSlot
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import org.w3c.dom.Text

class FirestoreClass {
    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: Signup, userInfo: User) {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisterSuccess()
            }.addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error register user !")
            }
    }

    fun loadUserData(activity: Activity) {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)!!

                when (activity) {
                    is Login -> {
                        if (loggedInUser != null) {
                            activity.signInSuccess()
                        }
                    }

                    is Homepage -> {
                        activity.updateUserDetails(loggedInUser)
                    }
                }
            }.addOnFailureListener { e ->
                when (activity) {
                    is Login -> {
                        activity.hideProgressDialog()
                    }

                    is Homepage -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName, "Error register user !")
            }
    }


    fun getCurrentUserId(): String {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""

        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    //below here is facilities_booking
    fun saveBookedData(selectedTimeSlot: String?, selectedTime: String?, selectedDate: String?) {
        val db = FirebaseFirestore.getInstance()
        val timeSlot: MutableMap<String, Any> = HashMap()

        timeSlot["timerID"] = "timeID0$selectedTimeSlot"
        timeSlot["timer"] = "$selectedTime"

        db.collection("facilities_booking").document("badminton").collection("court")
            .document("court_1").collection("$selectedDate").document().set(timeSlot)

            .addOnSuccessListener {
                Log.d("status", "successful added")
            }
            .addOnFailureListener {
                Log.d("status", "fail added")
            }


    }

    fun retriveBookedData(selectedDate: String?) {
        val db = FirebaseFirestore.getInstance()
        db.collection("facilities_booking").document("badminton").collection("court")
            .document("court_1").collection("$selectedDate")

            .get()
            .addOnSuccessListener {
                val alSlotAvailable: ArrayList<TimeSlot> = ArrayList()

                for (document in it.documents.indices) {


                    alSlotAvailable.add(
                        TimeSlot(
                            it.documents[document].data!!["timerID"] as String,
                            it.documents[document].data!!["timer"] as String
                        )
                    )

                }

                for (document in alSlotAvailable.indices) {
                    Log.d("timerID", alSlotAvailable[document].timerID)
                    Log.d("timer", alSlotAvailable[document].timer)

                }

            }
            .addOnFailureListener { exception ->
                Log.d("Error", exception.toString())
            }
    }


}