package com.example.hotelmanagementsystem_mobile.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hotelmanagementsystem_mobile.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.adapters.VoucherRecycleAdapter
import com.example.hotelmanagementsystem_mobile.models.ModelVoucher
import kotlinx.android.synthetic.main.activity_categories.*
import kotlinx.android.synthetic.main.activity_e_vouchers.*

class EVouchers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_e_vouchers)

        val arrayListVoucher = ArrayList<ModelVoucher>()
        arrayListVoucher.add(ModelVoucher(R.drawable.e_voucher,"MYR 20","E-voucher Code","HCNY2021","FOODS"));
        arrayListVoucher.add(ModelVoucher(R.drawable.e_voucher,"120 Minutes","E-voucher Code","HCNY2021","SPORTS"));

        val voucherRecycleAdapter = VoucherRecycleAdapter(arrayListVoucher,this@EVouchers)

        recycleViewVoucher.layoutManager=LinearLayoutManager(this)
        recycleViewVoucher.adapter=voucherRecycleAdapter
    }
}