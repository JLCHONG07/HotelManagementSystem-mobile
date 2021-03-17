package com.example.hotelmanagementsystem_mobile.activities.facilities_booking

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.adapters.TimerAvailableRecycleAdapter
import com.example.hotelmanagementsystem_mobile.models.ModelTimer
import kotlinx.android.synthetic.main.activity_booking_available.*
import org.w3c.dom.Text
import java.util.*

class BookingAvailable : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemClickListener,


    DatePickerDialog.OnDateSetListener {

    var day = 0;
    var month = 0;
    var year = 0;

    var savedDay = 0;
    var savedMonth = 0;
    var savedYear = 0;
    var cvtMonth:String?=null;

    private var arrayList:ArrayList<ModelTimer>?=null
    private var timerAdapter: TimerAvailableRecycleAdapter?=null


    private lateinit var alertDialog: AlertDialog

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_available)


        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayShowHomeEnabled(true)
        val intent = intent
        val aBarTitle = intent.getStringExtra("aBarTitle")
        val type = intent.getStringExtra("type")
        val selection = getString(R.string.selection)


        txtViewCourtRoom.text = "$selection $type :"
        txtViewSelection1.text = type
        txtViewSelection2.text = type
        actionBar!!.title = aBarTitle

        cardView60Minutes.setOnClickListener(this)
        cardView120Minutes.setOnClickListener(this)
        cardView180Minutes.setOnClickListener(this)
        cardViewSelection1.setOnClickListener(this)
        cardViewSelection2.setOnClickListener(this)
        btnCheckAvailable.setOnClickListener(this)


        pickDate()
    }


    /* Back to previous activity*/
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    //clicking button/card functions of the activities
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cardView60Minutes -> {
                selectedTimeDrtCard(cardView60Minutes, txtViewNum1)
            }
            R.id.cardView120Minutes -> {
                selectedTimeDrtCard(cardView120Minutes, txtViewNum2)
            }
            R.id.cardView180Minutes -> {
                selectedTimeDrtCard(cardView180Minutes, txtViewNum3)
            }
            R.id.cardViewSelection1 -> {
                selectionRoomCourt(cardViewSelection1, txtViewSelectionWord1)
            }
            R.id.cardViewSelection2 -> {
                selectionRoomCourt(cardViewSelection2, txtViewSelectionWord2)
            }
            R.id.btnCheckAvailable -> {
                showCustomDialog()

            }
        }

    }


    private fun showCustomDialog() {


        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.slot_available_dialog, null)

        val titleSlotAvailable=dialogView.findViewById<TextView>(R.id.txtViewWordSlotAvailable)
        titleSlotAvailable.text="Slot Available"

        val selectedDate=dialogView.findViewById<TextView>(R.id.txtViewSelectedDate)
        selectedDate.text="$cvtMonth $day"

        val gridView=dialogView.findViewById<GridView>(R.id.slot_available_time)

        arrayList = ArrayList()
        arrayList=setDataList()
        timerAdapter = TimerAvailableRecycleAdapter(arrayList!!, applicationContext)
        gridView?.adapter = timerAdapter
        gridView?.onItemClickListener=this



        val dialogBuilder: AlertDialog.Builder=AlertDialog.Builder(this)
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show()

        Log.i("AlertDialog", alertDialog.isShowing.toString())

    }

    //change design of card for time duration card selection
    private fun selectedTimeDrtCard(cv: CardView, tvNum: TextView) {
        cardDefaultView()
        cv.setCardBackgroundColor(Color.parseColor("#969FAA"))
        tvNum.setTextColor(Color.parseColor("#FFFFFFFF"))

    }

    //change design of card for selection room/court
    private fun selectionRoomCourt(cv: CardView, tvNum: TextView) {
        selectionCardDefView()
        cv.setCardBackgroundColor(Color.parseColor("#969FAA"))
        tvNum.setTextColor(Color.parseColor("#FFFFFFFF"))

    }

    //reset time duration card design
    private fun cardDefaultView() {
        cardView60Minutes.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
        cardView120Minutes.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
        cardView180Minutes.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
        txtViewNum1.setTextColor(Color.parseColor("#F95F62"))
        txtViewNum2.setTextColor(Color.parseColor("#F95F62"))
        txtViewNum3.setTextColor(Color.parseColor("#F95F62"))

    }

    //reset selection room/court design
    private fun selectionCardDefView() {
        cardViewSelection1.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
        cardViewSelection2.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
        txtViewSelectionWord1.setTextColor(Color.parseColor("#F95F62"))
        txtViewSelectionWord2.setTextColor(Color.parseColor("#F95F62"))

    }

    //trigger when click on dd MMM yyyy
    private fun pickDate() {

        btnDate.setOnClickListener {
            getDateCalender()

            DatePickerDialog(this, this, year, month, day).show()

        }

    }

    //get calender
    private fun getDateCalender() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month=cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)


    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        cvtMonth = converter(savedMonth)


        btnDate.text = "$savedDay $cvtMonth $savedYear"
    }

    private fun converter(month: Int?): String {

        when (month) {
            0 -> {
                return "Jan"
            }
            1 -> {
                return "Feb"

            }
            2 -> {
                return "Mar"

            }
            3 -> {
                return "Apr"

            }
            4 -> {
                return "May"

            }
            5 -> {
                return "Jun"

            }
            6 -> {
                return "Jul"

            }
            7 -> {
                return "Aug"

            }
            8 -> {
                return "Sep"


            }
            9 -> {
                return "Oct"

            }
            10 -> {
                return "Nov"

            }
            11 -> {
                return "Dec"

            }
        }
        return "null"
    }

    private fun setDataList(): ArrayList<ModelTimer>? {

        /*assign data by passing parameter to Sports Model*/
        val arrayList=ArrayList<ModelTimer>()

        arrayList.add(ModelTimer("Time1", "11:00 AM"))
        arrayList.add(ModelTimer("Time2", "12:00 PM"))
        arrayList.add(ModelTimer("Time3", "1:00 PM"))
        arrayList.add(ModelTimer("Time1", "2:00 PM"))



        return arrayList
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }
}