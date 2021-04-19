package com.example.hotelmanagementsystem_mobile.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.adapters.CheckOutDetailsAdapter
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.models.booking_details.BookingDetails
import com.example.hotelmanagementsystem_mobile.utils.Constants
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_check_out.*

class CheckOutActivity : BaseActivity() {

    private lateinit var checkOutDetailsAlertDialog : AlertDialog

    private var bookingDetails : ArrayList<BookingDetails> = ArrayList()
    private lateinit var mUserDetail : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        if(intent.hasExtra(Constants.USERS)) {
            mUserDetail = intent.getParcelableExtra(Constants.USERS)
        }

        showProgressDialog(resources.getString(R.string.please_wait))
        getCheckedInDetails()
    }

    fun successfulGetCheckedInDetails(checkInDetailsList: ArrayList<BookingDetails>) {
        hideProgressDialog()
        updateBookingDetailsUI(checkInDetailsList)
    }

    private fun getCheckedInDetails() {
        FirestoreClass().getCheckedInDetails(this, Constants.BOOKING_DETAILS)
    }

    private fun updateBookingDetailsUI(UpdatedBookingDetailsList: ArrayList<BookingDetails>) {
        bookingDetails = UpdatedBookingDetailsList

        rv_check_out_details.visibility = View.VISIBLE
        rv_check_out_details.layoutManager = LinearLayoutManager(this)
        rv_check_out_details.setHasFixedSize(true)

        val adapter = CheckOutDetailsAdapter(this, bookingDetails, mUserDetail)
        rv_check_out_details.adapter = adapter
    }

    fun showCheckOutDetailsDialog(position: Int) {
        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.check_out_details_dialog_view, null)
        var textNights =
            "${bookingDetails[position].room_reservation_details[0].numberOfDays} Days, ${bookingDetails[position].room_reservation_details[0].numberOfNights} Nights"
        var textReservationDetails =
            "${bookingDetails[position].room_reservation_details[0].numberOfRooms} Rooms, ${bookingDetails[position].room_reservation_details[0].numberOfGuests} Guests"

        val textViewReservationNumber =
            dialogView.findViewById<TextView>(R.id.check_out_details_dialog_reservation_number)
        textViewReservationNumber?.text = bookingDetails[position].reservationID

        val textViewNights =
            dialogView.findViewById<TextView>(R.id.check_out_details_dialog_nights)
        textViewNights?.text = textNights

        val textViewReservationDetails =
            dialogView.findViewById<TextView>(R.id.check_out_details_dialog_reservation_details)
        textViewReservationDetails?.text = textReservationDetails

        val textViewCheckInId =
            dialogView.findViewById<TextView>(R.id.check_out_details_dialog_check_in_id)
        textViewCheckInId?.text = bookingDetails[position].check_in_details[0].checkInID

        val textViewReservationDateRange =
            dialogView.findViewById<TextView>(R.id.check_out_details_dialog_date_range)
        textViewReservationDateRange.text = bookingDetails[position].room_reservation_details[0].reservationDateTime

        val textViewCheckOutConfirm = dialogView.findViewById<TextInputEditText>(R.id.tiet_check_out_dialog_confirm)

        textViewCheckOutConfirm.hint = resources.getString(R.string.tiet_confirm_check_out_hint)
        textViewCheckOutConfirm.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus) {
                textViewCheckOutConfirm.hint = resources.getString(R.string.tiet_confirm_check_out_hint)
            } else {
                textViewCheckOutConfirm.hint = ""
            }
        }

        val checkInDetailsDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        checkInDetailsDialogBuilder.setView(dialogView)
        checkOutDetailsAlertDialog = checkInDetailsDialogBuilder.create()
        checkOutDetailsAlertDialog.show()
    }
}