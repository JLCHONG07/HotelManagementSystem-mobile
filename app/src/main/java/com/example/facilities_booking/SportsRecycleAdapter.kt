package com.example.facilities_booking

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelmanagementsystem_mobile.R
import kotlinx.android.synthetic.main.sports_card_view_item.view.*


class SportsRecycleAdapter(private val arrayList: ArrayList<ModelSport>, val context: Context):
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


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        /*Hold the sports card item*/
        val v:View= inflate(context,R.layout.sports_card_view_item,null)

        /*get Image and TextView by the position of array*/
        val sportsImage:RelativeLayout =v.findViewById(R.id.imgSports)
        val sportsName:TextView=v.findViewById(R.id.txtViewSports)
        val listItem:ModelSport= arrayList[position]
        sportsImage.setBackgroundResource(listItem.sportsImage)
        sportsName.text=listItem.sportsName

        return v


    }

}