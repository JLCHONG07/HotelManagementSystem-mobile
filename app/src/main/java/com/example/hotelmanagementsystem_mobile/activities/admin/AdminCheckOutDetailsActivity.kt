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
import com.example.hotelmanagementsystem_mobile.adapters.admin.AdminCheckOutDetailsAdapter
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.models.booking_details.BookingDetails
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_admin_check_out_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AdminCheckOutDetailsActivity : BaseActivity(), CoroutineScope {

    private lateinit var todayBookingDetails: ArrayList<BookingDetails>
    private lateinit var otherDayBookingDetails: ArrayList<BookingDetails>
    private lateinit var searchResultCheckOutDetails : ArrayList<BookingDetails>

    private var job: Job = Job()
    private lateinit var userDetails: User
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    private lateinit var checkOutDetailsAlertDialog : AlertDialog

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_check_out_details)

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getTodayOtherDayCheckOutDetails(this, "")

        val textInputCheckOutID = findViewById<TextInputEditText>(R.id.tiet_admin_check_out_search)

        btn_admin_check_out_search.setOnClickListener {
            val userInput = textInputCheckOutID.text.toString()
            if(validateInput(userInput)) {
                showProgressDialog(resources.getString(R.string.please_wait))
                FirestoreClass().getTodayOtherDayCheckOutDetails(this, userInput)
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

    fun successfulGetCheckOutDetails(
        todayCheckOutDetails: ArrayList<BookingDetails>,
        otherDayCheckOutDetails: ArrayList<BookingDetails>,
        searchResultCheckOutDetails : ArrayList<BookingDetails>
    ) {
        val todayUserDetailsList: ArrayList<User> = ArrayList()
        val otherDayUserDetailsList: ArrayList<User> = ArrayList()
        val searchResultUserList = ArrayList<User>()

        launch {
            for (todayResult in todayCheckOutDetails) {
                userDetails =
                    FirestoreClass().getUserDetailsInAdmin(todayResult.checkedInUser[0])
                        .toObject(User::class.java)!!
                Log.i("Get Data", userDetails.toString())
                if (todayResult.checkedInUser[0] == userDetails.id) {
                    todayUserDetailsList.add(userDetails)
                }
            }

            for (otherDayResult in otherDayCheckOutDetails) {
                userDetails =
                    FirestoreClass().getUserDetailsInAdmin(otherDayResult.checkedInUser[0])
                        .toObject(User::class.java)!!
                if (otherDayResult.checkedInUser[0] == userDetails.id) {
                    otherDayUserDetailsList.add(userDetails)
                }
            }

            for (searchResult in searchResultCheckOutDetails) {
                userDetails =
                    FirestoreClass().getUserDetailsInAdmin(searchResult.checkedInUser[0])
                        .toObject(User::class.java)!!
                Log.i("Get Data", userDetails.toString())
                if (searchResult.checkedInUser[0] == userDetails.id) {
                    searchResultUserList.add(userDetails)
                }
            }
            updateAdminCheckOutDetailsOtherDay(otherDayCheckOutDetails, otherDayUserDetailsList)
            updateAdminCheckOutDetailsToday(todayCheckOutDetails, todayUserDetailsList)
            updateAdminCheckOutDetailsResult(searchResultCheckOutDetails, searchResultUserList)
        }
        hideProgressDialog()
    }

    fun showCheckOutDetailsDialog(bookingDetails : BookingDetails, userDetails : User) {
        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.check_out_history_details_dialog_view, null)
        var textNights =
            "${bookingDetails.room_reservation_details[0].numberOfDays} Days, ${bookingDetails.room_reservation_details[0].numberOfNights} Nights"
        var textReservationDetails =
            "${bookingDetails.room_reservation_details[0].numberOfRooms} Rooms, ${bookingDetails.room_reservation_details[0].numberOfGuests} Guests"

        val textViewReservationNumber =
            dialogView.findViewById<TextView>(R.id.check_out_history_details_dialog_reservation_number)
        textViewReservationNumber?.text = bookingDetails.reservationID

        val textViewNights =
            dialogView.findViewById<TextView>(R.id.check_out_history_details_dialog_nights)
        textViewNights?.text = textNights

        val textViewReservationDetails =
            dialogView.findViewById<TextView>(R.id.check_out_history_details_dialog_reservation_details)
        textViewReservationDetails?.text = textReservationDetails

        val textViewRoomTypes =
            dialogView.findViewById<TextView>(R.id.check_out_history_details_dialog_room_types)
        textViewRoomTypes?.text = bookingDetails.room_reservation_details[0].roomTypes

        val textViewDateAndTime =
            dialogView.findViewById<TextView>(R.id.check_out_history_details_dialog_date_time)
        textViewDateAndTime?.text = bookingDetails.check_in_details[0].checkInDateAndTime

        val textViewCheckInId =
            dialogView.findViewById<TextView>(R.id.check_out_history_details_dialog_check_in_id)
        textViewCheckInId?.text = bookingDetails.check_in_details[0].checkInID

        val textViewReservationDateRange =
            dialogView.findViewById<TextView>(R.id.check_out_history_details_dialog_date_range)
        textViewReservationDateRange.text = bookingDetails.room_reservation_details[0].reservationDateTime

        val textViewCheckOutUserName =
            dialogView.findViewById<TextView>(R.id.check_out_history_details_dialog_user_name)
        textViewCheckOutUserName.text = userDetails.name

        val textViewCheckOutID = dialogView.findViewById<TextView>(R.id.check_out_history_details_dialog_check_out_id)
        textViewCheckOutID.text = bookingDetails.check_out_details[0].checkOutID

        val checkOutDetailsDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        checkOutDetailsDialogBuilder.setView(dialogView)
        checkOutDetailsAlertDialog = checkOutDetailsDialogBuilder.create()
        checkOutDetailsAlertDialog.show()
    }

    private fun validateInput(checkOutID : String) : Boolean {
        return when {
            TextUtils.isEmpty(checkOutID) -> {
                false
            } else -> {
                true
            }
        }
    }

    private fun updateAdminCheckOutDetailsToday(
        UpdatedBookingDetailsTodayList: ArrayList<BookingDetails>,
        AlignTodayUserDetails: ArrayList<User>
    ) {
        if(UpdatedBookingDetailsTodayList.size <= 0) {
            admin_check_out_today.visibility = View.GONE
            tv_admin_today_total_check_out.visibility = View.GONE
            rv_admin_today_check_out_details.visibility = View.GONE
        }
        else {
            admin_check_out_today.visibility = View.VISIBLE
            tv_admin_today_total_check_out.visibility = View.VISIBLE
            rv_admin_today_check_out_details.visibility = View.VISIBLE

            tv_admin_search_total_result.visibility = View.GONE
            admin_check_out_result.visibility = View.GONE
            rv_admin_check_out_details_result.visibility = View.GONE
        }

        tv_admin_today_total_check_out.text = UpdatedBookingDetailsTodayList.size.toString()
        todayBookingDetails = UpdatedBookingDetailsTodayList

        rv_admin_today_check_out_details.layoutManager = LinearLayoutManager(this)
        rv_admin_today_check_out_details.setHasFixedSize(true)

        val adapter = AdminCheckOutDetailsAdapter(this, todayBookingDetails, AlignTodayUserDetails)
        rv_admin_today_check_out_details.adapter = adapter
    }

    private fun updateAdminCheckOutDetailsOtherDay(
        UpdatedBookingDetailsOtherDayList: ArrayList<BookingDetails>,
        AlignOtherDayUserDetails: ArrayList<User>
    ) {
        if(UpdatedBookingDetailsOtherDayList.size <= 0) {
            admin_check_out_other_day.visibility = View.GONE
            tv_admin_other_day_total_check_out.visibility = View.GONE
            rv_admin_other_day_check_out_details.visibility = View.GONE
        }
        else {
            admin_check_out_other_day.visibility = View.VISIBLE
            tv_admin_other_day_total_check_out.visibility = View.VISIBLE
            rv_admin_other_day_check_out_details.visibility = View.VISIBLE

            tv_admin_search_total_result.visibility = View.GONE
            admin_check_out_result.visibility = View.GONE
            rv_admin_check_out_details_result.visibility = View.GONE
        }

        tv_admin_other_day_total_check_out.text = UpdatedBookingDetailsOtherDayList.size.toString()
        otherDayBookingDetails = UpdatedBookingDetailsOtherDayList

        rv_admin_other_day_check_out_details.layoutManager = LinearLayoutManager(this)
        rv_admin_other_day_check_out_details.setHasFixedSize(true)

        val adapter = AdminCheckOutDetailsAdapter(this, otherDayBookingDetails, AlignOtherDayUserDetails)
        rv_admin_other_day_check_out_details.adapter = adapter
    }

    private fun updateAdminCheckOutDetailsResult(
        UpdatedBookingDetailsResultList: ArrayList<BookingDetails>,
        AlignResultUserDetails: ArrayList<User>
    ) {
        hideProgressDialog()

        if(UpdatedBookingDetailsResultList.size <= 0) {
            tv_admin_search_total_result.visibility = View.GONE
            admin_check_out_result.visibility = View.GONE
            rv_admin_check_out_details_result.visibility = View.GONE
        }
        else {
            tv_admin_search_total_result.visibility = View.VISIBLE
            admin_check_out_result.visibility = View.VISIBLE
            rv_admin_check_out_details_result.visibility = View.VISIBLE

            tv_admin_today_total_check_out.visibility = View.GONE
            tv_admin_other_day_total_check_out.visibility = View.GONE
            admin_check_out_other_day.visibility = View.GONE
            admin_check_out_today.visibility = View.GONE
            rv_admin_today_check_out_details.visibility = View.GONE
            rv_admin_other_day_check_out_details.visibility = View.GONE
        }

        tv_admin_search_total_result.text = UpdatedBookingDetailsResultList.size.toString()
        searchResultCheckOutDetails = UpdatedBookingDetailsResultList

        rv_admin_check_out_details_result.layoutManager = LinearLayoutManager(this)
        rv_admin_check_out_details_result.setHasFixedSize(true)

        val adapter = AdminCheckOutDetailsAdapter(this, searchResultCheckOutDetails, AlignResultUserDetails)
        rv_admin_check_out_details_result.adapter = adapter
    }
}