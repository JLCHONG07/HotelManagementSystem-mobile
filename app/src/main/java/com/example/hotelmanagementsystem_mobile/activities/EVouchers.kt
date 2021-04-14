package com.example.hotelmanagementsystem_mobile.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.hotelmanagementsystem_mobile.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.adapters.VoucherRecycleAdapter
import com.example.hotelmanagementsystem_mobile.models.ModelVoucher
import com.example.hotelmanagementsystem_mobile.models.TimeSlot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_categories.*
import kotlinx.android.synthetic.main.activity_e_vouchers.*

class EVouchers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_e_vouchers)

        val arrayListVoucher = ArrayList<ModelVoucher>()
        retrieveVoucher(arrayListVoucher)


        // val arrayListVoucher = ArrayList<ModelVoucher>()
        // arrayListVoucher.add(ModelVoucher(R.drawable.e_voucher,60,"Table Tennis","TT123456","SPORTS"));
        // arrayListVoucher.add(ModelVoucher(R.drawable.e_voucher,120,"Badminton","BMT12345","SPORTS"));
        // arrayListVoucher.add(ModelVoucher(R.drawable.e_voucher,180,"Snooker","SNK12345","SPORTS"));
        //  arrayListVoucher.add(ModelVoucher(R.drawable.e_voucher,60," ","GR123456","GAMING ROOM"));
        //  arrayListVoucher.add(ModelVoucher(R.drawable.e_voucher,120," ","GR123456","GAMING ROOM"));
        //  arrayListVoucher.add(ModelVoucher(R.drawable.e_voucher,180," ","GR123456","GAMING ROOM"));
        //  arrayListVoucher.add(ModelVoucher(R.drawable.e_voucher,60," ","BG123456","BOARD GAME"));
        //  arrayListVoucher.add(ModelVoucher(R.drawable.e_voucher,120," ","BG123456","BOARD GAME"));
        //  arrayListVoucher.add(ModelVoucher(R.drawable.e_voucher,180," ","BG123456","BOARD GAME"));


    }

    private fun assignVoucher(arrayListVoucher: ArrayList<ModelVoucher>) {
        val voucherRecycleAdapter = VoucherRecycleAdapter(arrayListVoucher, this@EVouchers)

        recycleViewVoucher.layoutManager = LinearLayoutManager(this)
        recycleViewVoucher.adapter = voucherRecycleAdapter

    }

    private fun retrieveVoucher(arrayListVoucher: ArrayList<ModelVoucher>) {
        val db = FirebaseFirestore.getInstance()

        db.collection("e_voucher")
            .get()
            .addOnSuccessListener {

                var counter = 0
                for (i in it.documents) {

                    arrayListVoucher.add(
                        ModelVoucher(
                            R.drawable.e_voucher,
                            it.documents[counter].data?.get("timeDuration") as String,
                            it.documents[counter].data?.get("vouchType") as String,
                            it.documents[counter].data?.get("vouchCode") as String,
                            it.documents[counter].data?.get("vouchCat") as String
                        )
                    )
                    counter++;
                }

                assignVoucher(arrayListVoucher)

            }


    }
}