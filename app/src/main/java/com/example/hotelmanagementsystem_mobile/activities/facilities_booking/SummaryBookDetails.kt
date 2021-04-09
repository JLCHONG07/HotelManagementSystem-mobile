package com.example.hotelmanagementsystem_mobile.activities.facilities_booking

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.Homepage
import kotlinx.android.synthetic.main.activity_summary_book_details.*

class SummaryBookDetails : AppCompatActivity(), View.OnClickListener {

    private lateinit var alertDialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary_book_details)

        btnConfirm.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnConfirm -> {
                showSuccessfulAlertBox()
            }


        }
    }

    private fun showSuccessfulAlertBox() {
        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.successful_book_msg, null)


        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        dialogBuilder.setView(dialogView)

        dialogView.findViewById<Button>(R.id.btnOK).setOnClickListener {
            val intent= Intent(this,Homepage::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show()




    }
}