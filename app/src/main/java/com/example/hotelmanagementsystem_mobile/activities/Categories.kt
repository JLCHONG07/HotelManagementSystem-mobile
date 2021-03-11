package com.example.hotelmanagementsystem_mobile.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.adapters.CategoriesRecycleAdapater
import com.example.hotelmanagementsystem_mobile.models.Model
import com.example.hotelmanagementsystem_mobile.R
import kotlinx.android.synthetic.main.activity_categories.*

class Categories : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        val arrayList = ArrayList<Model>()
        
        arrayList.add(Model( "Sports", R.drawable.categories_sports))
        arrayList.add(Model( "Board Game", R.drawable.categories_board_game))
        arrayList.add(Model("Gaming Room", R.drawable.categories_gaming_rooms))
        arrayList.add(Model( "Sports", R.drawable.categories_sports))
        arrayList.add(Model( "Board Game", R.drawable.categories_board_game))
        arrayList.add(Model("Gaming Room", R.drawable.categories_gaming_rooms))
        arrayList.add(Model( "Sports", R.drawable.categories_sports))
        arrayList.add(Model( "Board Game", R.drawable.categories_board_game))
        arrayList.add(Model("Gaming Room", R.drawable.categories_gaming_rooms))

        val categoriesRecycleAdapater= CategoriesRecycleAdapater(arrayList,this)
        recycleViewCategories.layoutManager=LinearLayoutManager(this)
        recycleViewCategories.adapter=categoriesRecycleAdapater
    }
}
