package com.example.hotelmanagementsystem_mobile.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.adapters.CheckOutHistoryDetailsAdapter
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.models.booking_details.BookingDetails
import com.example.hotelmanagementsystem_mobile.utils.Constants
import kotlinx.android.synthetic.main.activity_check_out_history.*

class CheckOutHistoryActivity : BaseActivity() {

    private lateinit var mUserDetail : User
    private var bookingDetails : ArrayList<BookingDetails> = ArrayList()

    private lateinit var checkOutHistoryDetailsDialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out_history)

        if(intent.hasExtra(Constants.USERS)) {
            mUserDetail = intent.getParcelableExtra(Constants.USERS)
        }
        showProgressDialog(resources.getString(R.string.please_wait))
        getCheckOutDetails()
    }

    fun successfulGetCheckedOutDetails(checkOutDetails : ArrayList<BookingDetails>) {
        hideProgressDialog()
        updateCheckOutDetailsUI(checkOutDetails)
    }

    private fun updateCheckOutDetailsUI(UpdatedBookingDetailsList: ArrayList<BookingDetails>) {
        bookingDetails = UpdatedBookingDetailsList

        rv_check_out_history.visibility = View.VISIBLE
        rv_check_out_history.layoutManager = LinearLayoutManager(this)
        rv_check_out_history.setHasFixedSize(true)

        val adapter = CheckOutHistoryDetailsAdapter(this, bookingDetails, mUserDetail)
        rv_check_out_history.adapter = adapter
    }

    private fun getCheckOutDetails() {
        FirestoreClass().getCheckedInDetails(this, Constants.BOOKING_DETAILS)
    }

    fun showCheckOutHistoryDetailsDialog(position: Int) {
        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.check_out_history_details_dialog_view, null)
        var textNights =
            "${bookingDetails[position].room_reservation_details[0].numberOfDays} Days, ${bookingDetails[position].room_reservation_details[0].numberOfNights} Nights"
        var textReservationDetails =
            "${bookingDetails[position].room_reservation_details[0].numberOfRooms} Rooms, ${bookingDetails[position].room_reservation_details[0].numberOfGuests} Guests"

        val textViewReservationNumber =
            dialogView.findViewById<TextView>(R.id.check_out_history_details_dialog_reservation_number)
        textViewReservationNumber?.text = bookingDetails[position].reservationID

        val textViewNights =
            dialogView.findViewById<TextView>(R.id.check_out_history_details_dialog_nights)
        textViewNights?.text = textNights

        val textViewReservationDetails =
            dialogView.findViewById<TextView>(R.id.check_out_history_details_dialog_reservation_details)
        textViewReservationDetails?.text = textReservationDetails

        val textViewRoomTypes =
            dialogView.findViewById<TextView>(R.id.check_out_history_details_dialog_room_types)
        textViewRoomTypes?.text = bookingDetails[position].room_reservation_details[0].roomTypes

        val textViewDateAndTime =
            dialogView.findViewById<TextView>(R.id.check_out_history_details_dialog_date_time)
        textViewDateAndTime?.text = bookingDetails[position].check_in_details[0].checkInDateAndTime

        val textViewCheckInId =
            dialogView.findViewById<TextView>(R.id.check_out_history_details_dialog_check_in_id)
        textViewCheckInId?.text = bookingDetails[position].check_in_details[0].checkInID

        val textViewReservationDateRange =
            dialogView.findViewById<TextView>(R.id.check_out_history_details_dialog_date_range)
        textViewReservationDateRange.text = bookingDetails[position].room_reservation_details[0].reservationDateTime

        val textViewCheckOutUserName =
            dialogView.findViewById<TextView>(R.id.check_out_history_details_dialog_user_name)
        textViewCheckOutUserName.text = mUserDetail.name

        val textViewCheckOutID = dialogView.findViewById<TextView>(R.id.check_out_history_details_dialog_check_out_id)
        textViewCheckOutID.text = bookingDetails[position].check_out_details[0].checkOutID

        val checkInDetailsDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        checkInDetailsDialogBuilder.setView(dialogView)
        checkOutHistoryDetailsDialog = checkInDetailsDialogBuilder.create()
        checkOutHistoryDetailsDialog.show()
    }
}