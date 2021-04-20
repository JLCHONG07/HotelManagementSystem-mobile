package com.example.hotelmanagementsystem_mobile.activities.facilities_booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.BaseActivity
import com.example.hotelmanagementsystem_mobile.adapters.BookHistoryRecycleAdapter
import com.example.hotelmanagementsystem_mobile.adapters.VoucherRecycleAdapter
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.models.BookFacilitiesHistory
import kotlinx.android.synthetic.main.activity_booking_history.*
import java.util.*
import kotlin.collections.ArrayList

class BookingHistory : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_history)

        val user = FirestoreClass().getCurrentUserId()
        FirestoreClass().retriveBookedHistory(this, user)


    }

    fun retrievedBookedHistory(arrayListBookHistory: ArrayList<BookFacilitiesHistory>) {

        val bookedHistory = ArrayList<BookFacilitiesHistory>()

        for (bookHistory in arrayListBookHistory.indices) {
            if (checkDate(arrayListBookHistory[bookHistory].date)) {
                Log.d("true", "true")
                if (arrayListBookHistory[bookHistory].cat.equals("Badminton")) {
                    bookedHistory.add(
                        BookFacilitiesHistory(
                            R.drawable.history_badminton,
                            arrayListBookHistory[bookHistory].catAndDuration,
                            arrayListBookHistory[bookHistory].time,
                            arrayListBookHistory[bookHistory].courtRoom,
                            R.color.history_yellow,
                            arrayListBookHistory[bookHistory].weekOfDay,
                            arrayListBookHistory[bookHistory].monthOfDate,
                            arrayListBookHistory[bookHistory].month,
                            arrayListBookHistory[bookHistory].cat,
                            arrayListBookHistory[bookHistory].date
                        )
                    )

                } else if (arrayListBookHistory[bookHistory].cat.equals("Table Tennis")) {
                    bookedHistory.add(
                        BookFacilitiesHistory(
                            R.drawable.history_table_tennis,
                            arrayListBookHistory[bookHistory].catAndDuration,
                            arrayListBookHistory[bookHistory].time,
                            arrayListBookHistory[bookHistory].courtRoom,
                            R.color.history_blue,
                            arrayListBookHistory[bookHistory].weekOfDay,
                            arrayListBookHistory[bookHistory].monthOfDate,
                            arrayListBookHistory[bookHistory].month,
                            arrayListBookHistory[bookHistory].cat,
                            arrayListBookHistory[bookHistory].date
                        )
                    )
                } else if (arrayListBookHistory[bookHistory].cat.equals("Snooker")) {
                    bookedHistory.add(
                        BookFacilitiesHistory(
                            R.drawable.history_snooker,
                            arrayListBookHistory[bookHistory].catAndDuration,
                            arrayListBookHistory[bookHistory].time,
                            arrayListBookHistory[bookHistory].courtRoom,
                            R.color.history_dark_green,
                            arrayListBookHistory[bookHistory].weekOfDay,
                            arrayListBookHistory[bookHistory].monthOfDate,
                            arrayListBookHistory[bookHistory].month,
                            arrayListBookHistory[bookHistory].cat,
                            arrayListBookHistory[bookHistory].date
                        )
                    )

                } else if (arrayListBookHistory[bookHistory].cat.equals("Gaming Room")) {
                    bookedHistory.add(
                        BookFacilitiesHistory(
                            R.drawable.categories_gaming_rooms,
                            arrayListBookHistory[bookHistory].catAndDuration,
                            arrayListBookHistory[bookHistory].time,
                            arrayListBookHistory[bookHistory].courtRoom,
                            R.color.history_dark_brown,
                            arrayListBookHistory[bookHistory].weekOfDay,
                            arrayListBookHistory[bookHistory].monthOfDate,
                            arrayListBookHistory[bookHistory].month,
                            arrayListBookHistory[bookHistory].cat,
                            arrayListBookHistory[bookHistory].date
                        )
                    )
                } else {
                    bookedHistory.add(
                        BookFacilitiesHistory(
                            R.drawable.categories_board_game,
                            arrayListBookHistory[bookHistory].catAndDuration,
                            arrayListBookHistory[bookHistory].time,
                            arrayListBookHistory[bookHistory].courtRoom,
                            R.color.history_dark_gray,
                            arrayListBookHistory[bookHistory].weekOfDay,
                            arrayListBookHistory[bookHistory].monthOfDate,
                            arrayListBookHistory[bookHistory].month,
                            arrayListBookHistory[bookHistory].cat,
                            arrayListBookHistory[bookHistory].date
                        )
                    )
                }

            }
            else{
                Log.d("false","false")
            }
            val BookHistoryRecycleAdapter = BookHistoryRecycleAdapter(bookedHistory, this)
            recycleViewCategories.layoutManager = LinearLayoutManager(this)
            recycleViewCategories.adapter = BookHistoryRecycleAdapter

        }
    }

    private fun checkDate(date: String): Boolean {

        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)

        Log.d("date", date)
        // get savedDay,savedMonth,savedYear eg 29_4_2021 savedDay=29,savedMonth=4,savedYear=2021
        var counter = 0
        //get total length of date
        val dateLength = date.length
        var savedDay: String? = ""
        var savedMonth: String? = ""
        var savedYear: String? = ""
        var currentDateChar: Char
        var i = 0
        while (i < dateLength) {
            currentDateChar = date.get(i)
            if (!currentDateChar.equals('_')) {
                if (counter == 0) {
                    savedDay += currentDateChar
                    i++
                } else if (counter == 1) {
                    savedMonth += currentDateChar
                    i++
                } else {
                    savedYear += currentDateChar
                    i++

                }
            } else {
                counter++
                i++
            }

        }

        Log.d("savedDay", savedDay.toString())
        Log.d("savedMonth", savedMonth.toString())
        Log.d("savedYear", savedYear.toString())


        //current year bigger than saved year
        if(year> savedYear!!.toInt()){
            return false
        }

        //current month bigger than saved month
        if(month>savedMonth!!.toInt()){
            return false
        }

        //same month but smaller day
        if(month== savedMonth.toInt() && day>savedDay!!.toInt()){
            return false
        }

        return true
    }
}