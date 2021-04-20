package com.example.hotelmanagementsystem_mobile.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.models.TimeSlot


class AdminViewTimerAdapter(
    private val arrayList: ArrayList<TimeSlot>,
    val context: Context,
    val slotsBooked: MutableMap<String, Any>
) : BaseAdapter() {
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return arrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        /*Hold the time card item*/
        val v: View = View.inflate(context, R.layout.slot_available_card, null)
        /*get TextView by the position of array*/
        val timerTextView: TextView = v.findViewById(R.id.txtViewTime)
        val listItem: TimeSlot = arrayList[position]
        timerTextView.text = listItem.timer
        //if booked then set the gray
        if (slotsBooked.containsKey(arrayList[position].timerID)) {

            v.findViewById<LinearLayout>(R.id.linearLayout_slots_available_time)
                .setBackgroundResource(R.drawable.clicked_time_border_outline)
            v.findViewById<TextView>(R.id.txtViewTime)
                .setTextColor(Color.parseColor("#FFFFFFFF"))

        }
        return v
    }
}
