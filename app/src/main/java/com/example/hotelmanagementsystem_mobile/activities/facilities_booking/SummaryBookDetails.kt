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
import android.widget.CheckBox
import android.widget.EditText
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.Homepage
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import kotlinx.android.synthetic.main.activity_booking_available.*
import kotlinx.android.synthetic.main.activity_summary_book_details.*

class SummaryBookDetails : AppCompatActivity(), View.OnClickListener {

    private lateinit var alertDialog: AlertDialog

    private var selectedTime: String? = null
    private var selectedDuration: String? = null
    private var selectedRoomCourt: String? = null
    private var selectedTimeSlot: Long? = null
    private var selectedDate: String? = null
    private var currentCat: String? = null
    private var currentType: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary_book_details)


        val selectedDate = intent. getStringExtra("selectedDate")
        val selectedStartTime = intent.getStringExtra("startTime")
        val selectDuration = intent.getStringExtra("selectedDuration")
        val selectedRoomCourt =intent.getStringExtra("selectedRoomCourt")
        val selectedTimeSlot =intent.getStringExtra("selectedTimeSlot")
        val currentCat = intent.getStringExtra("currentCat")
        val currentType = intent.getStringExtra("currentType")

        if (selectedTime != null) {

            val intent = Intent(this, SummaryBookDetails::class.java)
            intent.putExtra("selectedDate", btnDate.text)
            intent.putExtra("selectedStartTime",selectedStartTime)
            intent.putExtra("selectedRoomCourt",selectedRoomCourt)
            intent.putExtra("startTime", selectedTime)
            intent.putExtra("selectedDuration", selectedDuration)
            intent.putExtra("selectedTimeSlot",selectedTimeSlot)
            intent.putExtra("currentCat",currentCat)
            intent.putExtra("currentType", currentType)
            //idk actually
        }

        var cvtToHours = selectDuration.toInt()
        cvtToHours /= 60
        Log.d("cvtToHours", cvtToHours.toString())
        val calEndTime = sumHours(cvtToHours, selectedStartTime)

        BookingDate.text = selectedDate
        startTime.text = selectedStartTime
        bookDrtMin.text = selectDuration + " " + getString(R.string.sc_minutes)
        endTime.text = calEndTime
        btnConfirm.setOnClickListener(this)
        textViewTAndC.setOnClickListener(this)
        btnApply.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnConfirm -> {

                val checkBoxChecked = findViewById<CheckBox>(R.id.CheckBoxPayment)
                if (checkBoxChecked.isChecked) {
                    txtViewCheckErrorMsg.visibility = View.INVISIBLE

                }
                if (validVoucher() && checkBoxChecked.isChecked) {
                    showSuccessfulAlertBox()
                } else {
                    txtViewCheckErrorMsg.visibility = View.VISIBLE
                }
            }
            R.id.textViewTAndC -> {
                showTAndCAlertBox()
            }
            R.id.btnApply -> {
                val checkBoxChecked = findViewById<CheckBox>(R.id.CheckBoxPayment)
                if (checkBoxChecked.isChecked) {
                    txtViewCheckErrorMsg.visibility = View.INVISIBLE

                }
                validVoucher()
            }


        }
    }

    private fun showTAndCAlertBox() {
        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.term_and_condition, null)


        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show()
        alertDialog.window?.setLayout(600, 1000);


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

    private fun validVoucher(): Boolean {

        val voucherEditText = findViewById<EditText>(R.id.editTextVoucher).text.toString().trim()
        Log.d("voucher code", voucherEditText)

        if (voucherEditText.isNullOrBlank()) {
            txtViewVoucherErrorMsg.visibility = View.VISIBLE
            return false

        } else {
            txtViewVoucherErrorMsg.visibility = View.INVISIBLE
            return true
            //any validation here
        }


    }

    fun sumHours(hours: Int, startTime: String): String {
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

         if(startHours.equals(" 1")){
             startHours= 11.toString()
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

    private fun history() {
        var cvtToHours = selectedDuration?.toInt()
        cvtToHours = cvtToHours?.div(60)
        if (cvtToHours!! > 1) {
            var counter = 0
            while (counter < cvtToHours) {
                FirestoreClass().saveBookedData(
                    selectedTimeSlot, selectedTime,
                    selectedDate, selectedRoomCourt, currentCat, currentType
                )
                Log.d("currentCat", currentCat)
                Log.d("currentType", currentType)
                counter++
                selectedTimeSlot = selectedTimeSlot?.plus(1)
                selectedTime = SummaryBookDetails().sumHours(1, selectedTime.toString())
                //selectedTime = sumHours(selectedTime.toString())
            }
        } else {
            Log.d("currentCat", currentCat)
            Log.d("currentType", currentType)
            FirestoreClass().saveBookedData(
                selectedTimeSlot, selectedTime,
                selectedDate, selectedRoomCourt, currentCat, currentType
            )

        }
    }

}