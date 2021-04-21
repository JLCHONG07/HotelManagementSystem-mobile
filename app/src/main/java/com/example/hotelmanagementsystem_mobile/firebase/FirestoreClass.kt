package com.example.hotelmanagementsystem_mobile.firebase

import android.app.Activity
import android.os.Build
import android.text.format.DateUtils
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.hotelmanagementsystem_mobile.activities.*
import com.example.hotelmanagementsystem_mobile.activities.admin.AdminCheckInDetailsActivity
import com.example.hotelmanagementsystem_mobile.activities.facilities_booking.BookingAvailable
import com.example.hotelmanagementsystem_mobile.activities.facilities_booking.BookingHistory
import com.example.hotelmanagementsystem_mobile.activities.user_profile.EditUserProfile
import com.example.hotelmanagementsystem_mobile.fragments.AccountFragment
import com.example.hotelmanagementsystem_mobile.fragments.AdminAccountFragment
import com.example.hotelmanagementsystem_mobile.fragments.AdminHomeFragment
import com.example.hotelmanagementsystem_mobile.fragments.HomeFragment
import com.example.hotelmanagementsystem_mobile.models.BookFacilitiesHistory
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.models.booking_details.BookingDetails
import com.example.hotelmanagementsystem_mobile.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.time.milliseconds

class FirestoreClass {
    private val mFirestore = FirebaseFirestore.getInstance()

    //write a new user into firebase
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

    //get user information from firebase
    fun loadUserData(activity: Activity, fragment: Fragment) {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)!!
                when (activity) {
                    is Login -> {
                        if (loggedInUser != null) {
                            activity.signInSuccess(loggedInUser)
                        }
                    }
                    is EditUserProfile -> {
                        activity.getUserDetails(loggedInUser)
                    }
                }
                when (fragment) {
                    is HomeFragment -> {
                        fragment.updateUserDetails(loggedInUser)
                    }
                    is AdminHomeFragment -> {
                        fragment.updateUserDetails(loggedInUser)
                    }
                    is AccountFragment -> {
                        fragment.getUserDetails(loggedInUser)
                    }
                    is AdminAccountFragment -> {
                        fragment.getUserDetails(loggedInUser)
                    }
                }
            }.addOnFailureListener { e ->
                when (activity) {
                    is Login -> {
                        activity.hideProgressDialog()
                    }
                    is EditUserProfile -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName, "Error register user !")
            }
    }

    //update the user information in database
    fun updateUserProfileData(activity: EditUserProfile, userHashMap: HashMap<String, Any>) {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName, "Profile Data updated successfully")
                Toast.makeText(activity, "Profile updated successfully!", Toast.LENGTH_LONG)
                    .show()
                activity.profileUpdateSuccess()
            }.addOnFailureListener { exception ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating a board",
                    exception
                )
                Toast.makeText(activity, "Error when updating the profile!", Toast.LENGTH_LONG)
                    .show()
            }
    }

    //Get user details, return with snapshot
    suspend fun getUserDetailsInAdmin(user_id : String) : DocumentSnapshot{
        return mFirestore.collection(Constants.USERS)
            .document(user_id)
            .get()
            .await()
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
            .addOnSuccessListener { document ->
                if(document.documents.isNotEmpty()) {
                    val bookingDetails =
                        document.documents[0].toObject(BookingDetails::class.java)!!
                    //Check if user checked in
                    if(bookingDetails.status == "checkedin" && bookingDetails.checkedInUser.contains(getCurrentUserId())) {
                        Toast.makeText(activity, "You already checked in!", Toast.LENGTH_SHORT).show()
                        activity.userCheckedIn()
                    } else {
                        bookingDetails.bookingID = document.documents[0].id
                        Log.i(javaClass.simpleName, bookingDetails.toString())
                        activity.successfulGetBookingDetails(bookingDetails)
                    }
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
        mFirestore.collection(collection_path)
            .document(booking_details_id)
            .update(bookingDetailHashMap)
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Booking details update successfully")
                activity.successfulUpdateBookingDetails()
            }.addOnFailureListener {
                    exception ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while updating booking details", exception)
            }
    }

    fun getCheckedInDetails(activity: Activity, collection_path: String) {
        mFirestore.collection(collection_path)
            .whereArrayContains(Constants.CHECKED_IN_USER, getCurrentUserId())
            .get()
            .addOnSuccessListener {
                    document ->

                when(activity) {
                    is CheckInActivity -> {
                        Log.i("FirestoreClass", "Successful get checked in details")
                        val checkedInDetails : ArrayList<BookingDetails> = ArrayList()

                        for(result in document.documents) {
                            val details = result.toObject(BookingDetails::class.java)!!
                            if(details.status == "checkedin") {
                                checkedInDetails.add(details)
                            }
                        }
                        activity.successfulGetCheckedInDetails(checkedInDetails)
                    }

                    is CheckOutActivity -> {
                        val checkedInDetails : ArrayList<BookingDetails> = ArrayList()

                        for(result in document.documents) {
                            val details = result.toObject(BookingDetails::class.java)!!
                            if(details.status == "checkedin") {
                                checkedInDetails.add(details)
                            }
                        }
                        activity.successfulGetCheckedInDetails(checkedInDetails)
                    }

                    is CheckOutHistoryActivity -> {
                        val checkedInDetails : ArrayList<BookingDetails> = ArrayList()

                        for(result in document.documents) {
                            val details = result.toObject(BookingDetails::class.java)!!
                            if(details.status == "checkedout" && details.checkedInUser[0] == getCurrentUserId()) {
                                checkedInDetails.add(details)
                            }
                        }
                        activity.successfulGetCheckedOutDetails(checkedInDetails)
                    }
                }

            }.addOnFailureListener {exception ->
                when(activity) {
                    is CheckInActivity -> {
                        activity.hideProgressDialog()
                        Log.e(activity.javaClass.simpleName, "Error while get check in details", exception)
                    }

                    is CheckOutActivity -> {
                        activity.hideProgressDialog()
                        Log.e(activity.javaClass.simpleName, "Error while get check in details", exception)
                    }
                }
            }
    }

    fun updateCheckOutDetails(activity: CheckOutActivity, reservation_id : String, new_booking_details : HashMap<String, Any>) {
        mFirestore.collection(Constants.BOOKING_DETAILS)
            .document(reservation_id)
            .update(new_booking_details)
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Checkout details update successfully")
                activity.successfulUpdateCheckedOutDetails()
            }.addOnFailureListener {
                    exception ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while updating booking details", exception)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTodayOtherDayCheckInDetails(activity: AdminCheckInDetailsActivity) {
        mFirestore.collection(Constants.BOOKING_DETAILS)
            .whereEqualTo(Constants.STATUS, "checkedin")
            .get()
            .addOnSuccessListener {
                    document ->
                val todayBookingDetails = ArrayList<BookingDetails>()
                val otherDayBookingDetails = ArrayList<BookingDetails>()

                for(result in document.documents) {
                    val bookingDetails = result.toObject(BookingDetails::class.java)!!
                    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val c = Calendar.getInstance()
                    val datetime = LocalDateTime.parse(bookingDetails.check_in_details[0].checkInDateAndTime, format)
                    Log.i("Date", datetime.toString())
                    Log.i("Date", c.get(Calendar.DATE).toString())
                    Log.i("Date", c.time.month.toString())
                    Log.i("Date", c.get(Calendar.YEAR).toString())

                    if (c.get(Calendar.DAY_OF_MONTH) == datetime.dayOfMonth
                        && c.time.month == datetime.monthValue - 1
                        && c.get(Calendar.YEAR) == datetime.year) {
                        todayBookingDetails.add(bookingDetails)
                    }
                    else {
                        otherDayBookingDetails.add(bookingDetails)
                    }
                }

                activity.successfulGetCheckInDetails(todayBookingDetails, otherDayBookingDetails)
            }
            .addOnFailureListener {exception ->
                Log.e(activity.javaClass.simpleName, "Error while get all booking details !", exception)
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

            }
            .addOnCompleteListener{
                activity.checkSlotAvailable(timeSlot)

            }
            .addOnFailureListener { exception ->
                Log.d("Error", exception.toString())
            }


    }

    fun retrieveViewBookedData1(
        activity: AdminViewTimeSlots,
        selectedDate: String?,
        categories: String?,
        type: String?
    ) {

        val timeSlot: MutableMap<String, Any> = HashMap()
        val courtRoom1 = "$type 1"
        mFirestore.collection(Constants.FACILITIES_BOOKING).document("$categories").collection("$type")
            .document(courtRoom1).collection("$selectedDate")
            .get()
            .addOnSuccessListener {

                for (document in it.documents.indices) {

                    timeSlot.put(

                        it.documents[document].data!!["timerID"] as String,
                        it.documents[document].data!!["timer"] as String
                    )
                }
            }

            .addOnCompleteListener {

                activity.getDataList1(timeSlot)
            }

            .addOnFailureListener { exception ->

                Log.d("Error", exception.toString())

            }

    }

    fun retrieveViewBookedData2(
        activity: AdminViewTimeSlots,
        selectedDate: String?,
        categories: String?,
        type: String?
    ) {

        val timeSlot: MutableMap<String, Any> = HashMap()
        val courtRoom2 = "$type 2"
        mFirestore.collection(Constants.FACILITIES_BOOKING).document("$categories").collection("$type")
            .document(courtRoom2).collection("$selectedDate")
            .get()
            .addOnSuccessListener {

                for (document in it.documents.indices) {

                    timeSlot.put(

                        it.documents[document].data!!["timerID"] as String,
                        it.documents[document].data!!["timer"] as String
                    )
                }
            }
            .addOnCompleteListener {
                activity.setDataList2(timeSlot)
            }
            .addOnFailureListener { exception ->
                Log.d("Error", exception.toString())
            }

    }

    fun history(
        userID: String,
        time:String,
        courtRoom: String,
        weekOfDay: String,
        date: String,
        categories:String,
        catAndDuration:String,
        cvtMonth:String,
        savedDate:String
    ) {

        val historyData: MutableMap<String, Any> = HashMap()
        historyData["userID"] = userID
        historyData["time"] = time
        historyData["courtRoom"] = courtRoom
        historyData["weekOfDay"] = weekOfDay
        historyData["date"] = date
        historyData["categories"] = categories
        historyData["catAndDuration"] = catAndDuration
        historyData["cvtMonth"] = cvtMonth
        historyData["savedDate"] = savedDate

        mFirestore.collection(Constants.BOOKING_HISTORY).document(userID).collection("bookingID").document()
            .set(historyData)
            .addOnSuccessListener {
                Log.d("status", "successful added History")
            }
            .addOnFailureListener {
                Log.d("status", "fail added History")
            }

    }

    fun retriveBookedHistory(activity: BookingHistory, userID: String) {

        val bookFacilitiesHistory: ArrayList<BookFacilitiesHistory> = ArrayList()
        mFirestore.collection(Constants.BOOKING_HISTORY).document(userID).collection(Constants.BOOKING_ID)
            .get()
            .addOnSuccessListener {


                for (document in it.documents.indices) {

                    bookFacilitiesHistory.add(
                        BookFacilitiesHistory(
                            0,
                            it.documents[document].data!!.get("catAndDuration") as String,
                            it.documents[document].data!!.get("time") as String,
                            it.documents[document].data!!.get("courtRoom") as String,
                            0,
                            it.documents[document].data!!.get("weekOfDay") as String,
                            it.documents[document].data!!.get("savedDate") as String,
                            it.documents[document].data!!.get("cvtMonth") as String,
                            it.documents[document].data!!.get("categories") as String,
                            it.documents[document].data!!.get("date") as String,

                        )
                    )

                }
            }
            .addOnCompleteListener{
                activity.retrievedBookedHistory(bookFacilitiesHistory)
            }

    }
}