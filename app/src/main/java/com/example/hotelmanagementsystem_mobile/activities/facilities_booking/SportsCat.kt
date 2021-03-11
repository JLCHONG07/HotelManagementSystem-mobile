package com.example.hotelmanagementsystem_mobile.activities.facilities_booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.adapters.SportsRecycleAdapter
import com.example.hotelmanagementsystem_mobile.models.ModelSport

class SportsCat : AppCompatActivity(),AdapterView.OnItemClickListener{

    private var arrayList:ArrayList<ModelSport>?=null
    private var gridView:GridView?=null
    private var sportsRecycleAdapter: SportsRecycleAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sports_cat)


        gridView = findViewById(R.id.sports_cat_grid_view)
        arrayList = ArrayList()
        arrayList=setDataList()
        sportsRecycleAdapter = SportsRecycleAdapter(arrayList!!, applicationContext)
        gridView?.adapter = sportsRecycleAdapter
        gridView?.onItemClickListener=this
    }

    private fun setDataList(): ArrayList<ModelSport>? {

        /*assign data by passing parameter to Sports Model*/
       var arrayList=ArrayList<ModelSport>()

        arrayList.add(ModelSport("Badminton",R.drawable.sports_badminton))
        arrayList.add(ModelSport("Table Tennis",R.drawable.sports_table_tennis))
        arrayList.add(ModelSport("Snooker",R.drawable.sports_snooker))



        return arrayList
    }


    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }
}