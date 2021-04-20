package com.example.hotelmanagementsystem_mobile.activities

import android.annotation.SuppressLint
import android.os.Bundle

import android.view.MotionEvent
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.adapters.AdminViewTimerAdapter
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.models.TimeSlot

class AdminViewTimeSlots : BaseActivity() {

    private var gridView1: GridView? = null
    private var timerAdapter1: AdminViewTimerAdapter? = null
    private var gridView2: GridView? = null
    private var timerAdapter2: AdminViewTimerAdapter? = null
    private var type: String? = null
    private var selectedDate: String? = null
    private var aBarTitle: String? = null
    private var convertedDate: String? = null

    @SuppressLint("ClickableViewAccessibility", "CutPasteId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_view_time_slots)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        val intent = intent
        aBarTitle = intent.getStringExtra("aBarTitle")
        type = intent.getStringExtra("type")
        selectedDate = intent.getStringExtra("selectedDate")
        convertedDate=intent.getStringExtra("convertedDate")
        actionBar.title = aBarTitle

        findViewById<GridView>(R.id.slot_available_time1).setOnTouchListener {

                v, event ->
            event.action == MotionEvent.ACTION_MOVE

        }

        findViewById<GridView>(R.id.slot_available_time2).setOnTouchListener {

                v, event ->
            event.action == MotionEvent.ACTION_MOVE

        }

        findViewById<TextView>(R.id.txtViewDate).text = convertedDate
        findViewById<TextView>(R.id.tvCourtRoom1).text = "$type 1"
        findViewById<TextView>(R.id.tvCourtRoom2).text = "$type 2"
        retrieveViewBookedData1()
    }

    private fun setDataList(): ArrayList<TimeSlot> {

        val arrayList = ArrayList<TimeSlot>()
        arrayList.add(TimeSlot("timeID00", "10:00 AM"))
        arrayList.add(TimeSlot("timeID01", "11:00 AM"))
        arrayList.add(TimeSlot("timeID02", "12:00 PM"))
        arrayList.add(TimeSlot("timeID03", "1:00 PM"))
        arrayList.add(TimeSlot("timeID04", "2:00 PM"))
        arrayList.add(TimeSlot("timeID05", "3:00 PM"))
        arrayList.add(TimeSlot("timeID06", "4:00 PM"))
        arrayList.add(TimeSlot("timeID07", "5:00 PM"))
        arrayList.add(TimeSlot("timeID08", "6:00 PM"))
        arrayList.add(TimeSlot("timeID09", "7:00 PM"))
        arrayList.add(TimeSlot("timeID10", "8:00 PM"))
        arrayList.add(TimeSlot("timeID11", "9:00 PM"))
        return arrayList

    }

    fun getDataList1(slotsBooked: MutableMap<String, Any>) {

        val totalSlot = setDataList()
        gridView1 = findViewById(R.id.slot_available_time1)
        timerAdapter1 = AdminViewTimerAdapter(totalSlot, this@AdminViewTimeSlots, slotsBooked)
        gridView1?.adapter = timerAdapter1
        retrieveViewBookedData2()

    }

    fun setDataList2(slotsBooked: MutableMap<String, Any>) {

        val totalSlot = setDataList()
        gridView2 = findViewById(R.id.slot_available_time2)
        timerAdapter2 = AdminViewTimerAdapter(totalSlot, this@AdminViewTimeSlots, slotsBooked)
        gridView2?.adapter = timerAdapter2

    }

    fun retrieveViewBookedData1() {

        FirestoreClass().retrieveViewBookedData1(
            this@AdminViewTimeSlots,
            selectedDate,
            aBarTitle,
            type
        )

    }

    fun retrieveViewBookedData2() {

        FirestoreClass().retrieveViewBookedData2(
            this@AdminViewTimeSlots,
            selectedDate,
            aBarTitle,
            type
        )

    }

}