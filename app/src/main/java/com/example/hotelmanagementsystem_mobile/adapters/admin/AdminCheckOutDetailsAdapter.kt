package com.example.hotelmanagementsystem_mobile.adapters.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.admin.AdminCheckOutDetailsActivity
import com.example.hotelmanagementsystem_mobile.models.User
import com.example.hotelmanagementsystem_mobile.models.booking_details.BookingDetails
import kotlinx.android.synthetic.main.item_check_in_out_details.view.*

class AdminCheckOutDetailsAdapter(private val context : AdminCheckOutDetailsActivity,
                                    private val list : ArrayList<BookingDetails>,
                                    private val user_list : ArrayList<User>)
                                    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val layoutOne = 0
    private val layoutTwo = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val firstLayout =
            LayoutInflater.from(context).inflate(R.layout.item_check_in_out_details, parent, false)
        val secondLayout =
            LayoutInflater.from(context).inflate(R.layout.item_check_in_out_details, parent, false)

        return when (viewType) {
            layoutOne -> {
                MyViewHolder1(firstLayout)
            }
            else -> {
                MyViewHolder2(secondLayout)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bookingDetailsModel = list[position]
        val userDetailsModel = user_list[position]

        when (holder.itemViewType) {
            layoutOne -> {
                Glide
                    .with(context)
                    .load(userDetailsModel.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(holder.itemView.civ_profile_image)

                holder.itemView.tv_check_in_user_name.text = userDetailsModel.name
                holder.itemView.tv_check_in_id.text = bookingDetailsModel.check_out_details[0].checkOutID
                holder.itemView.tv_check_in_number_of_room.text =
                    bookingDetailsModel.room_reservation_details[0].numberOfRooms.toString()
                holder.itemView.tv_check_in_number_of_customer.text =
                    bookingDetailsModel.room_reservation_details[0].numberOfGuests.toString()
                holder.itemView.tv_check_in__date_range.text =
                    bookingDetailsModel.room_reservation_details[0].reservationDateTime

                holder.itemView.cv_check_in_details.setOnClickListener {
                    context.showCheckOutDetailsDialog(bookingDetailsModel, userDetailsModel)
                }
            }
            else -> {
                Glide
                    .with(context)
                    .load(userDetailsModel.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(holder.itemView.civ_profile_image)

                holder.itemView.tv_check_in_user_name.text = userDetailsModel.name
                holder.itemView.tv_check_in_id.text = bookingDetailsModel.check_out_details[0].checkOutID
                holder.itemView.tv_check_in_number_of_room.text =
                    bookingDetailsModel.room_reservation_details[0].numberOfRooms.toString()
                holder.itemView.tv_check_in_number_of_customer.text =
                    bookingDetailsModel.room_reservation_details[0].numberOfGuests.toString()
                holder.itemView.tv_check_in__date_range.text =
                    bookingDetailsModel.room_reservation_details[0].reservationDateTime

                holder.itemView.cv_check_in_details.setOnClickListener {
                    context.showCheckOutDetailsDialog(bookingDetailsModel, userDetailsModel)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> {
                layoutOne
            }
            else -> {
                layoutTwo
            }
        }
    }

    private class MyViewHolder1(view: View) : RecyclerView.ViewHolder(view)
    private class MyViewHolder2(view: View) : RecyclerView.ViewHolder(view)
}
