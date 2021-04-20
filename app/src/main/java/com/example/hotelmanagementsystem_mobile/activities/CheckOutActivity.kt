package com.example.hotelmanagementsystem_mobile.activities

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.adapters.CheckOutDetailsAdapter
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.models.booking_details.BookingDetails
import com.example.hotelmanagementsystem_mobile.models.booking_details.CheckOutDetails
import com.example.hotelmanagementsystem_mobile.utils.Constants
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_check_out.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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

    fun successfulUpdateCheckedOutDetails() {
        getCheckedInDetails()
        if(this::checkOutDetailsAlertDialog.isInitialized) {
            checkOutDetailsAlertDialog.dismiss()
        }
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

        val textViewCheckOutConfirm =
            dialogView.findViewById<TextInputEditText>(R.id.tiet_check_out_dialog_confirm)

        val checkBoXTermsAndCondition =
            dialogView.findViewById<CheckBox>(R.id.cb_check_out_dialog_terms_and_condition)
        val buttonCheckOut = dialogView.findViewById<Button>(R.id.btn_check_out_confirm)
        val noneFirstCheckInUser = dialogView.findViewById<TextView>(R.id.tv_none_first_check_in_user)

        if(bookingDetails[position].checkedInUser[0] == mUserDetail.id) {
            textViewCheckOutConfirm.hint = resources.getString(R.string.tiet_confirm_check_out_hint)
            textViewCheckOutConfirm.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    textViewCheckOutConfirm.hint =
                        resources.getString(R.string.tiet_confirm_check_out_hint)
                } else {
                    textViewCheckOutConfirm.hint = ""
                }
            }

            buttonCheckOut.setOnClickListener {
                val confirmText = textViewCheckOutConfirm.text.toString()
                if (validateForm(confirmText) && checkBoXTermsAndCondition.isChecked) {
                    val checkOutDetails = updateCheckOutInfo(position)

                    noneFirstCheckInUser.text = ""

                    showProgressDialog(resources.getString(R.string.please_wait))
                    FirestoreClass().updateCheckOutDetails(
                        this,
                        bookingDetails[position].bookingID,
                        checkOutDetails
                    )
                } else {
                    if (!validateForm(confirmText)) {
                        Toast.makeText(
                            this,
                            "Please enter 'CONFIRM' or 'confirm'.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (!checkBoXTermsAndCondition.isChecked) {
                        Toast.makeText(
                            this,
                            "Please comply to our terms and conditions",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        } else {
            textViewCheckOutConfirm.isEnabled = false
            textViewCheckOutConfirm.setBackgroundResource(R.color.dialog_text_view_background_color)
            checkBoXTermsAndCondition.isEnabled = false
            buttonCheckOut.isEnabled = false
            buttonCheckOut.setBackgroundResource(R.color.dialog_text_view_background_color)
            buttonCheckOut.setTextColor(Color.BLACK)
            buttonCheckOut.isClickable = false
            noneFirstCheckInUser.isEnabled = true
            noneFirstCheckInUser.text = resources.getString(R.string.only_first_user)
            noneFirstCheckInUser.setTextColor(Color.RED)
        }

        val checkInDetailsDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        checkInDetailsDialogBuilder.setView(dialogView)
        checkOutDetailsAlertDialog = checkInDetailsDialogBuilder.create()
        checkOutDetailsAlertDialog.show()
    }

    private fun validateForm(confirm: String) : Boolean {
        return when {
            TextUtils.equals(confirm, "confirm") -> {
                true
            }
            TextUtils.equals(confirm, "CONFIRM") -> {
                true
            }
            else -> {
                false
            }
        }
    }

    private fun updateCheckOutInfo(position: Int) : HashMap<String, Any> {
        val newBookingDetails = bookingDetails[position]
        val checkOutText = "checkedout"
        var checkOutDetailsList : ArrayList<CheckOutDetails> = ArrayList()
        val newBookingDetailsHashMap = HashMap<String, Any>()

        //Check out details
        val checkOutDateTime = SimpleDateFormat("yyyy:MM:dd HH.mm.ss", Locale.ENGLISH)
        val formattedCheckOut = checkOutDateTime.format(Date())

        val checkOutDateTimeID = SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH)
        val formattedCheckOutDateTimeID = checkOutDateTimeID.format(Date())
        val generatedCheckOutID = "co${formattedCheckOutDateTimeID}${mUserDetail.passportNumber.takeLast(4)}"
        val checkOutDetails = CheckOutDetails(
            checkOutID = generatedCheckOutID,
            checkOutDateTime = formattedCheckOut.toString()
        )

        checkOutDetailsList.add(checkOutDetails)

        newBookingDetails.check_out_details = checkOutDetailsList

        newBookingDetails.status = checkOutText

        newBookingDetailsHashMap[Constants.BOOKING_ID] = newBookingDetails.bookingID
        newBookingDetailsHashMap[Constants.RESERVATION_ID] = newBookingDetails.reservationID
        newBookingDetailsHashMap[Constants.STATUS] = newBookingDetails.status
        newBookingDetailsHashMap[Constants.CHECKED_IN_USER] = newBookingDetails.checkedInUser
        newBookingDetailsHashMap[Constants.CHECK_IN_DETAILS_PATH] = newBookingDetails.check_in_details
        newBookingDetailsHashMap[Constants.ROOM_RESERVATION_DETAILS_PATH] = newBookingDetails.room_reservation_details
        newBookingDetailsHashMap[Constants.CHECK_OUT_DETAILS_PATH] = newBookingDetails.check_out_details

        return newBookingDetailsHashMap
    }
}