package com.example.hotelmanagementsystem_mobile.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.AdminFacilitiesBooking
import com.example.hotelmanagementsystem_mobile.activities.AdminViewTimeSlots
import com.example.hotelmanagementsystem_mobile.activities.facilities_booking.BookingAvailable
import com.example.hotelmanagementsystem_mobile.activities.facilities_booking.Categories
import com.example.hotelmanagementsystem_mobile.activities.facilities_booking.SportsCat
import com.example.hotelmanagementsystem_mobile.models.categories
import kotlinx.android.synthetic.main.row_categories.view.*


class CategoriesRecycleAdapter(private val arrayList:ArrayList<categories>, val context:Context):
    RecyclerView.Adapter<CategoriesRecycleAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

       /* Bind image and text view together by get the variable*/
        fun bindItems(model: categories){
            itemView.txtViewCat.text=model.cat_name
            itemView.imgCat.setBackgroundResource(model._image)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        /*Hold the categories card item*/
       val v=LayoutInflater.from(parent.context).inflate(R.layout.row_categories,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

       /* categories on click action which perform navigation*/
        holder.itemView.imgCat.setOnClickListener {
            val cardPos=arrayList[position]

            val actionBarTitle:String=cardPos.cat_name

            when(context){
                is Categories ->{
                    when (position) {
                        0 -> {
                            val type:String="Court"
                            //Log.d("position",position.toString())
                            val intent=Intent(context, SportsCat::class.java)
                            intent.putExtra("aBarTitle",actionBarTitle)
                            intent.putExtra("type", type)
                            //Log.d("court", type.toString())
                            context.startActivities(arrayOf(intent))
                        }
                        1 -> {
                            val type:String=holder.itemView.context.getString(R.string.select_room)
                           // Log.d("position",position.toString())
                            val intent=Intent(context,BookingAvailable::class.java)
                            intent.putExtra("aBarTitle",actionBarTitle)
                            intent.putExtra("type", type)
                            context.startActivities(arrayOf(intent))
                        }
                        2 -> {
                            val type:String=holder.itemView.context.getString(R.string.select_room)
                           // Log.d("position",position.toString())
                            val intent=Intent(context,BookingAvailable::class.java)
                            intent.putExtra("aBarTitle",actionBarTitle)
                            intent.putExtra("type", type)
                            //Log.d("room", type.toString())
                            context.startActivities(arrayOf(intent))
                        }
                    }
                }
               is AdminFacilitiesBooking->{
                   val dateSelected=context.validationRequiredField()
                   if(dateSelected) {
                       when (position) {
                           0 -> {
                               val type: String = "Court"
                               context.selectedView(actionBarTitle,type)

                           }
                           1 -> {
                               val type: String = "Court"
                               context.selectedView(actionBarTitle,type)

                           }
                           2 -> {
                               val type: String = "Court"
                               context.selectedView(actionBarTitle,type)
                           }
                           3 -> {
                               val type: String = "Room"
                               context.selectedView(actionBarTitle,type)
                           }
                           4 -> {
                               val type: String = "Room"
                               context.selectedView(actionBarTitle,type)
                           }
                       }
                   }
               }
            }

        }

    }

}