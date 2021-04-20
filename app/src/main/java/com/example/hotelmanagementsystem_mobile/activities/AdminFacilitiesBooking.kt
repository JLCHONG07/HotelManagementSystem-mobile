package com.example.hotelmanagementsystem_mobile.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.facilities_booking.BookingAvailable
import com.example.hotelmanagementsystem_mobile.adapters.CategoriesRecycleAdapter
import com.example.hotelmanagementsystem_mobile.models.categories
import kotlinx.android.synthetic.main.activity_admin_facilities_booking.*
import kotlinx.android.synthetic.main.activity_admin_facilities_booking.btnDate
import kotlinx.android.synthetic.main.activity_booking_available.*
import kotlinx.android.synthetic.main.activity_categories.*
import java.util.*
import kotlin.collections.ArrayList

class AdminFacilitiesBooking : BaseActivity(), DatePickerDialog.OnDateSetListener {

    private var day = 0;
    private var month = 0;
    private var year = 0;
    private var savedDay = 0;
    private var savedMonth = 0;
    private var savedYear = 0;
    private var selectedDate: String? = null
    private var cvtMonth: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var first: Boolean = true
    private var fromHomePage: Boolean = true

    // private var temDate: String? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_facilities_booking)

        val arrayList = ArrayList<categories>()
        /*add the data to arrayList of Model (Categories)*/
        arrayList.add(categories("Badminton", R.drawable.history_badminton))
        arrayList.add(categories("Table Tennis", R.drawable.history_table_tennis))
        arrayList.add(categories("Snooker", R.drawable.history_snooker))
        arrayList.add(categories("Board Game", R.drawable.categories_board_game))
        arrayList.add(categories("Gaming Room", R.drawable.categories_gaming_rooms))

        val adminCheckFBAdapter = CategoriesRecycleAdapter(arrayList, this@AdminFacilitiesBooking)
        rvAdminCheckFB.layoutManager = LinearLayoutManager(this)
        rvAdminCheckFB.adapter = adminCheckFBAdapter

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        btnDate.text = sharedPreferences.getString("btnDate", btnDate.text.toString())
        selectedDate = sharedPreferences.getString("selectedDate", selectedDate)
        savedDay = sharedPreferences.getInt("savedDay", savedDay)
        fromHomePage = sharedPreferences.getBoolean("fromHomePage", fromHomePage)

        val intent = intent
        fromHomePage = intent.getBooleanExtra("fromHomePage", fromHomePage)


        if (fromHomePage) {

            btnDate.text = getString(R.string.date_picker)
            savedDay = 0

        }


        pickDate()


    }

    @SuppressLint("CommitPrefEdits")
    override fun onStop() {

        super.onStop()

        fromHomePage = false

        Log.d("firstTime", first.toString())

        with(sharedPreferences.edit()) {
            putString("btnDate", btnDate.text.toString()).apply()
            putString("selectedDate", selectedDate).apply()
            putInt("savedDay", savedDay).apply()
            putBoolean("fromHomePage", fromHomePage).apply()


        }


    }


    fun validationRequiredField(): Boolean {

        var pass = true
        if (savedDay != 0) {
            txtViewAdminDateErrorMsg.visibility = View.INVISIBLE
            return pass

        } else {
            txtViewAdminDateErrorMsg.visibility = View.VISIBLE
            pass = false
            return pass
        }
    }

    //trigger when click on dd MMM yyyy
    private fun pickDate() {

        btnDate.setOnClickListener {
            getDateCalender()

            DatePickerDialog(this, this, year, month, day).show()

        }
    }

    //get current Date on calender
    private fun getDateCalender() {

        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)


        //val currentDate = "${day}/${month}/${year}"
        //Log.d("currentHour", savedHour.toString())
        // Log.d("currentMinute", savedMinute.toString())
        //Log.d("currentDate", currentDate)

    }


    //Update the date after selected from calender
    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year
        selectedDate = "${savedDay}_${savedMonth + 1}_${savedYear}"
        // Log.d("selectedDate", selectedDate)
        cvtMonth = BookingAvailable().converter(savedMonth)
        btnDate.text = "$savedDay $cvtMonth $savedYear"
        //temDate = btnDate.text.toString()

    }

    fun selectedView(categories: String, type: String) {

        val intent = Intent(this, AdminViewTimeSlots::class.java)
        intent.putExtra("aBarTitle", categories)
        intent.putExtra("type", type)
        intent.putExtra("selectedDate", selectedDate)
        intent.putExtra("convertedDate", btnDate.text)
        startActivities(arrayOf(intent))

    }
}