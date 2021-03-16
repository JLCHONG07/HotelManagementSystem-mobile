package com.example.hotelmanagementsystem_mobile.activities.facilities_booking

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.cardview.widget.CardView
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.Homepage
import kotlinx.android.synthetic.main.activity_booking_available.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class BookingAvailable : AppCompatActivity(), View.OnClickListener,DatePickerDialog.OnDateSetListener {

    var day=0;
    var month=0;
    var year=0;

    var savedDay=0;
    var savedMonth=0;
    var savedYear=0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_available)


        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayShowHomeEnabled(true)
        var intent = intent
        val aBarTitle = intent.getStringExtra("aBarTitle")

        actionBar!!.title = aBarTitle

        cardView60Minutes.setOnClickListener(this)
        cardView120Minutes.setOnClickListener(this)
        cardView180Minutes.setOnClickListener(this)



        pickDate()
    }


    /* Back to previous activity*/
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    //clicking button/card functions of the activities
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cardView60Minutes -> {
                selectedTimeDrtCard(cardView60Minutes, txtViewNum1)
            }
            R.id.cardView120Minutes -> {
                selectedTimeDrtCard(cardView120Minutes, txtViewNum2)
            }
            R.id.cardView180Minutes -> {
                selectedTimeDrtCard(cardView180Minutes, txtViewNum3)
            }
        }
    }

    //change design of card for time duration card selection
    private fun selectedTimeDrtCard(cv: CardView, tvNum: TextView) {
        cardDefaultView()
        cv.setCardBackgroundColor(Color.parseColor("#969FAA"))
        tvNum.setTextColor(Color.parseColor("#FFFFFFFF"))

    }

    //reset time duration card design
    private fun cardDefaultView() {
        cardView60Minutes.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
        cardView120Minutes.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
        cardView180Minutes.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
        txtViewNum1.setTextColor(Color.parseColor("#F95F62"))
        txtViewNum2.setTextColor(Color.parseColor("#F95F62"))
        txtViewNum3.setTextColor(Color.parseColor("#F95F62"))

    }
    private fun pickDate(){



        btnDate.setOnClickListener{
            getDateCalender()

            DatePickerDialog(this,this,year,month,day).show()

        }
        
    }

    private fun getDateCalender(){
        val cal=Calendar.getInstance()
        day=cal.get(Calendar.DAY_OF_MONTH)
        month=cal.get(Calendar.MONTH)
        year=cal.get(Calendar.YEAR)
        
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay=dayOfMonth
        savedMonth=month
        savedYear=year

        btnDate.text="$savedDay/$savedMonth/$savedYear"
    }
}