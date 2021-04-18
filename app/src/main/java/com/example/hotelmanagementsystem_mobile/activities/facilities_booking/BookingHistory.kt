package com.example.hotelmanagementsystem_mobile.activities.facilities_booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.BaseActivity
import com.example.hotelmanagementsystem_mobile.adapters.BookHistoryRecycleAdapter
import com.example.hotelmanagementsystem_mobile.adapters.VoucherRecycleAdapter
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.models.BookFacilitiesHistory
import kotlinx.android.synthetic.main.activity_booking_history.*

class BookingHistory : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_history)


        val user= FirestoreClass().getCurrentUserId()
        FirestoreClass().retriveBookedHistory(this, user)
        /*    arrayListBookHistory.add(
                BookFacilitiesHistory(
                    R.drawable.history_badminton,
                    "Badminton (60 minutes)",
                    "4:00 PM - 5:00 PM",
                    "Court 1",
                    R.color.history_yellow,
                    "Tue",
                    "25",
                    "December"
                )
            )
            arrayListBookHistory.add(
                BookFacilitiesHistory(
                    R.drawable.history_table_tennis,
                    "Table Tennis (180 minutes)",
                    "4:00 PM - 5:00 PM",
                    "Court 1",
                    R.color.history_blue,
                    "Tue",
                    "25",
                    "December"
                )
            )
            arrayListBookHistory.add(
                BookFacilitiesHistory(
                    R.drawable.history_snooker,
                    "Snooker (60 minutes)",
                    "4:00 PM - 5:00 PM",
                    "Court 1",
                    R.color.history_dark_green,
                    "Tue",
                    "25",
                    "December"
                )

            )

            arrayListBookHistory.add(
                BookFacilitiesHistory(
                    R.drawable.categories_board_game,
                    "Board Game (60 minutes)",
                    "4:00 PM - 5:00 PM",
                    "Court 1",
                    R.color.history_dark_gray,
                    "Tue",
                    "25",
                    "December"
                )
            )
            arrayListBookHistory.add(
                BookFacilitiesHistory(
                    R.drawable.categories_gaming_rooms,
                    "Gaming Room (180 minutes)",
                    "4:00 PM - 5:00 PM",
                    "Court 1",
                    R.color.history_dark_brown,
                    "Tue",
                    "25",
                    "December"
                )

            )*/

    }

    fun retrievedBookedHistory(arrayListBookHistory: ArrayList<BookFacilitiesHistory>) {

        val bookedHistory = ArrayList<BookFacilitiesHistory>()

        for (bookHistory in arrayListBookHistory.indices) {
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
                        arrayListBookHistory[bookHistory].cat
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
                        arrayListBookHistory[bookHistory].cat
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
                        arrayListBookHistory[bookHistory].cat
                    )
                )

            } else if (arrayListBookHistory[bookHistory].cat.equals("Gaming Room")) {
                bookedHistory.add(
                    BookFacilitiesHistory(
                        R.drawable.categories_gaming_rooms,
                        arrayListBookHistory[bookHistory].catAndDuration,
                        arrayListBookHistory[bookHistory].time,
                        arrayListBookHistory[bookHistory].courtRoom,
                        R.color.history_dark_gray,
                        arrayListBookHistory[bookHistory].weekOfDay,
                        arrayListBookHistory[bookHistory].monthOfDate,
                        arrayListBookHistory[bookHistory].month,
                        arrayListBookHistory[bookHistory].cat
                    )
                )
            } else {
                bookedHistory.add(
                    BookFacilitiesHistory(
                        R.drawable.categories_board_game,
                        arrayListBookHistory[bookHistory].catAndDuration,
                        arrayListBookHistory[bookHistory].time,
                        arrayListBookHistory[bookHistory].courtRoom,
                        R.color.history_dark_brown,
                        arrayListBookHistory[bookHistory].weekOfDay,
                        arrayListBookHistory[bookHistory].monthOfDate,
                        arrayListBookHistory[bookHistory].month,
                        arrayListBookHistory[bookHistory].cat
                    )
                )
            }

        }
        val BookHistoryRecycleAdapter = BookHistoryRecycleAdapter(bookedHistory, this)
        recycleViewCategories.layoutManager = LinearLayoutManager(this)
        recycleViewCategories.adapter = BookHistoryRecycleAdapter

    }


}