package com.example.hotelmanagementsystem_mobile.activities.facilities_booking

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import kotlinx.android.synthetic.main.activity_admin_facilities_booking.*
import kotlinx.android.synthetic.main.activity_booking_available.*
import kotlinx.android.synthetic.main.activity_booking_available.btnDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


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
    private var savedHour = 0
    private var savedMinute = 0
    private var currentCat: String? = null
    private var currentType: String? = null
    private var arrayList: ArrayList<TimeSlot>? = null
    private var timerAdapter: TimerAvailableRecycleAdapter? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var alertDialog: AlertDialog

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_available)

        Log.d("onCreate", "onCreate")
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        val intent = intent
        aBarTitle = intent.getStringExtra("aBarTitle")
        type = intent.getStringExtra("type")
        val selection = getString(R.string.selection)

        if (type == null) {

            sharedPreferences =
                getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
            aBarTitle = sharedPreferences.getString("aBarTitle", aBarTitle)
            type = sharedPreferences.getString("type", type)
            btnDate.text = sharedPreferences.getString("btnDate", btnDate.text.toString())
            selectedDate = sharedPreferences.getString("selectedDate", selectedDate)
            savedDay = sharedPreferences.getInt("savedDay", savedDay)
           cvtMonth=sharedPreferences.getString("cvtMonth",cvtMonth)
        }

        txtViewCourtRoom.text = "$selection $type :"
        txtViewSelection1.text = type
        txtViewSelection2.text = type
        actionBar.title = aBarTitle
        currentCat = actionBar.title.toString()
        currentType = txtViewSelection1.text.toString()

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        aBarTitle = sharedPreferences.getString("aBarTitle", aBarTitle)
        type = sharedPreferences.getString("type", type)

        cardView60Minutes.setOnClickListener(this)
        cardView120Minutes.setOnClickListener(this)
        cardView180Minutes.setOnClickListener(this)
        cardViewSelection1.setOnClickListener(this)
        cardViewSelection2.setOnClickListener(this)
        btnCheckAvailable.setOnClickListener(this)

        pickDate()
    }

    override fun onStop()
    {
        super.onStop()
       // Log.d("onStop", "Stop")
        with(sharedPreferences.edit()) {
            putString("aBarTitle", currentCat).apply()
            putString("type", txtViewSelection1.text.toString()).apply()
            putString("btnDate", btnDate.text.toString()).apply()
            putString("selectedDate", selectedDate).apply()
            putInt("savedDay", savedDay).apply()
            putString("cvtMonth",cvtMonth).apply()
        }
    }

    /* Back to previous activity*/
    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        finish()
        return true

    }

/*    override fun onPause() {

        super.onPause()
        Log.d("onPause", "onPause")
        with(sharedPreferences.edit()) {
            putString("aBarTitle", intent.getStringExtra("aBarTitle")).apply()
            putString("type", intent.getStringExtra("type")).apply()
        }
    }*/

/*    override fun onStart() {

        super.onStart()
        Log.d("onStart", "onStart")
        //Log.d("onPause", "onPause")
        with(sharedPreferences.edit()) {
            putString("aBarTitle1", intent.getStringExtra("aBarTitle")).apply()
            putString("type1", intent.getStringExtra("type")).apply()
        }
    }*/

    //---------------------UI---------------------------//
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
                //check is all fields alrdy selected eg. date, time, duration
                validationRequiredField()
                if (validationRequiredField()) {
                    //check the date selected is within 30 days ranges
                    if (validationDate()) {

                        txtViewDateErrorMsg.visibility = View.INVISIBLE
                        retrieveBookedData()

                    } else {

                        val dateError = getString(R.string.date_error)
                        txtViewDateErrorMsg.text = dateError
                        txtViewDateErrorMsg.visibility = View.VISIBLE

                    }
                }
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

    //create dialog after click on Check Available button when full
    private fun showCustomDialogFull() {

        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.booking_full, null)
        dialogView.findViewById<Button>(R.id.btnOK).setOnClickListener {

            alertDialog.dismiss()

        }
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        dialogBuilder.setView(dialogView)
        alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false);

    }


    //create dialog after click on Check Available button when not full
    @SuppressLint("SetTextI18n")
    private fun showCustomDialogAvailable(timeSlot: MutableMap<String, Any>) {

        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.slot_available_dialog, null)
        val titleSlotAvailable =
            dialogView.findViewById<TextView>(R.id.txtViewWordSlotAvailable)
        titleSlotAvailable.text = "Slot Available"
        val txtViewselectedDate = dialogView.findViewById<TextView>(R.id.txtViewSelectedDate)
        txtViewselectedDate.text = "$cvtMonth $savedDay"
        val gridView = dialogView.findViewById<GridView>(R.id.slot_available_time)
        dialogView.findViewById<Button>(R.id.btnBookNow).setOnClickListener {

            if (selectedTime != null) {
                dialogView.findViewById<TextView>(R.id.txtViewTimeSlotErrorMsg).visibility =
                    View.INVISIBLE
                val intent = Intent(this, SummaryBookDetails::class.java)
                // Usable data for storing history and as summary details data
                intent.putExtra("selectedDate", selectedDate)
                intent.putExtra("startTime", selectedTime)
                intent.putExtra("selectedDuration", selectedDuration)
                intent.putExtra("selectedTimeSlot", selectedTimeSlot)
                intent.putExtra("selectedRoomCourt", selectedRoomCourt)
                intent.putExtra("currentCat", currentCat)
                intent.putExtra("currentType", currentType)
                intent.putExtra("savedDay", savedDay)
                intent.putExtra("savedMonth", savedMonth)
                intent.putExtra("savedYear", savedYear)
                intent.putExtra("cvtMonth", cvtMonth)
                alertDialog.dismiss()
                startActivity(intent)

            } else {

                dialogView.findViewById<TextView>(R.id.txtViewTimeSlotErrorMsg).visibility =
                    View.VISIBLE

            }
        }
        arrayList = ArrayList()
        arrayList = setDataList(timeSlot)
        // a small checking for added arraylist size for slots grid view,
        // if it is empty after get from setDataList(timeSlot) which mean it is full and no more slots
        if (arrayList!!.size > 0) {

            timerAdapter = TimerAvailableRecycleAdapter(arrayList!!, this@BookingAvailable)
            gridView?.adapter = timerAdapter
            gridView?.onItemClickListener = this
            val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
            dialogBuilder.setView(dialogView)
            alertDialog = dialogBuilder.create()
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.show()

        } else {

            showCustomDialogFull()

        }
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
        Log.d("selectedTime", selectedTime)
        selectedTimeSlot = convertIDtoLong(selectedTime!!)
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
        //Log.d("selectedTime", selectedTime)
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
        savedHour = cal.get(Calendar.HOUR_OF_DAY)
        savedMinute = cal.get(Calendar.MINUTE)

        val currentDate = "${day}/${month}/${year}"
        //Log.d("currentHour", savedHour.toString())
        // Log.d("currentMinute", savedMinute.toString())
        //Log.d("currentDate", currentDate)

    }

    //Update the date after selected from calender
    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year
        selectedDate = "${savedDay}_${savedMonth + 1}_${savedYear}"
        // Log.d("selectedDate", selectedDate)
        cvtMonth = converter(savedMonth)
        btnDate.text = "$savedDay $cvtMonth $savedYear"

    }

    //----------------------Validation------------------------------//

    //validation for date, time duration, room/court which unable to blank
    private fun validationRequiredField(): Boolean {

        var pass = true
        if (savedDay != 0 && selectedDuration != null && selectedRoomCourt != null) {

            txtViewDateErrorMsg.visibility = View.INVISIBLE
            txtViewTimeError.visibility = View.INVISIBLE
            txtViewCourtRoomError.visibility = View.INVISIBLE
            validationDate()
            return pass

        } else {

            if (savedDay == 0) {

                txtViewDateErrorMsg.visibility = View.VISIBLE
                pass = false

            } else {

                txtViewDateErrorMsg.visibility = View.INVISIBLE

            }
            if (selectedDuration == null) {

                txtViewTimeError.visibility = View.VISIBLE
                pass = false

            } else {

                txtViewTimeError.visibility = View.INVISIBLE

            }

            if (selectedRoomCourt == null) {

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

        //if within 30 days then return true
        return when {

            month == savedMonth -> {
                day <= savedDay

            }
            month < savedMonth -> {

                (savedDay + 30) - day <= 30

            }

            else -> {

                false

            }
        }
    }

    //----------------------Checking Time Slots--------------------//
    // retrieved data and called from fireStore class
    fun checkSlotAvailable(timeSlot: MutableMap<String, Any>) {

        val slotAvailability = checkForSlotSize(timeSlot)
        //if not slots available then call the full alertbox
        if (!slotAvailability) {

            showCustomDialogFull()

        }
    }

    //check for the booked slots time
    private fun checkForSlotSize(timeSlot: MutableMap<String, Any>): Boolean {

        var slotAvailability: Boolean = false
        //Log.d("size", timeSlot.size.toString())
        //check is it full slot, max slots for time is 12
        if (timeSlot.size != 12) {

            slotAvailability = checkForCurrent()

            if (slotAvailability) {
                //making fake data with hashmap and use to compare with arraylist
                assignFakeData(timeSlot)

            } else {

                return slotAvailability

            }

        } else {

            return slotAvailability

        }
        return true
    }

    // check for close time as 10:00 AM to 10:00 PM of facilities opening
    private fun checkForCurrent(): Boolean {

        //check for current day slot available for time
        if (day == savedDay) {
            //duration is 60 minutes but current hour is more than 9:00pm
            if (selectedDuration.equals("60") && savedHour >= 21 && savedMinute > 0) {
                return false

            }
            //duration is 120 minutes but current hour is more than 8:00pm
            else if (selectedDuration.equals("120") && savedHour >= 20 && savedMinute > 0) {
                return false

            }
            //duration is 120 minutes but current hour is more than 7:00pm
            else if (selectedDuration.equals("180") && savedHour >= 19 && savedMinute > 0) {
                return false

            }
        }
        return true
    }

    //check for booking time
    private fun assignFakeData(timeSlot: MutableMap<String, Any>) {
        //selected date is not current date or same date before 9AM
        if ((savedHour < 9 && day == savedDay) || day < savedDay) {

            showCustomDialogAvailable(timeSlot)

        }
        // this is current date from 10:00 AM to 10:00 PM booking, as the time will passed even it is not booked
        else {

            var hourForID = savedHour - 10
           // Log.d("hourForID", hourForID.toString())
            var currentTimeSlot = formatID(hourForID.toLong())
            var addNewTime = "timeID$currentTimeSlot"
            //currentHour - 10 to get the range from start to current end time booking
            var startTimeSlot = savedHour - 10
           // Log.d("addNewTime", addNewTime)
           // Log.d("startTimeSlot", startTimeSlot.toString())
            var currentHour = savedHour
            var currentTimer = cvtTo12Hours(currentHour)
            // Log.d("currentTime2", currentTimer)
            while (startTimeSlot >= 0) {

                //if this slot didnot booked and the time alrdy pass eg current time is 4:00PM but 11:00AM to 13:00PM slot is empty,
                    // required add to timeSlot hashmap for comparing later
                if (!timeSlot.containsKey(addNewTime)) {

                    timeSlot.put(addNewTime, currentTimer)

                }
                startTimeSlot -= 1
                hourForID -= 1
                currentHour -= 1
                currentTimer = cvtTo12Hours(currentHour)
                currentTimeSlot = formatID(hourForID.toLong())
                addNewTime = "timeID$currentTimeSlot"

            }
            showCustomDialogAvailable(timeSlot)
            /* for (key in timeSlot.keys) {
                 Log.d("timerID", key)
                 Log.d("time", timeSlot[key].toString())
             }*/
        }
    }

    //assign data for grid view which is time slots available
    private fun setDataList(timeSlot: MutableMap<String, Any>): ArrayList<TimeSlot> {

        // assign data to arratList by following the selected duration which use for grid view display
        val arrayList = ArrayList<TimeSlot>()
        val arrayListResult = ArrayList<TimeSlot>()

        when {

            selectedDuration.equals("60") -> {

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
            selectedDuration.equals("120") -> {

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
            else -> {

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
        }

        // below are checking arrayList assigned above compare with hashMap timeSlot
        // If exists will not add to the arrayListResult
        when {
            selectedDuration.equals("60") -> {

                for (arrayListTem in arrayList.indices) {

                    if (!timeSlot.containsKey(arrayList[arrayListTem].timerID)) {

                        arrayListResult.add(
                            TimeSlot(
                                arrayList[arrayListTem].timerID,
                                arrayList[arrayListTem].timer
                            )
                        )
                    }
                }
            }

            selectedDuration.equals("120") -> {

                for (arrayListTem in arrayList.indices) {

                    if (!timeSlot.containsKey(arrayList[arrayListTem].timerID)) {

                        //check for next hours id exists
                        if (arrayListTem + 1 < arrayList.size) {

                            if (!timeSlot.containsKey(arrayList[arrayListTem + 1].timerID)) {

                                arrayListResult.add(
                                    TimeSlot(
                                        arrayList[arrayListTem].timerID,
                                        arrayList[arrayListTem].timer
                                    )
                                )

                            }
                            //if not the last position then will check with the exists id
                        } else {

                            if (!timeSlot.containsKey(arrayList[arrayListTem].timerID)) {

                                arrayListResult.add(
                                    TimeSlot(
                                        arrayList[arrayListTem].timerID,
                                        arrayList[arrayListTem].timer
                                    )
                                )

                            }
                        }

                    }
                }
            }

            else -> {

                for (arrayListTem in arrayList.indices) {
                    //check for current hour
                    if (!timeSlot.containsKey(arrayList[arrayListTem].timerID)) {
                        //check for next hours and next two hours exists by id
                        var i = 1
                        while (i <= 2) {
                            //check is it no more array behind eg. total size is 12 current size is 10,
                            // if +1 still within the size then continue else
                            if (arrayListTem + i < arrayList.size) {
                                //check is it next hours/ next two hours exists by id
                                if (!timeSlot.containsKey(arrayList[arrayListTem + i].timerID)) {
                                    //if next hours and next two hours is not exists and current checking is two times then add
                                    if (i == 2) {

                                        arrayListResult.add(
                                            TimeSlot(
                                                arrayList[arrayListTem].timerID,
                                                arrayList[arrayListTem].timer
                                            )
                                        )

                                    }
                                    i++
                                    //break if next / next two hours exists
                                } else {

                                    break
                                }
                                //else come here for check is it current hours is exist else break
                            } else {
                                if (!timeSlot.containsKey(arrayList[arrayListTem].timerID)) {
                                    arrayListResult.add(
                                        TimeSlot(
                                            arrayList[arrayListTem].timerID,
                                            arrayList[arrayListTem].timer
                                        )
                                    )

                                }
                                //break to go next array
                                break

                            }

                        }

                    }

                }

            }

        }

        return arrayListResult

    }
    //-------------Converter/Formatter--------------------//
    //24 hours format to 12 hours format eg 13:00 to 1:00 PM
    private fun cvtTo12Hours(currentHour: Int): String {

        var currentHour = currentHour
        var result: String? = null
        // 10 AM to 11 AM
        if (currentHour in 10..11) {

            result = "$currentHour:00 AM"

        } else if (currentHour == 12) {

            result = "$currentHour:00 PM"

        } else {

            currentHour -= 12
            result = "$currentHour:00 PM"

        }

        //Log.d("currentTime", result)
        return result

    }

    //convert month in Integer form to String MMM
    fun converter(month: Int?): String {

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

    //get id from time text view eg 11:00 AM --> "11" with return long var
    private fun convertIDtoLong(selectedTime: String): Long {
        //get hours of selectedTime
        var i = 0
        val startTimeLength = selectedTime.length
        var startHours: String? = ""
        while (i < startTimeLength) {

            val currentChar: Char = selectedTime.get(i)
            if (currentChar.compareTo(':') != 0) {

                startHours += currentChar
                i++

            } else {

                break

            }

        }
        var bookedTime = startHours!!.toLong()
        if (bookedTime < 10) {

            bookedTime += 12 - 10
        } else {

            bookedTime -= 10

        }
        return bookedTime
    }

    //format id eg 1 --> 01
    fun formatID(id: Long?): String? {

        var selectedTimeSlot: String? = null
        if (id != null) {

            if (id < 10) {

                selectedTimeSlot = "0$id"
                //Log.d("selectedTimeSlot1", selectedTimeSlot)

            } else {

                selectedTimeSlot = id.toString()

            }

        }

        return selectedTimeSlot
    }

    //-----------------Get Data from firebase calling fireStoreClass---------//
    //read booked data
    private fun retrieveBookedData() {

        Log.d("currentCat", currentCat)
        FirestoreClass().retrieveBookedData(
            this, selectedDate,
            selectedRoomCourt,
            currentCat,
            currentType
        )

    }

}