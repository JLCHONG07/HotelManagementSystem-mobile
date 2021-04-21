package com.example.hotelmanagementsystem_mobile.activities

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.hotelmanagementsystem_mobile.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.adapters.VoucherRecycleAdapter
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.models.ModelVoucher
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_categories.*
import kotlinx.android.synthetic.main.activity_e_vouchers.*
import java.lang.StringBuilder

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
        generateVoucher()

    }

    fun rand(start: Int, end: Int): Int {
        require(start <= end) { "Illegal Argument" }
        return (start..end).random()
    }

    fun generateVoucher() {
        val voucherArray = ArrayList<ModelVoucher>()
        val user = FirestoreClass().getCurrentUserId()
        val timeDuration60 = "60"
        val timeDuration120 = "120"
        val timeDuration180 = "180"

        for (i in 1..15) {

            val voucherCode = getRandomCode()
            var sportType = String()
            if (i == 1) {
                sportType = "Badminton"
                voucherArray.add(
                    ModelVoucher(
                        0,
                        timeDuration60,
                        sportType,
                        voucherCode,
                        "Sports",
                        true,
                        user
                    )
                )
            } else if (i == 2) {
                sportType = "Table Tennis"
                voucherArray.add(
                    ModelVoucher(
                        0,
                        timeDuration120,
                        sportType,
                        voucherCode,
                        "Sports",
                        true,
                        user
                    )
                )
            } else if (i == 3) {
                sportType = "Snooker"
                voucherArray.add(
                    ModelVoucher(
                        0,
                        timeDuration180,
                        sportType,
                        voucherCode,
                        "Sports",
                        true,
                        user
                    )
                )

            }
            else{
                sportType = "Snooker"
                voucherArray.add(
                    ModelVoucher(
                        0,
                        timeDuration180,
                        sportType,
                        voucherCode,
                        "Sports",
                        true,
                        user
                    )
                )

            }
        }

        for (i in voucherArray.indices) {
            Log.d("voucherCode", voucherArray[i].vouchCode)
        }

    }

    private fun getRandomCode(): String {

        val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var randomString = (1..4)
            .map { i -> kotlin.random.Random.nextInt(0, 25) }
            .map(source::get)
            .joinToString("")

        for (i in 1..4) {
            val randomNo = rand(0, 9)
            randomString += randomNo.toString()
        }

        //Log.d("randomString",randomString)
        return randomString

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

                // var counter = 0
                for (i in it.documents.indices) {

                    arrayListVoucher.add(
                        ModelVoucher(
                            R.drawable.e_voucher,
                            it.documents[i].data?.get("timeDuration") as String,
                            it.documents[i].data?.get("vouchType") as String,
                            it.documents[i].data?.get("vouchCode") as String,
                            it.documents[i].data?.get("vouchCat") as String,
                            it.documents[i].data?.get("available") as Boolean,
                            it.documents[i].data?.get("voucherID") as String

                        )
                    )
                    //  counter++;
                }

                assignVoucher(arrayListVoucher)

            }

    }
}