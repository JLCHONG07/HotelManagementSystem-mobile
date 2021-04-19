package com.example.hotelmanagementsystem_mobile.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.adapters.CheckInDetailsAdapter
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.models.*
import com.example.hotelmanagementsystem_mobile.models.booking_details.BookingDetails
import com.example.hotelmanagementsystem_mobile.models.booking_details.CheckInDetails
import com.example.hotelmanagementsystem_mobile.utils.Constants
import com.google.android.material.textfield.TextInputEditText
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_check_in.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckInActivity : BaseActivity() {
    private lateinit var checkInAlertDialog: AlertDialog
    private lateinit var checkInDetailsAlertDialog: AlertDialog

    private lateinit var bookingDetailsPath : String
    private lateinit var mUserDetail : User
    private var bookingDetails : ArrayList<BookingDetails> = ArrayList()

    private var reservationId : String = ""
    private var tAndCCheckbox : Boolean = false

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_in)

        bookingDetailsPath = ""
        sharedPreferences = getSharedPreferences(resources.getString(R.string.booking_details_preference_file), Context.MODE_PRIVATE)
        if(bookingDetailsPath.isEmpty() && !sharedPreferences.getString("booking_details_path", "").isNullOrEmpty()) {
            showProgressDialog(resources.getString(R.string.please_wait))
            bookingDetailsPath = sharedPreferences.getString("booking_details_path", "").toString()
            FirestoreClass().getCheckedInDetails(this, bookingDetailsPath)
        }

        if(intent.hasExtra(Constants.USERS)) {
            mUserDetail = intent.getParcelableExtra(Constants.USERS)
        }

        fab_check_in.setOnClickListener {
            showCheckInDialog()
        }

        val addReservationIDEditText = findViewById<TextInputEditText>(R.id.tiet_reservation_id_add)

        btn_check_in_add.setOnClickListener {
            if(!addReservationIDEditText.text.isNullOrBlank()) {
                showProgressDialog(resources.getString(R.string.please_wait))
                FirestoreClass().getBookingDetails(this, Constants.BOOKING_DETAILS, addReservationIDEditText.text.toString())
            }
            else {
                Toast.makeText(this, "Please enter your reservation id", Toast.LENGTH_LONG).show()
            }
        }

        addReservationIDEditText.hint = resources.getString(R.string.tv_reservation_id_add_hint)
        addReservationIDEditText.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus) {
                addReservationIDEditText.hint = resources.getString(R.string.tv_reservation_id_add_hint)
            } else {
                addReservationIDEditText.hint = ""
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Log.i("OnStop", "OnStop() call")
        if(bookingDetailsPath.isNotEmpty()) {
            with(sharedPreferences.edit()) {
                putString("booking_details_path", bookingDetailsPath).apply()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if(result != null) {
                if(result.contents != null) {
                    Toast.makeText(this, "Scanned successfully " + result.contents, Toast.LENGTH_LONG).show()
                    bookingDetailsPath = result.contents
                    if(bookingDetailsPath.isNotEmpty()) {
                        showProgressDialog(resources.getString(R.string.please_wait))
                        Log.i(javaClass.simpleName, bookingDetailsPath)
                        Log.i(javaClass.simpleName, reservationId)
                        FirestoreClass().getBookingDetails(this, bookingDetailsPath, reservationId)
                    } else {
                        Log.i(javaClass.simpleName, "No path")
                        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
                    }
                    Log.i("CheckInActivity", bookingDetailsPath)
                } else {
                    Toast.makeText(this, "There is problem when scanning!", Toast.LENGTH_LONG).show()
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        } else {
            Log.i("CheckInActivity", "Something went wrong")
        }
    }

    fun successfulGetBookingDetails(bookingDetails : BookingDetails) {
        hideProgressDialog()
        if(bookingDetails.check_in_details[0].checkInStatus != "checkedin" ||
            bookingDetails.check_in_details[0].checkInStatus != "checkedout" ||
                !bookingDetails.checkedInUser.contains(mUserDetail.id)) {
            updateBookingDetails(bookingDetails)
        } else {
            showProgressDialog(resources.getString(R.string.please_wait))
            getCheckedInDetails()
        }
    }

    fun successfulUpdateBookingDetails() {
        hideProgressDialog()

        if(this::checkInAlertDialog.isInitialized) {
            checkInAlertDialog.dismiss()
        }

        //Get checked in details
        showProgressDialog(resources.getString(R.string.please_wait))
        getCheckedInDetails()
    }

    fun successfulGetCheckedInDetails(checkInDetailsList: ArrayList<BookingDetails>) {
        hideProgressDialog()
        updateBookingDetailsUI(checkInDetailsList)
    }

    private fun validateInput(reservationId : String) : Boolean {
        return when {
            TextUtils.isEmpty(reservationId) -> {
                false
            } else -> {
                true
            }
        }
    }

    private fun updateBookingDetails(bookingDetails: BookingDetails) {
        showProgressDialog(resources.getString(R.string.please_wait))
        val status = "checkedin"

        //get current datetime
        val checkInCurrentDate = SimpleDateFormat("yyyy:MM:dd HH.mm.ss", Locale.ENGLISH)
        val checkInGenerateIDDateTime = SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH)
        val datetimeFormatted = checkInCurrentDate.format(Date())
        val generateDateTimeCheckInID = checkInGenerateIDDateTime.format(Date())

        //Custom check in id
        val generateCheckInID = "ci${generateDateTimeCheckInID}${mUserDetail.passportNumber.takeLast(4)}"

        //Create new check in details
        var checkInUser : ArrayList<String> = ArrayList()

        checkInUser = bookingDetails.checkedInUser

        if(!checkInUser.contains(mUserDetail.id)) {
            checkInUser.add(mUserDetail.id)
        }

        val oldCheckInDetails = bookingDetails.check_in_details
        val newCheckInDetails = CheckInDetails(
            generateCheckInID,
            datetimeFormatted.toString(), status
        )

        val newCheckInDetailsArray: ArrayList<CheckInDetails> = ArrayList()
        if(oldCheckInDetails[0].checkInStatus != "checkedin") {
            newCheckInDetailsArray.add(newCheckInDetails)
            bookingDetails.check_in_details = newCheckInDetailsArray
        }
        else {
            bookingDetails.check_in_details = oldCheckInDetails
        }

        bookingDetails.checkedInUser = checkInUser


        //Create new hash map
        val bookingDetailsHashMap = HashMap<String, Any>()

        //Set new booking detail to the hash map
        bookingDetailsHashMap[Constants.RESERVATION_ID] = bookingDetails.reservationID
        bookingDetailsHashMap[Constants.BOOKING_ID] = bookingDetails.bookingID
        bookingDetailsHashMap[Constants.CHECKED_IN_USER] = bookingDetails.checkedInUser
        bookingDetailsHashMap[Constants.CHECK_IN_DETAILS_PATH] = bookingDetails.check_in_details
        bookingDetailsHashMap[Constants.ROOM_RESERVATION_DETAILS_PATH] = bookingDetails.room_reservation_details

        if(bookingDetails != null) {
            Log.i(javaClass.simpleName, bookingDetailsPath)
            FirestoreClass().updateBookingDetails(this, bookingDetailsPath, bookingDetailsHashMap, bookingDetails.bookingID)
        }
    }

    private fun updateBookingDetailsUI(UpdatedBookingDetailsList: ArrayList<BookingDetails>) {
        bookingDetails = UpdatedBookingDetailsList

        rv_check_in_details.visibility = View.VISIBLE
        rv_check_in_details.layoutManager = LinearLayoutManager(this)
        rv_check_in_details.setHasFixedSize(true)

        val adapter = CheckInDetailsAdapter(this, bookingDetails, mUserDetail)
        rv_check_in_details.adapter = adapter
    }

    private fun getCheckedInDetails() {
        FirestoreClass().getCheckedInDetails(this, bookingDetailsPath)
    }

    private fun showCheckInDialog() {
        val inflater : LayoutInflater = this.layoutInflater
        val dialogView : View = inflater.inflate(R.layout.check_in_dialog, null)

        val checkInUsername = dialogView.findViewById<TextInputEditText>(R.id.tiet_check_in_dialog_username)
        checkInUsername.setText(mUserDetail.name)
        checkInUsername.keyListener = null

        val checkInICorPassport = dialogView.findViewById<TextInputEditText>(R.id.tiet_check_in_dialog_ic_no_or_passport)
        checkInICorPassport.setText(mUserDetail.passportNumber)
        checkInICorPassport.keyListener = null

        val checkInReservationID = dialogView.findViewById<TextInputEditText>(R.id.tiet_check_in_dialog_reservation_id)
        checkInReservationID.hint = resources.getString(R.string.tv_check_in_reservation_id)

        checkInReservationID.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus) {
                checkInReservationID.hint = resources.getString(R.string.tv_check_in_reservation_id)
            } else {
                checkInReservationID.hint = ""
            }
        }

        val termsAndConditionsCheckBox = dialogView.findViewById<CheckBox>(R.id.cb_check_in_dialog_terms_and_condition)
        val checkInConfirm = dialogView.findViewById<Button>(R.id.btn_check_in_confirm)
        checkInConfirm.setOnClickListener {
            reservationId = checkInReservationID.text.toString()
            tAndCCheckbox = termsAndConditionsCheckBox.isChecked
            if(validateInput(reservationId) && tAndCCheckbox) {
                val scanner = IntentIntegrator(this)
                scanner.initiateScan()
            } else {
                if(!validateInput(reservationId)) {
                    Toast.makeText(this, "Please enter your reservation id", Toast.LENGTH_LONG).show()
                } else if(!tAndCCheckbox) {
                    Toast.makeText(this, "Please comply to our terms and conditions", Toast.LENGTH_LONG).show()
                }
            }
        }

        val checkInDialogBuilder : AlertDialog.Builder = AlertDialog.Builder(this)

        checkInDialogBuilder.setView(dialogView)
        checkInAlertDialog = checkInDialogBuilder.create()
        checkInAlertDialog.show()
    }

    fun showCheckInDetailsDialog(position: Int) {
        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.check_in_details_dialog_view, null)
        var textNights =
            "${bookingDetails[position].room_reservation_details[0].numberOfDays} Days, ${bookingDetails[position].room_reservation_details[0].numberOfNights} Nights"
        var textReservationDetails =
            "${bookingDetails[position].room_reservation_details[0].numberOfRooms} Rooms, ${bookingDetails[position].room_reservation_details[0].numberOfGuests} Guests"

        val textViewReservationNumber =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_reservation_number)
        textViewReservationNumber?.text = bookingDetails[position].reservationID

        val textViewNights =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_nights)
        textViewNights?.text = textNights

        val textViewReservationDetails =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_reservation_details)
        textViewReservationDetails?.text = textReservationDetails

        val textViewRoomTypes =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_room_types)
        textViewRoomTypes?.text = bookingDetails[position].room_reservation_details[0].roomTypes

        val textViewDateAndTime =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_date_time)
        textViewDateAndTime?.text = bookingDetails[position].check_in_details[0].checkInDateAndTime

        val textViewCheckInId =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_check_in_id)
        textViewCheckInId?.text = bookingDetails[position].check_in_details[0].checkInID

        val textViewReservationDateRange =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_date_range)
        textViewReservationDateRange.text = bookingDetails[position].room_reservation_details[0].reservationDateTime

        val checkInDetailsDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        checkInDetailsDialogBuilder.setView(dialogView)
        checkInDetailsAlertDialog = checkInDetailsDialogBuilder.create()
        checkInDetailsAlertDialog.show()
    }
}