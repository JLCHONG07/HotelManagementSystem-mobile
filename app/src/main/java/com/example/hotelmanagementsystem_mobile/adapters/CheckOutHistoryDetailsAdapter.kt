package com.example.hotelmanagementsystem_mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.CheckOutHistoryActivity
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.models.booking_details.BookingDetails
import kotlinx.android.synthetic.main.item_check_in_out_details.view.*

open class CheckOutHistoryDetailsAdapter(private val context : CheckOutHistoryActivity, private var list : ArrayList<BookingDetails>
                                        , private val mUserDetails : User) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_check_in_out_details, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder) {
            Glide
                .with(context)
                .load(mUserDetails.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(holder.itemView.civ_profile_image)

            holder.itemView.tv_check_in_user_name.text = mUserDetails.name
            holder.itemView.tv_check_in_id.text = model.check_out_details[0].checkOutID
            holder.itemView.tv_check_in_number_of_room.text = model.room_reservation_details[0].numberOfRooms.toString()
            holder.itemView.tv_check_in_number_of_customer.text = model.room_reservation_details[0].numberOfGuests.toString()
            holder.itemView.tv_check_in__date_range.text = model.room_reservation_details[0].reservationDateTime

            holder.itemView.cv_check_in_details.setOnClickListener {
                context.showCheckOutHistoryDetailsDialog(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}