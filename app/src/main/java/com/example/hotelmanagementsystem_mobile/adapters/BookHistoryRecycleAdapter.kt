package com.example.hotelmanagementsystem_mobile.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.models.BookFacilitiesHistory
import kotlinx.android.synthetic.main.row_booking_history.view.*
import kotlinx.android.synthetic.main.row_evoucher.view.*

class BookHistoryRecycleAdapter(private val arrayListBookHistory: ArrayList<BookFacilitiesHistory>, val context: Context):
    RecyclerView.Adapter<BookHistoryRecycleAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /* Bind image and text view together by get the variable*/
        fun bindItems(model: BookFacilitiesHistory) {
            //cat image
            itemView.imageCat.setBackgroundResource(model.imageCat)
            //categories and Duration eg badminton (60 minutes)
            itemView.tvCatAndDuration.text = model.catAndDuration
            //time eg 6:00 PM - 7:00 PM
            itemView.tvTime.text = model.time
            //Court or Room eg Court 1
            itemView.tvCourtRoom.text = model.courtRoom
            //color of rightside
            itemView.imgColour.setBackgroundResource(model.color)
            //week of day eg mon, tue...
            itemView.tvWeekOfDay.text = model.weekOfDay
            //date eg 1-30
            itemView.tvMonthOfDate.text=model.monthOfDate
            //month eg December
            itemView.tvMonth.text=model.month

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookHistoryRecycleAdapter.ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.row_booking_history,parent,false)
        return BookHistoryRecycleAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: BookHistoryRecycleAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayListBookHistory[position])
    }

    override fun getItemCount(): Int {
        return arrayListBookHistory.size
    }

}