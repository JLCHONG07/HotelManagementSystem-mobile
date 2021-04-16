package com.example.hotelmanagementsystem_mobile.firebase

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.hotelmanagementsystem_mobile.activities.Homepage
import com.example.hotelmanagementsystem_mobile.activities.Login
import com.example.hotelmanagementsystem_mobile.activities.Signup
import com.example.hotelmanagementsystem_mobile.activities.facilities_booking.BookingAvailable
import com.example.hotelmanagementsystem_mobile.fragments.HomeFragment
import com.example.hotelmanagementsystem_mobile.models.TimeSlot
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import java.sql.Time

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

    fun loadUserData(activity: Activity, fragment: Fragment) {
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
                }

                when (fragment) {
                    is HomeFragment -> {
                        if (loggedInUser != null) {
                            fragment.updateUserDetails(loggedInUser)
                        }
                    }
                }
            }.addOnFailureListener { e ->
                when (activity) {
                    is Login -> {
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
    //add booked data to database
    fun saveBookedData(
        selectedTimeSlot: Long?, selectedTime: String?, selectedDate: String?,
        selectedRoomCourt: String?,
        categories: String?,
        type: String?,
    ) {

        val timeSlot: MutableMap<String, Any> = HashMap()

        val selectedTimeSlotID = BookingAvailable().formatID(selectedTimeSlot)
        timeSlot["timerID"] = "timeID$selectedTimeSlotID"
        timeSlot["timer"] = "$selectedTime"

        val court = "$type $selectedRoomCourt"

        mFirestore.collection("facilities_booking").document("$categories").collection("$type")
            .document(court).collection("$selectedDate").document().set(timeSlot)

            .addOnSuccessListener {
                Log.d("status", "successful added")
            }
            .addOnFailureListener {
                Log.d("status", "fail added")
            }


    }

    //read data from database
    fun retrieveBookedData(
        activity: BookingAvailable,
        selectedDate: String?,
        selectedRoomCourt: String?,
        categories: String?,
        type: String?
    ) {

        val court = "$type $selectedRoomCourt"
        mFirestore.collection("facilities_booking").document("$categories").collection("$type")
            .document(court).collection("$selectedDate")

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


                activity.checkSlotAvailable(alSlotAvailable)

                //for (document in alSlotAvailable.indices) {
                 //   Log.d("timerID", alSlotAvailable[document].timerID)
                 //   Log.d("timer", alSlotAvailable[document].timer)

               // }

            }
            .addOnFailureListener { exception ->
                Log.d("Error", exception.toString())
            }

    }
}