package com.example.hotelmanagementsystem_mobile.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.CheckInActivity
import com.example.hotelmanagementsystem_mobile.activities.Homepage
import com.example.hotelmanagementsystem_mobile.activities.Login
import com.example.hotelmanagementsystem_mobile.activities.Signup
import com.example.hotelmanagementsystem_mobile.activities.facilities_booking.BookingAvailable
import com.example.hotelmanagementsystem_mobile.fragments.HomeFragment
import com.example.hotelmanagementsystem_mobile.models.TimeSlot
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.models.booking_details.BookingDetails
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

    //get, update booking details
    fun getBookingDetails(activity: CheckInActivity, collection_path: String, reservation_id : String) {
        mFirestore.collection(collection_path)
            .whereEqualTo(Constants.RESERVATION_ID, reservation_id)
            .get()
            .addOnSuccessListener {
                    document ->
                if(document.documents.isNotEmpty()) {
                    val bookingDetails =
                        document.documents[0].toObject(BookingDetails::class.java)!!
                    bookingDetails.bookingID = document.documents[0].id
                    Log.i(javaClass.simpleName, bookingDetails.toString())
                    activity.successfulGetBookingDetails(bookingDetails)
                } else {
                    activity.hideProgressDialog()
                    Toast.makeText(activity, "No records found.", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                    e ->
                activity.hideProgressDialog()
                Log.e(e.javaClass.simpleName, "Error while retrieve booking details!", e)
            }
    }

    fun updateBookingDetails(activity: CheckInActivity, collection_path: String, bookingDetailHashMap: HashMap<String, Any>, booking_details_id : String) {
        Log.i("Update Booking Details", collection_path)
        mFirestore.collection(collection_path)
            .document(booking_details_id)
            .update(bookingDetailHashMap)
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName, "Booking details update successfully")
                activity.successfulUpdateBookingDetails()
            }.addOnFailureListener {
                    exception ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while updating booking details", exception)
            }
    }

    fun getCheckedInDetails(activity: CheckInActivity, collection_path: String) {
        mFirestore.collection(collection_path)
            .whereArrayContains(Constants.CHECKED_IN_USER, getCurrentUserId())
            .get()
            .addOnSuccessListener {
                document ->
                Log.i("FirestoreClass", "Successful get checked in details")
                val checkedInDetails : ArrayList<BookingDetails> = ArrayList()

                for(result in document.documents) {
                    val details = result.toObject(BookingDetails::class.java)!!
                    checkedInDetails.add(details)
                }
                activity.successfulGetCheckedInDetails(checkedInDetails)
            }.addOnFailureListener {exception ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while get check in details", exception)
            }
    }

    //below here is facilities_booking
    //add booked data to database
    fun saveBookedData(
        selectedTimeSlot: Long?, selectedTime: String?, selectedDate: String?,
        selectedRoomCourt:String?,
        categories:String?,
        type:String?,
    ) {

        val timeSlot: MutableMap<String, Any> = HashMap()

        val selectedTimeSlotID= BookingAvailable().formatID(selectedTimeSlot)
        timeSlot["timerID"] = "timeID$selectedTimeSlotID"
        timeSlot["timer"] = "$selectedTime"

        val court= "$type $selectedRoomCourt"

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
        //val alSlotAvailable: ArrayList<TimeSlot> = ArrayList()
        // alSlotAvailable.clear()
        val timeSlot: MutableMap<String, Any> = HashMap()
        val court = "$type $selectedRoomCourt"
        mFirestore.collection("facilities_booking").document("$categories").collection("$type")
            .document(court).collection("$selectedDate")


            .get()
            .addOnSuccessListener {


                for (document in it.documents.indices) {


                    timeSlot.put(
                        it.documents[document].data!!["timerID"] as String,
                        it.documents[document].data!!["timer"] as String
                    )


                }


                //activity.checkSlotAvailable(alSlotAvailable)

                //for (document in alSlotAvailable.indices) {
                //   Log.d("timerID", alSlotAvailable[document].timerID)
                //   Log.d("timer", alSlotAvailable[document].timer)

                // }

            }
            .addOnCompleteListener{
                activity.checkSlotAvailable(timeSlot)

            }
            .addOnFailureListener { exception ->
                Log.d("Error", exception.toString())
            }


    }
}