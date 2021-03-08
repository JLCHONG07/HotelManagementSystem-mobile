package com.example.hotelmanagementsystem_mobile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_categories.view.*


class CategoriesRecycleAdapater(val arrayList:ArrayList<Model>,val context:Context):
    RecyclerView.Adapter<CategoriesRecycleAdapater.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bindItems(model: Model){
            itemView.txtViewCat.text=model.cat_name
            itemView.imgCat.setBackgroundResource(model._image)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val v=LayoutInflater.from(parent.context).inflate(R.layout.row_categories,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bindItems(arrayList[position])
    }



}