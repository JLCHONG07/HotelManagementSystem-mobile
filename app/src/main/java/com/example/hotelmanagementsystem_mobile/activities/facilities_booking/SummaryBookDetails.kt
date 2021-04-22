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
import com.example.hotelmanagementsystem_mobile.activities.BaseActivity
import com.example.hotelmanagementsystem_mobile.activities.Homepage
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.fragments.HomeFragment
import com.example.hotelmanagementsystem_mobile.models.ModelVoucher
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.models.booking_details.BookingDetails
import com.example.hotelmanagementsystem_mobile.utils.Constants
import kotlinx.android.synthetic.main.activity_booking_available.*
import kotlinx.android.synthetic.main.activity_summary_book_details.*
import java.util.*

class SummaryBookDetails : BaseActivity(), View.OnClickListener {

    private lateinit var alertDialog: AlertDialog

    private var selectedTime: String? = null
    private var selectedDuration: String? = null
    private var selectedRoomCourt: String? = null
    private var selectedTimeSlot: Long? = null
    private var selectedDate: String? = null
    private var currentCat: String? = null
    private var currentType: String? = null

    private var savedDay = 0;
    private var savedMonth = 0;
    private var savedYear = 0;
    private var cvtMonth: String? = null

    private var weekOfDay: String? = null

    private var date: String? = null
    private var invalidVoucher: Boolean = false
    private var reservationID = ArrayList<String>()

    //private var imageCatURL:String?=null
    // private var color:String?=null
    private var time: String? = null
    private var catAndDuration: String? = null
    private val user = FirestoreClass().getCurrentUserId()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary_book_details)


        selectedDate = intent.getStringExtra("selectedDate")
        selectedTime = intent.getStringExtra("startTime") //start Time
        selectedDuration = intent.getStringExtra("selectedDuration")
        selectedRoomCourt = intent.getStringExtra("selectedRoomCourt")
        selectedTimeSlot = intent.getLongExtra("selectedTimeSlot", 0)
        currentCat = intent.getStringExtra("currentCat")
        currentType = intent.getStringExtra("currentType")

        savedDay = intent.getIntExtra("savedDay", 0)
        savedMonth = intent.getIntExtra("savedMonth", 0)
        savedYear = intent.getIntExtra("savedYear", 0)
        cvtMonth = intent.getStringExtra("cvtMonth")
       // username.text=user.name

/*
        if (selectedTime != null) {

            val intent = Intent(this, SummaryBookDetails::class.java)
            intent.putExtra("selectedDate", btnDate.text)
            intent.putExtra("selectedStartTime", selectedStartTime)
            intent.putExtra("selectedRoomCourt", selectedRoomCourt)
            intent.putExtra("startTime", selectedTime)
            intent.putExtra("selectedDuration", selectedDuration)
            intent.putExtra("selectedTimeSlot", selectedTimeSlot)
            intent.putExtra("currentCat", currentCat)
            intent.putExtra("currentType", currentType)
            //idk actually about this
        }
*/

        val selectedHours = selectedDuration
        var cvtToHours = selectedHours!!.toInt()
        cvtToHours /= 60
        Log.d("cvtToHours", cvtToHours.toString())
        val calEndTime = sumHours(cvtToHours, selectedTime)
        weekOfDay = getWeekOfday()
        cvtMonth = getMonth()
        time = "$selectedTime - ${calEndTime}"
        date = "${savedDay}_${savedMonth + 1}_${savedYear}"
        catAndDuration = "$currentCat (${selectedDuration} minutes)"
        BookingDate.text = "$savedDay $cvtMonth $savedYear"
        startTime.text = selectedTime
        bookDrtMin.text = selectedDuration + " " + getString(R.string.sc_minutes)
        endTime.text = calEndTime
        btnConfirm.setOnClickListener(this)
        textViewTAndC.setOnClickListener(this)

        FirestoreClass().loadUserData(this,HomeFragment())
        getTotalPrice(cvtToHours)
        //btnApply.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnConfirm -> {

                val checkBoxChecked = findViewById<CheckBox>(R.id.CheckBoxPayment)
                if (checkBoxChecked.isChecked) {
                    txtViewCheckErrorMsg.visibility = View.INVISIBLE
                    Log.d("getCheckedInDetails","getCheckedInDetails")
                    FirestoreClass().getCheckedInDetails(this@SummaryBookDetails, Constants.BOOKING_DETAILS)

                } else {
                    txtViewCheckErrorMsg.visibility = View.VISIBLE
                }

            }
            R.id.textViewTAndC -> {
                showTAndCAlertBox()
            }

        }
    }


    //convert month in Integer form to String MMM
    fun getMonth(): String {

        when (savedMonth) {
            0 -> {
                return "January"
            }
            1 -> {
                return "February"

            }
            2 -> {
                return "March"

            }
            3 -> {
                return "April"

            }
            4 -> {
                return "May"

            }
            5 -> {
                return "June"

            }
            6 -> {
                return "July"

            }
            7 -> {
                return "August"

            }
            8 -> {
                return "September"


            }
            9 -> {
                return "October"

            }
            10 -> {
                return "November"

            }
            11 -> {
                return "December"

            }
        }
        return "null"
    }


    private fun getWeekOfday(): String {
        val gregorianCalendar = GregorianCalendar(savedYear, savedMonth, savedDay - 1)
        val weekOfDay = gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK)

        when (weekOfDay) {

            1 -> {
                return "MON"

            }
            2 -> {
                return "TUE"

            }
            3 -> {
                return "WED"

            }
            4 -> {
                return "THU"

            }
            5 -> {
                return "FRI"

            }
            6 -> {
                return "SAT"

            }
            7 -> {
                return "SUN"

            }
        }
        return "null"
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
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false);

    }

    fun getUserName(user : User){

        username.text=user.name
    }

    @SuppressLint("SetTextI18n")
    private fun getTotalPrice(price:Int){
        if(price==1){
            totalPrice.text="RM "+(price*20).toString()
        }
        else if(price==2){
            totalPrice.text="RM "+(price*20).toString()
        }
        else{
            totalPrice.text="RM "+(price*20).toString()
        }

    }



    fun getVoucherCode(voucherCodeArray: ArrayList<ModelVoucher>) {

        Log.d("getVoucherCode","getVoucherCode")

        val cmpVoucherCode = editTextVoucher.text.toString()
        var codeMatch = false
        var currentRsvtID=String()
        var voucherID=String()
        if (editTextVoucher.text.isNullOrBlank()) {
            txtViewVoucherErrorMsg.visibility = View.VISIBLE

        } else {
            txtViewVoucherErrorMsg.visibility = View.INVISIBLE
            for (i in voucherCodeArray.indices) {
/*                Log.d("vouchCode",voucherCodeArray[i].vouchCode)
                Log.d("cmpVoucherCode",cmpVoucherCode.toString())
                Log.d("available",voucherCodeArray[i].available.toString())
                Log.d("timeDuration", voucherCodeArray[i].timeDuration)
                Log.d("selectedDuration",selectedDuration)
                Log.d("vouchType",voucherCodeArray[i].vouchType)
                Log.d("currentCat",currentCat)*/
                if (voucherCodeArray[i].vouchCode == cmpVoucherCode &&
                    voucherCodeArray[i].timeDuration == selectedDuration && voucherCodeArray[i].vouchType == currentCat
                ) {

                    currentRsvtID=voucherCodeArray[i].userID
                    codeMatch = true
                    voucherID=voucherCodeArray[i].voucherID
                    break
                    Log.d("codeMatch",codeMatch.toString())
                }
/*                else {
                    Log.d("codeMatch", codeMatch.toString())
                }*/
                //txtViewVoucherErrorMsg.visibility = View.INVISIBLE
                //any validation here
            }
            if (!codeMatch) {
                txtViewVoucherErrorMsg.text="Invalid voucher code"
                txtViewVoucherErrorMsg.visibility = View.VISIBLE
            } else {
                txtViewVoucherErrorMsg.visibility = View.INVISIBLE
                history()
                saveBookData()
                updateVoucherCode(cmpVoucherCode,currentRsvtID,voucherID)
                showSuccessfulAlertBox()
            }
        }


    }

    fun updateVoucherCode(cmpVoucherCode:String,reservationID:String,voucherID:String){

        FirestoreClass().updateVoucherCode(
            cmpVoucherCode,reservationID,voucherID
        )
    }

    fun retrieveVoucher2(bookingDetails: ArrayList<BookingDetails>) {

        //var reservationID=ArrayList<String>()
        Log.d("retrieveVoucher2","retrieveVoucher2")
        for (i in bookingDetails) {
            if (i.checkedInUser.contains(user)) {
                reservationID.add(i.reservationID)
            }
        }
        Log.d("retrieveVoucher","retrieveVoucher")
        FirestoreClass().retrieveVoucher(this@SummaryBookDetails, reservationID)
    }

/*
    private fun validVoucher(): Boolean {

        val voucherEditText = findViewById<EditText>(R.id.editTextVoucher).text.toString().trim()
        Log.d("voucher code", voucherEditText)

        if (voucherEditText.isNullOrBlank()) {
            txtViewVoucherErrorMsg.visibility = View.VISIBLE
            return false

        } else {
            // if(editTextVoucher.text.toString()==)
            txtViewVoucherErrorMsg.visibility = View.INVISIBLE
            return true
            //any validation here
        }


    }
*/

    fun sumHours(hours: Int, startTime: String?): String {
        //get hours of startTime
        var i = 0
        val startTimeLength = startTime!!.length
        var startHours = String()
        var totalSum = String()
        while (i < startTimeLength) {
            val currentChar: Char = startTime.get(i)
            if (currentChar.compareTo(':') != 0) {
                startHours += currentChar
                i++

            } else {

                break
            }
        }

        if (startHours.equals(" 1")) {
            startHours = 11.toString()
        }

        var endHours = startHours.toInt()
        endHours = endHours.plus(hours)
        when {
            endHours == 12 -> {
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

    private fun saveBookData() {
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


    private fun history() {

        val userID = FirestoreClass().getCurrentUserId()
        val courtRoom = "$currentType $selectedRoomCourt"
        FirestoreClass().history(
            userID,
            time.toString(),
            courtRoom,
            weekOfDay.toString(),
            date.toString(),
            currentCat.toString(),
            catAndDuration.toString(),
            cvtMonth.toString(),
            savedDay.toString()
        )


    }

}