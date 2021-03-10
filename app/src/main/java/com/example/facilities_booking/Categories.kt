package com.example.facilities_booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.R
import kotlinx.android.synthetic.main.activity_categories.*

class Categories : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        val arrayList = ArrayList<Model>()
        /*add the data to arrayList of Model (Categories)*/
        arrayList.add(Model( "Sports", R.drawable.categories_sports))
        arrayList.add(Model( "Board Game", R.drawable.categories_board_game))
        arrayList.add(Model("Gaming Room", R.drawable.categories_gaming_rooms))

        /*pass data with parameter*/
        val categoriesRecycleAdapater= CategoriesRecycleAdapter(arrayList,this)
        recycleViewCategories.layoutManager=LinearLayoutManager(this)
        recycleViewCategories.adapter=categoriesRecycleAdapater
    }
}
