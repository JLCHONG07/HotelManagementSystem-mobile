package com.example.hotelmanagementsystem_mobile.activities.admin

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.BaseActivity
import com.example.hotelmanagementsystem_mobile.adapters.admin.AdminCheckInDetailsAdapter
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.models.booking_details.BookingDetails
import kotlinx.android.synthetic.main.activity_admin_check_in_details.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AdminCheckInDetailsActivity : BaseActivity(), CoroutineScope {

    private lateinit var todayBookingDetails: ArrayList<BookingDetails>
    private lateinit var otherDayBookingDetails: ArrayList<BookingDetails>

    private var job: Job = Job()
    private lateinit var userDetails: User
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    private lateinit var checkInDetailsAlertDialog : AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_check_in_details)

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getTodayOtherDayCheckInDetails(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    fun successfulGetCheckInDetails(
        todayCheckInDetails: ArrayList<BookingDetails>,
        otherDayCheckInDetails: ArrayList<BookingDetails>
    ) {
        hideProgressDialog()

        val todayUserDetailsList: ArrayList<User> = ArrayList()
        val otherDayUserDetailsList: ArrayList<User> = ArrayList()

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
            updateAdminCheckInDetailsOtherDay(otherDayCheckInDetails, otherDayUserDetailsList)
            updateAdminCheckInDetailsToday(todayCheckInDetails, todayUserDetailsList)
        }
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

        val checkInDetailsDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        checkInDetailsDialogBuilder.setView(dialogView)
        checkInDetailsAlertDialog = checkInDetailsDialogBuilder.create()
        checkInDetailsAlertDialog.show()
    }

    private fun updateAdminCheckInDetailsToday(
        UpdatedBookingDetailsList: ArrayList<BookingDetails>,
        AlignUserDetails: ArrayList<User>
    ) {
        tv_admin_today_total.text = UpdatedBookingDetailsList.size.toString()
        todayBookingDetails = UpdatedBookingDetailsList

        rv_admin_today_check_in_details.visibility = View.VISIBLE
        rv_admin_today_check_in_details.layoutManager = LinearLayoutManager(this)
        rv_admin_today_check_in_details.setHasFixedSize(true)

        val adapter = AdminCheckInDetailsAdapter(this, todayBookingDetails, AlignUserDetails, 0)
        rv_admin_today_check_in_details.adapter = adapter
    }

    private fun updateAdminCheckInDetailsOtherDay(
        UpdatedBookingDetailsList: ArrayList<BookingDetails>,
        AlignUserDetails: ArrayList<User>
    ) {
        tv_admin_other_day_total.text = UpdatedBookingDetailsList.size.toString()
        otherDayBookingDetails = UpdatedBookingDetailsList

        rv_admin_other_day_check_in_details.visibility = View.VISIBLE
        rv_admin_other_day_check_in_details.layoutManager = LinearLayoutManager(this)
        rv_admin_other_day_check_in_details.setHasFixedSize(true)

        val adapter = AdminCheckInDetailsAdapter(this, otherDayBookingDetails, AlignUserDetails, 1)
        rv_admin_other_day_check_in_details.adapter = adapter
    }
}