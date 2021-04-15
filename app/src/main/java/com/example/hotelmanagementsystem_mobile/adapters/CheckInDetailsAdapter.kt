
package com.example.hotelmanagementsystem_mobile.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.CheckInActivity
import com.example.hotelmanagementsystem_mobile.models.CheckIn
import kotlinx.android.synthetic.main.item_check_in_details.view.*

open class CheckInDetailsAdapter(private val context: CheckInActivity, private var list: ArrayList<CheckIn>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_check_in_details, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        Log.i("Position", position.toString())
        if(holder is MyViewHolder) {
            holder.itemView.tv_check_in_id.text = model.checkInId
            holder.itemView.tv_check_in_number_of_room.text = model.numberOfRooms.toString()
            holder.itemView.tv_check_in_number_of_customer.text = model.numberOfQuests.toString()

            holder.itemView.cv_check_in_details.setOnClickListener {
                context.showCustomAlertDialog(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}