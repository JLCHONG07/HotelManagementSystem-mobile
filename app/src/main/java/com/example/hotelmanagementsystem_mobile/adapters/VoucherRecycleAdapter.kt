package com.example.hotelmanagementsystem_mobile.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.EVouchers
import com.example.hotelmanagementsystem_mobile.models.ModelVoucher
import kotlinx.android.synthetic.main.row_evoucher.view.*

class VoucherRecycleAdapter(private val arrayListVoucher: ArrayList<ModelVoucher>, val context: Context):
    RecyclerView.Adapter<VoucherRecycleAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /* Bind image and text view together by get the variable*/
        fun bindItems(model: ModelVoucher) {
            //voucher image
            itemView.voucherImage.setBackgroundResource(model._image)
            //Voucher Duration
            itemView.tvMinutes.text = model.timeDuration.toString()
            //Voucher type eg badminton, table tennis, snooker may nullable also
            itemView.tvFBTypes.text = model.vouchType
            //Voucher code
            itemView.tvEVCode.text = model.vouchCode
            //Voucher categories eg, sport, board game, Gaming Room
            itemView.tvCategories.text = model.vouchCat


        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherRecycleAdapter.ViewHolder {

        /*Hold the categories card item*/
        val v=LayoutInflater.from(parent.context).inflate(R.layout.row_evoucher,parent,false)
        return VoucherRecycleAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayListVoucher.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayListVoucher[position])
        //Log.d("holder", holder.bindItems(arrayListVoucher[position]).toString())
        holder.itemView.voucherImage.setOnClickListener {
            val voucherPos=arrayListVoucher[position]
            val voucherCode=voucherPos.vouchCode
            when(context){
                is EVouchers->{
                    context.copyText(voucherCode)
                }
            }

        }
    }
}