package com.example.hotelmanagementsystem_mobile.activities.facilities_booking

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.cardview.widget.CardView
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.BaseActivity
import com.example.hotelmanagementsystem_mobile.adapters.TimerAvailableRecycleAdapter
import com.example.hotelmanagementsystem_mobile.firebase.FirestoreClass
import com.example.hotelmanagementsystem_mobile.models.TimeSlot
import kotlinx.android.synthetic.main.activity_booking_available.*
import java.util.*
import kotlin.collections.ArrayList


class BookingAvailable : BaseActivity(), View.OnClickListener, AdapterView.OnItemClickListener,


    DatePickerDialog.OnDateSetListener {

    private var day = 0;
    private var month = 0;
    private var year = 0;

    private var savedDay = 0;
    private var savedMonth = 0;
    private var savedYear = 0;
    private var cvtMonth: String? = null
    private var previousParent: AdapterView<*>? = null
    private var previousPosition = -1
    private var selectedTime: String? = null
    private var selectedDuration: String? = null
    private var selectedRoomCourt: String? = null
    private var selectedTimeSlot: Long? = null
    private var selectedDate: String? = null
    private var aBarTitle: String? = null
    private var type: String? = null

    private var arrayList: ArrayList<TimeSlot>? = null
    private var timerAdapter: TimerAvailableRecycleAdapter? = null

    private lateinit var alertDialog: AlertDialog

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_available)


        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        val intent = intent
        aBarTitle = intent.getStringExtra("aBarTitle")
        type = intent.getStringExtra("type")
        val selection = getString(R.string.selection)

        txtViewCourtRoom.text = "$selection $type :"
        txtViewSelection1.text = type
        txtViewSelection2.text = type
        actionBar.title = aBarTitle

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
                validationRequiredField()
                retrieveBookedData()

                if (validationRequiredField()) {

                    if (validationDate()) {
                        txtViewDateErrorMsg.visibility = View.INVISIBLE
                        showCustomDialog()
                    } else {
                        val dateError = getString(R.string.date_error)
                        txtViewDateErrorMsg.text = dateError
                        txtViewDateErrorMsg.visibility = View.VISIBLE
                    }
                }

            }

        }

    }


    //create dialog after click on Check Available button
    @SuppressLint("SetTextI18n")
    private fun showCustomDialog() {


        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.slot_available_dialog, null)

        val titleSlotAvailable = dialogView.findViewById<TextView>(R.id.txtViewWordSlotAvailable)
        titleSlotAvailable.text = "Slot Available"

        val txtViewselectedDate = dialogView.findViewById<TextView>(R.id.txtViewSelectedDate)
        txtViewselectedDate.text = "$cvtMonth $savedDay"


        val gridView = dialogView.findViewById<GridView>(R.id.slot_available_time)

        dialogView.findViewById<Button>(R.id.btnBookNow).setOnClickListener {


            if (selectedTime != null) {
                dialogView.findViewById<TextView>(R.id.txtViewTimeSlotErrorMsg).visibility =
                    View.INVISIBLE
                val intent = Intent(this, SummaryBookDetails::class.java)
                intent.putExtra("selectedDate", btnDate.text)
                intent.putExtra("startTime", selectedTime)
                intent.putExtra("selectedDuration", selectedDuration)
                alertDialog.dismiss()

                saveBookData()

                startActivity(intent)
            } else {
                dialogView.findViewById<TextView>(R.id.txtViewTimeSlotErrorMsg).visibility =
                    View.VISIBLE

            }
        }

        arrayList = ArrayList()
        arrayList = setDataList()
        timerAdapter = TimerAvailableRecycleAdapter(arrayList!!, applicationContext)
        gridView?.adapter = timerAdapter
        gridView?.onItemClickListener = this


        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show()


    }


    //validation for date, time duration, room/court which unable to blank
    private fun validationRequiredField(): Boolean {
        //val requiredErrorMsg = getString(R.string.require_error)
        var pass = true
        if (savedDay != 0 && selectedDuration != null && selectedRoomCourt != null) {
            txtViewDateErrorMsg.visibility = View.INVISIBLE
            txtViewTimeError.visibility = View.INVISIBLE
            txtViewCourtRoomError.visibility = View.INVISIBLE
            validationDate()
            return pass
        } else {
            if (savedDay == 0) {
                // txtViewDateErrorMsg.text = requiredErrorMsg
                txtViewDateErrorMsg.visibility = View.VISIBLE
                pass = false
            } else {
                txtViewDateErrorMsg.visibility = View.INVISIBLE

            }
            if (selectedDuration == null) {
                //txtViewTimeError.text = requiredErrorMsg
                txtViewTimeError.visibility = View.VISIBLE
                pass = false

            } else {
                txtViewTimeError.visibility = View.INVISIBLE

            }

            if (selectedRoomCourt == null) {
                // txtViewCourtRoomError.text = requiredErrorMsg
                txtViewCourtRoomError.visibility = View.VISIBLE
                pass = false
            } else {
                txtViewCourtRoomError.visibility = View.INVISIBLE
            }
        }
        return pass
    }

    //validation for selected Date
    private fun validationDate(): Boolean {

        //first 3 if is checking the date,month,year not lesser than current
        if (year > savedYear) {
            return false
        }
        if (month > savedMonth) {

            return false
        }
        if (day > savedDay) {
            return false
        }

        //if within 30 days then return true
        return when {
            month == savedMonth -> {
                day <= savedDay

            }
            month < savedMonth -> {
                (day + 30) - savedDay == 30
            }

            else -> {
                false
            }
        }

    }

    //change design of card for time duration after card selection
    private fun selectedTimeDrtCard(cv: CardView, tvNum: TextView) {
        cardDefaultView()
        cv.setCardBackgroundColor(Color.parseColor("#969FAA"))
        tvNum.setTextColor(Color.parseColor("#FFFFFFFF"))
        selectedDuration = tvNum.text.toString()
    }

    //change design of card for selection room/court
    private fun selectionRoomCourt(cv: CardView, tvNum: TextView) {
        selectionCardDefView()
        cv.setCardBackgroundColor(Color.parseColor("#969FAA"))
        tvNum.setTextColor(Color.parseColor("#FFFFFFFF"))
        selectedRoomCourt = tvNum.text.toString()

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

    //get current Date on calender
    private fun getDateCalender() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)


        val currentDate = "${day}/${month}/${year}"
        Log.d("currentDate", currentDate)


    }

    //Update the date after selected from calender
    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year


        selectedDate = "${savedDay}_${savedMonth + 1}_${savedYear}"

        Log.d("selectedDate", selectedDate)

        cvtMonth = converter(savedMonth)


        btnDate.text = "$savedDay $cvtMonth $savedYear"
    }

    //convert month in Integer form to String MMM
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

    //assign data for grid view which is time slots available
    private fun setDataList(): ArrayList<TimeSlot>? {

        /*assign data by passing parameter to Sports Model*/
        val arrayList = ArrayList<TimeSlot>()


        if(selectedDuration.equals("60")) {
            arrayList.add(TimeSlot("timeID00", "10:00 AM"))
            arrayList.add(TimeSlot("timeID01", "11:00 AM"))
            arrayList.add(TimeSlot("timeID02", "12:00 PM"))
            arrayList.add(TimeSlot("timeID03", "1:00 PM"))
            arrayList.add(TimeSlot("timeID04", "2:00 PM"))
            arrayList.add(TimeSlot("timeID05", "3:00 PM"))
            arrayList.add(TimeSlot("timeID06", "4:00 PM"))
            arrayList.add(TimeSlot("timeID07", "5:00 PM"))
            arrayList.add(TimeSlot("timeID08", "6:00 PM"))
            arrayList.add(TimeSlot("timeID09", "7:00 PM"))
            arrayList.add(TimeSlot("timeID10", "8:00 PM"))
            arrayList.add(TimeSlot("timeID11", "9:00 PM"))
        }
        else if(selectedDuration.equals("120")){
            arrayList.add(TimeSlot("timeID00", "10:00 AM"))
            arrayList.add(TimeSlot("timeID01", "11:00 AM"))
            arrayList.add(TimeSlot("timeID02", "12:00 PM"))
            arrayList.add(TimeSlot("timeID03", "1:00 PM"))
            arrayList.add(TimeSlot("timeID04", "2:00 PM"))
            arrayList.add(TimeSlot("timeID05", "3:00 PM"))
            arrayList.add(TimeSlot("timeID06", "4:00 PM"))
            arrayList.add(TimeSlot("timeID07", "5:00 PM"))
            arrayList.add(TimeSlot("timeID08", "6:00 PM"))
            arrayList.add(TimeSlot("timeID09", "7:00 PM"))
            arrayList.add(TimeSlot("timeID10", "8:00 PM"))

        }
        else{
            arrayList.add(TimeSlot("timeID00", "10:00 AM"))
            arrayList.add(TimeSlot("timeID01", "11:00 AM"))
            arrayList.add(TimeSlot("timeID02", "12:00 PM"))
            arrayList.add(TimeSlot("timeID03", "1:00 PM"))
            arrayList.add(TimeSlot("timeID04", "2:00 PM"))
            arrayList.add(TimeSlot("timeID05", "3:00 PM"))
            arrayList.add(TimeSlot("timeID06", "4:00 PM"))
            arrayList.add(TimeSlot("timeID07", "5:00 PM"))
            arrayList.add(TimeSlot("timeID08", "6:00 PM"))
            arrayList.add(TimeSlot("timeID09", "7:00 PM"))
        }


        return arrayList
    }

    //click on the time slots available
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        if (previousPosition >= 0) {
            timeCardDefaultView(previousParent, previousPosition)
        }

        val v: View = parent!!.getChildAt(position)

        v.findViewById<LinearLayout>(R.id.linearLayout_slots_available_time)
            .setBackgroundResource(R.drawable.clicked_time_border_outline)
        v.findViewById<TextView>(R.id.txtViewTime).setTextColor(Color.parseColor("#FFFFFFFF"))

        previousPosition = position
        selectedTime = v.findViewById<TextView>(R.id.txtViewTime).text.toString()
        selectedTimeSlot = id
        Log.d("selected ID", selectedTimeSlot.toString())
        previousParent = parent


    }

    //reset time card default view
    private fun timeCardDefaultView(parent: AdapterView<*>?, position: Int) {

        val v: View = parent!!.getChildAt(position)
        v.findViewById<LinearLayout>(R.id.linearLayout_slots_available_time)
            .setBackgroundResource(R.drawable.default_time_border_outline)
        v.findViewById<TextView>(R.id.txtViewTime).setTextColor(Color.parseColor("#FF000000"))
        selectedTime = v.findViewById<TextView>(R.id.txtViewTime).text.toString()


    }

    //format id eg 1 --> 01
    fun formatID(id: Long?): String? {

        var selectedTimeSlot1: String? = null
        if (id != null) {
            if (id < 10) {

                selectedTimeSlot1 = "0$id"


            } else {
                selectedTimeSlot1 = id.toString()
            }
        }

        return selectedTimeSlot1

    }

    //post booked data
    private fun saveBookData() {
        var cvtToHours = selectedDuration?.toInt()
        cvtToHours = cvtToHours?.div(60)
        if (cvtToHours!! > 1) {
            var counter = 0
            while (counter < cvtToHours) {
                FirestoreClass().saveBookedData(
                    selectedTimeSlot, selectedTime,
                    selectedDate, selectedRoomCourt, aBarTitle, type
                )
                counter++
                selectedTimeSlot = selectedTimeSlot?.plus(1)
                selectedTime = SummaryBookDetails().sumHours(1, selectedTime.toString())


            }
        } else {
            FirestoreClass().saveBookedData(
                selectedTimeSlot, selectedTime,
                selectedDate, selectedRoomCourt, aBarTitle, type
            )
        }

    }

    //read booked data
    private fun retrieveBookedData() {
        FirestoreClass().retrieveBookedData(
            selectedDate,
            selectedRoomCourt,
            aBarTitle,
            type
        )

    }


}