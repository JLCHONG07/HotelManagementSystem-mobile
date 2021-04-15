package com.example.hotelmanagementsystem_mobile.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.adapters.CheckInDetailsAdapter
import com.example.hotelmanagementsystem_mobile.models.*
import com.example.hotelmanagementsystem_mobile.utils.Constants
import com.google.android.material.textfield.TextInputEditText
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_check_in.*

class CheckInActivity : AppCompatActivity() {
    private lateinit var alertDialog: AlertDialog

    private lateinit var mUserDetail : User
    private var checkInDetailsList : ArrayList<CheckIn> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_in)

        if(intent.hasExtra(Constants.USERS)) {
            mUserDetail = intent.getParcelableExtra(Constants.USERS)
        }

        fab_check_in.setOnClickListener {
            showCheckInDialog()
        }
    }

    override fun onStart() {
        super.onStart()
        getCheckInDetails()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if(result != null) {
                if(result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun getCheckInDetails() {
        checkInDetailsList.add(CheckIn("RSVTN0001", 3, 2, 2, 4, "Quest Room", "checkin0001", "2 February 2021, 2:30pm"))
        checkInDetailsList.add(CheckIn("RSVTN0001", 3, 2, 2, 4, "Quest Room", "checkin0001", "2 February 2021, 2:30pm"))

        rv_check_in_details.visibility = View.VISIBLE
        rv_check_in_details.layoutManager = LinearLayoutManager(this)
        rv_check_in_details.setHasFixedSize(true)

        val adapter = CheckInDetailsAdapter(this, checkInDetailsList)
        rv_check_in_details.adapter = adapter
    }

    @SuppressLint("ClickableViewAccessibility")
    fun showCheckInDialog() {
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
        checkInReservationID.setOnTouchListener { view, event ->
            checkInReservationID.hint = ""
            false
        }

        checkInReservationID.setOnFocusChangeListener { view, hasFocus ->
            if(!hasFocus) {
                checkInReservationID.hint = resources.getString(R.string.tv_check_in_reservation_id)
            }
        }

        val checkInConfirm = dialogView.findViewById<Button>(R.id.btn_check_in_confirm)
        checkInConfirm.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.setBeepEnabled(false)
            scanner.initiateScan()
        }

        val dialogBuilder : AlertDialog.Builder = AlertDialog.Builder(this)

        dialogBuilder.setView(dialogView)
        alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    fun showCustomAlertDialog(position: Int) {
        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.check_in_out_details_dialog_view, null)
        var textNights =
            "${checkInDetailsList[position].numberOfDays} Days, ${checkInDetailsList[position].numberOfNights} Nights"
        var textReservationDetails =
            "${checkInDetailsList[position].numberOfRooms} Rooms, ${checkInDetailsList[position].numberOfQuests} Guests"

        val textViewReservationNumber =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_reservation_number)
        textViewReservationNumber?.text = checkInDetailsList[position].reservationNumber

        val textViewNights =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_nights)
        textViewNights?.text = textNights

        val textViewReservationDetails =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_reservation_details)
        textViewReservationDetails?.text = textReservationDetails

        val textViewRoomTypes =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_room_types)
        textViewRoomTypes?.text = checkInDetailsList[position].roomTypes

        val textViewDateAndTime =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_date_time)
        textViewDateAndTime?.text = checkInDetailsList[position].checkInDateAndTime

        val textViewCheckInId =
            dialogView.findViewById<TextView>(R.id.check_in_details_dialog_check_in_id)
        textViewCheckInId?.text = checkInDetailsList[position].checkInId

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        dialogBuilder.setView(dialogView)
        alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}