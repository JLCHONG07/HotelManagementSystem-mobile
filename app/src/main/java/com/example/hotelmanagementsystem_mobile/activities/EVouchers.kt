package com.example.hotelmanagementsystem_mobile.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.hotelmanagementsystem_mobile.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.adapters.VoucherRecycleAdapter
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.models.ModelVoucher
import com.example.hotelmanagementsystem_mobile.models.booking_details.BookingDetails
import com.example.hotelmanagementsystem_mobile.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_categories.*
import kotlinx.android.synthetic.main.activity_e_vouchers.*
import kotlinx.android.synthetic.main.row_evoucher.*

class EVouchers : AppCompatActivity() {

    private var myClipboard: ClipboardManager? = null



    private val user = FirestoreClass().getCurrentUserId()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_e_vouchers)

       // val arrayListVoucher = ArrayList<ModelVoucher>()
        //retrieveVoucher(arrayListVoucher)


        myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?;

        //generateVoucher()

        FirestoreClass().getCheckedInDetails(this,Constants.BOOKING_DETAILS)
    }

    fun copyText(voucherCode: String) {
        var myClip: ClipData? = null
        myClip = ClipData.newPlainText("text", voucherCode);
        myClipboard?.setPrimaryClip(myClip)


        Toast.makeText(this, "Copied Voucher Code $voucherCode", Toast.LENGTH_SHORT).show();
    }

    fun rand(start: Int, end: Int): Int {
        require(start <= end) { "Illegal Argument" }
        return (start..end).random()
    }

    fun generateVoucher(reservationID:String) {
        val voucherArray = ArrayList<ModelVoucher>()

        val timeDuration60 = "60"
        val timeDuration120 = "120"
        val timeDuration180 = "180"

        for (i in 1..5) {

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
                        reservationID,
                        ""
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
                        reservationID,
                        ""
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
                        reservationID,
                        ""
                    )
                )

            }
            else if(i==4){
                sportType = "Board Game"
                voucherArray.add(
                    ModelVoucher(
                        0,
                        timeDuration120,
                        sportType,
                        voucherCode,
                        "",
                        reservationID,
                        ""
                    )
                )

            }
            else{
                sportType = "Gaming Room"
                voucherArray.add(
                    ModelVoucher(
                        0,
                        timeDuration180,
                        sportType,
                        voucherCode,
                        "",
                        reservationID,
                        ""
                    )
                )
            }
        }

        for (i in voucherArray.indices) {
            Log.d("voucherCode", voucherArray[i].vouchCode)
        }

        saveGeneratedVoucher(voucherArray)

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

     fun assignVoucher(arrayListVoucher: ArrayList<ModelVoucher>) {
        val voucherRecycleAdapter = VoucherRecycleAdapter(arrayListVoucher, this@EVouchers)

        recycleViewVoucher.layoutManager = LinearLayoutManager(this)
        recycleViewVoucher.adapter = voucherRecycleAdapter

    }
    fun saveGeneratedVoucher(voucherArray: ArrayList<ModelVoucher>){
        FirestoreClass().saveGeneratedVoucher(voucherArray)
    }

    fun retrieveVoucher(bookingDetails:ArrayList<BookingDetails>){

        var reservationID=ArrayList<String>()
        for(i in bookingDetails){
            if(i.checkedInUser.contains(user)){
                reservationID.add(i.reservationID)
            }
        }
        FirestoreClass().retrieveVoucher(this,reservationID)
    }


}