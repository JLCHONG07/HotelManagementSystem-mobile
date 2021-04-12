package com.example.hotelmanagementsystem_mobile.activities.facilities_booking

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.Homepage
import kotlinx.android.synthetic.main.activity_booking_available.*
import kotlinx.android.synthetic.main.activity_summary_book_details.*

class SummaryBookDetails : AppCompatActivity(), View.OnClickListener {

    private lateinit var alertDialog: AlertDialog

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary_book_details)


        val selectedDate = intent.getStringExtra("selectedDate")
        val selectedStartTime = intent.getStringExtra("startTime")
        val selectDuration = intent.getStringExtra("selectedDuration")

        var cvtToHours = selectDuration.toInt()
        cvtToHours /= 60
        Log.d("cvtToHours", cvtToHours.toString())
        val calEndTime = sumHours(cvtToHours, selectedStartTime)

        BookingDate.text = selectedDate
        startTime.text = selectedStartTime
        bookDrtMin.text = selectDuration + " " + getString(R.string.sc_minutes)
        endTime.text = calEndTime
        btnConfirm.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnConfirm -> {
                showSuccessfulAlertBox()
            }


        }
    }

    private fun showSuccessfulAlertBox() {
        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.successful_book_msg, null)


        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        dialogBuilder.setView(dialogView)

        dialogView.findViewById<Button>(R.id.btnOK).setOnClickListener {
            val intent = Intent(this, Homepage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            alertDialog.dismiss()
            startActivity(intent)
        }
        alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show()


    }

    private fun sumHours(hours: Int, startTime: String): String {
        //get hours of startTime
        var i = 0
        val startTimeLength = startTime.length
        var startHours: String? = ""
        var totalSum: String? = null
        while (i < startTimeLength) {
            val currentChar: Char = startTime.get(i)
            if (currentChar.compareTo(':') != 0) {
                startHours += currentChar
                i++

            } else {

                break
            }
        }

        var endHours = startHours?.toInt()
        endHours = endHours?.plus(hours)
        when {
            endHours!! == 12 -> {
                totalSum = "$endHours:00 PM"

            }
            endHours == 11 -> {


                totalSum = "$endHours:00 AM"

            }
            endHours > 12 -> {

                endHours -= 12
                totalSum = " $endHours:00 PM"
            }
            else -> {
                totalSum = "$endHours:00 PM"
            }
        }


        Log.d("startHours", startHours.toString())
        Log.d("endHours", endHours.toString())
        Log.d("endTime", totalSum.toString())

        return totalSum.toString()


    }
}