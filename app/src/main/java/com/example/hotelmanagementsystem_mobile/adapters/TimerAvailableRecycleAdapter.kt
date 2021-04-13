package com.example.hotelmanagementsystem_mobile.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.models.TimeSlot
import org.w3c.dom.Text

class TimerAvailableRecycleAdapter(private val arrayList: ArrayList<TimeSlot>, val context: Context):
    BaseAdapter(){
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

        /*Hold the sports card item*/
        val v: View = View.inflate(context, R.layout.slot_available_card, null)

        /*get Image and TextView by the position of array*/
        val timerTextView:TextView=v.findViewById(R.id.txtViewTime)

        val listItem: TimeSlot = arrayList[position]

        timerTextView.text=listItem.timer


        return v



    }

}
