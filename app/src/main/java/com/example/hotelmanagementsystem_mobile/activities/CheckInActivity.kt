package com.example.hotelmanagementsystem_mobile.activities

import android.content.Context
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.adapters.CheckInDetailsAdapter
import com.example.hotelmanagementsystem_mobile.models.*
import kotlinx.android.synthetic.main.activity_check_in.*

class CheckInActivity : AppCompatActivity() {
    private lateinit var alertDialog: AlertDialog
    private var checkInDetailsList : ArrayList<CheckIn> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_in)

        setupActionBar()

        val typeface = Typeface.createFromAsset(assets, "BerkshireSwash-Regular.ttf")
        tv_check_in_title.typeface = typeface
    }

    override fun onStart() {
        super.onStart()
        getCheckInDetails()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_check_in_activity)

        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_check_in_activity.setNavigationOnClickListener{onBackPressed()}
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

    fun showCustomAlertDialog(position: Int) {
        Log.i("Position", position.toString())
        Log.i("Position", checkInDetailsList[position].toString())
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
        Log.i("AlertDialog", alertDialog.isShowing.toString())
    }
}