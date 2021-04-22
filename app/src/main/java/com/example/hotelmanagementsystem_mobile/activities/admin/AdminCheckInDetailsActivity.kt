package com.example.hotelmanagementsystem_mobile.activities.admin

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.BaseActivity
import com.example.hotelmanagementsystem_mobile.adapters.admin.AdminCheckInDetailsAdapter
import com.example.hotelmanagementsystem_mobile.adapters.admin.AdminCheckOutDetailsAdapter
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.models.booking_details.BookingDetails
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_admin_check_in_details.*
import kotlinx.android.synthetic.main.activity_admin_check_out_details.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AdminCheckInDetailsActivity : BaseActivity(), CoroutineScope {

    private lateinit var todayBookingDetails: ArrayList<BookingDetails>
    private lateinit var otherDayBookingDetails: ArrayList<BookingDetails>
    private lateinit var searchResultCheckInDetails : ArrayList<BookingDetails>

    private var job: Job = Job()
    private lateinit var userDetails: User
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    private lateinit var checkInDetailsAlertDialog : AlertDialog


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_check_in_details)

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getTodayOtherDayCheckInDetails(this,"")

        val textInputCheckInID = findViewById<TextInputEditText>(R.id.tiet_admin_check_in_search)

        btn_admin_check_in_search.setOnClickListener {
            val userInput = textInputCheckInID.text.toString()
            if(validateInput(userInput)) {
                showProgressDialog(resources.getString(R.string.please_wait))
                FirestoreClass().getTodayOtherDayCheckInDetails(this, userInput)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    fun successfulGetCheckInDetails(
        todayCheckInDetails: ArrayList<BookingDetails>,
        otherDayCheckInDetails: ArrayList<BookingDetails>,
        searchResultCheckInDetails : ArrayList<BookingDetails>
    ) {
        val todayUserDetailsList: ArrayList<User> = ArrayList()
        val otherDayUserDetailsList: ArrayList<User> = ArrayList()
        val searchResultUserList = ArrayList<User>()

        launch {
            for (todayResult in todayCheckInDetails) {
                userDetails =
                    FirestoreClass().getUserDetailsInAdmin(todayResult.checkedInUser[0])
                        .toObject(User::class.java)!!
                Log.i("Get Data", userDetails.toString())
                if (todayResult.checkedInUser[0] == userDetails.id) {
                    todayUserDetailsList.add(userDetails)
                }
            }

            for (otherDayResult in otherDayCheckInDetails) {
                userDetails =
                    FirestoreClass().getUserDetailsInAdmin(otherDayResult.checkedInUser[0])
                        .toObject(User::class.java)!!
                if (otherDayResult.checkedInUser[0] == userDetails.id) {
                    otherDayUserDetailsList.add(userDetails)
                }
            }

            for (searchResult in searchResultCheckInDetails) {
                userDetails =
                    FirestoreClass().getUserDetailsInAdmin(searchResult.checkedInUser[0])
                        .toObject(User::class.java)!!
                Log.i("Get Data", userDetails.toString())
                if (searchResult.checkedInUser[0] == userDetails.id) {
                    searchResultUserList.add(userDetails)
                }
            }

            updateAdminCheckInDetailsOtherDay(otherDayCheckInDetails, otherDayUserDetailsList)
            updateAdminCheckInDetailsToday(todayCheckInDetails, todayUserDetailsList)
            updateAdminCheckInDetailsResult(searchResultCheckInDetails, searchResultUserList)
        }
        hideProgressDialog()
    }

    fun showCheckInDetailsDialog(bookingDetails : BookingDetails, userDetails : User) {
        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.check_in_details_dialog_view, null)
        var textNights =
            "${bookingDetails.room_reservation_details[0].numberOfDays} Days, ${bookingDetails.room_reservation_details[0].numberOfNights} Nights"
        var textReservationDetails =
            "${bookingDetails.room_reservation_details[0].numberOfRooms} Rooms, ${bookingDetails.room_reservation_details[0].numberOfGuests} Guests"

        val textViewReservationNumber =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_reservation_number)
        textViewReservationNumber?.text = bookingDetails.reservationID

        val textViewNights =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_nights)
        textViewNights?.text = textNights

        val textViewReservationDetails =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_reservation_details)
        textViewReservationDetails?.text = textReservationDetails

        val textViewRoomTypes =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_room_types)
        textViewRoomTypes?.text = bookingDetails.room_reservation_details[0].roomTypes

        val textViewDateAndTime =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_date_time)
        textViewDateAndTime?.text = bookingDetails.check_in_details[0].checkInDateAndTime

        val textViewCheckInId =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_check_in_id)
        textViewCheckInId?.text = bookingDetails.check_in_details[0].checkInID

        val textViewReservationDateRange =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_date_range)
        textViewReservationDateRange.text = bookingDetails.room_reservation_details[0].reservationDateTime

        val textViewCheckInUserName = dialogView.findViewById<TextView>(R.id.check_in_details_dialog_user_name)
        textViewCheckInUserName.text = userDetails.name

        val checkInDetailsDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        checkInDetailsDialogBuilder.setView(dialogView)
        checkInDetailsAlertDialog = checkInDetailsDialogBuilder.create()
        checkInDetailsAlertDialog.show()
    }

    private fun validateInput(checkInID : String) : Boolean {
        return when {
            TextUtils.isEmpty(checkInID) -> {
                false
            } else -> {
                true
            }
        }
    }

    private fun updateAdminCheckInDetailsToday(
        UpdatedBookingDetailsList: ArrayList<BookingDetails>,
        AlignUserDetails: ArrayList<User>
    ) {
        if(UpdatedBookingDetailsList.size <= 0) {
            admin_check_in_today.visibility = View.GONE
            tv_admin_today_total.visibility = View.GONE
            rv_admin_today_check_in_details.visibility = View.GONE
        }
        else {
            admin_check_in_today.visibility = View.VISIBLE
            tv_admin_today_total.visibility = View.VISIBLE
            rv_admin_today_check_in_details.visibility = View.VISIBLE

            tv_admin_search_result_total.visibility = View.GONE
            admin_check_in_result.visibility = View.GONE
            rv_admin_result_check_in_details.visibility = View.GONE
        }

        tv_admin_today_total.text = UpdatedBookingDetailsList.size.toString()
        todayBookingDetails = UpdatedBookingDetailsList

        rv_admin_today_check_in_details.layoutManager = LinearLayoutManager(this)
        rv_admin_today_check_in_details.setHasFixedSize(true)

        val adapter = AdminCheckInDetailsAdapter(this, todayBookingDetails, AlignUserDetails, 0)
        rv_admin_today_check_in_details.adapter = adapter
    }

    private fun updateAdminCheckInDetailsOtherDay(
        UpdatedBookingDetailsOtherDayList: ArrayList<BookingDetails>,
        AlignUserDetails: ArrayList<User>
    ) {
        if(UpdatedBookingDetailsOtherDayList.size <= 0) {
            admin_check_in_other_day.visibility = View.GONE
            tv_admin_other_day_total.visibility = View.GONE
            rv_admin_other_day_check_in_details.visibility = View.GONE
        }
        else {
            admin_check_in_other_day.visibility = View.VISIBLE
            tv_admin_other_day_total.visibility = View.VISIBLE
            rv_admin_other_day_check_in_details.visibility = View.VISIBLE

            tv_admin_search_result_total.visibility = View.GONE
            admin_check_in_result.visibility = View.GONE
            rv_admin_result_check_in_details.visibility = View.GONE
        }

        tv_admin_other_day_total.text = UpdatedBookingDetailsOtherDayList.size.toString()
        otherDayBookingDetails = UpdatedBookingDetailsOtherDayList

        rv_admin_other_day_check_in_details.layoutManager = LinearLayoutManager(this)
        rv_admin_other_day_check_in_details.setHasFixedSize(true)

        val adapter = AdminCheckInDetailsAdapter(this, otherDayBookingDetails, AlignUserDetails, 1)
        rv_admin_other_day_check_in_details.adapter = adapter
    }

    private fun updateAdminCheckInDetailsResult(
        UpdatedBookingDetailsResultList: ArrayList<BookingDetails>,
        AlignResultUserDetails: ArrayList<User>
    ) {
        hideProgressDialog()

        if(UpdatedBookingDetailsResultList.size <= 0) {
            tv_admin_search_result_total.visibility = View.GONE
            admin_check_in_result.visibility = View.GONE
            rv_admin_result_check_in_details.visibility = View.GONE
        }
        else {
            tv_admin_search_result_total.visibility = View.VISIBLE
            admin_check_in_result.visibility = View.VISIBLE
            rv_admin_result_check_in_details.visibility = View.VISIBLE

            tv_admin_today_total.visibility = View.GONE
            tv_admin_other_day_total.visibility = View.GONE
            admin_check_in_other_day.visibility = View.GONE
            admin_check_in_today.visibility = View.GONE
            rv_admin_today_check_in_details.visibility = View.GONE
            rv_admin_other_day_check_in_details.visibility = View.GONE
        }

        tv_admin_search_result_total.text = UpdatedBookingDetailsResultList.size.toString()
        searchResultCheckInDetails = UpdatedBookingDetailsResultList

        rv_admin_result_check_in_details.layoutManager = LinearLayoutManager(this)
        rv_admin_result_check_in_details.setHasFixedSize(true)

        val adapter = AdminCheckInDetailsAdapter(this, searchResultCheckInDetails, AlignResultUserDetails, 2)
        rv_admin_result_check_in_details.adapter = adapter
    }
}