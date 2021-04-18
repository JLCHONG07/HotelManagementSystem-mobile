package com.example.hotelmanagementsystem_mobile.activities.facilities_booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.adapters.BookHistoryRecycleAdapter
import com.example.hotelmanagementsystem_mobile.adapters.VoucherRecycleAdapter
import com.example.hotelmanagementsystem_mobile.models.BookFacilitiesHistory
import kotlinx.android.synthetic.main.activity_booking_history.*

class BookingHistory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_history)

        val arrayListBookHistory = ArrayList<BookFacilitiesHistory>()
        arrayListBookHistory.add(
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

        )
        val BookHistoryRecycleAdapter = BookHistoryRecycleAdapter(arrayListBookHistory, this)

        recycleViewCategories.layoutManager = LinearLayoutManager(this)
        recycleViewCategories.adapter = BookHistoryRecycleAdapter
    }
}