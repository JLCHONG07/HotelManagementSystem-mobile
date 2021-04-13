package com.example.hotelmanagementsystem_mobile.activities.facilities_booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import androidx.appcompat.app.ActionBar
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.adapters.SportsRecycleAdapter
import com.example.hotelmanagementsystem_mobile.models.Sport

class SportsCat : AppCompatActivity(),AdapterView.OnItemClickListener{

    private var arrayList:ArrayList<Sport>?=null
    private var gridView:GridView?=null
    private var sportsRecycleAdapter: SportsRecycleAdapter?=null
    private var type:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sports_cat)

        val actionBar: ActionBar?=supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayShowHomeEnabled(true)
        var intent=intent
        val aBarTitle=intent.getStringExtra("aBarTitle")
        type=intent.getStringExtra("type")
        actionBar!!.title = aBarTitle

        gridView = findViewById(R.id.sports_cat_grid_view)
        arrayList = ArrayList()
        arrayList=setDataList()
        sportsRecycleAdapter = SportsRecycleAdapter(arrayList!!, applicationContext)
        gridView?.adapter = sportsRecycleAdapter
        gridView?.onItemClickListener=this
    }

    private fun setDataList(): ArrayList<Sport>? {

        /*assign data by passing parameter to Sports Model*/
       val arrayList=ArrayList<Sport>()

        arrayList.add(Sport("Badminton",R.drawable.sports_badminton))
        arrayList.add(Sport("Table Tennis",R.drawable.sports_table_tennis))
        arrayList.add(Sport("Snooker",R.drawable.sports_snooker))



        return arrayList
    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        val cardPos= arrayList?.get(position)
        val actionBarTitle:String= cardPos!!.sportsName

        val intent= Intent(this@SportsCat,BookingAvailable::class.java)
        intent.putExtra("aBarTitle",actionBarTitle)
        intent.putExtra("type",type)
        startActivity(intent)


    }
}