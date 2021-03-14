package com.example.hotelmanagementsystem_mobile.activities.facilities_booking
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelmanagementsystem_mobile.R
import com.example.hotelmanagementsystem_mobile.activities.Homepage
import com.example.hotelmanagementsystem_mobile.adapters.CategoriesRecycleAdapter
import com.example.hotelmanagementsystem_mobile.models.Model
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

        val categoriesRecycleAdapater = CategoriesRecycleAdapter(arrayList,this@Categories)

        recycleViewCategories.layoutManager=LinearLayoutManager(this)
        recycleViewCategories.adapter=categoriesRecycleAdapater
    }

    /*override fun onBackPressed() {
        val intent = Intent (this, Homepage::class.java)
        startActivity(intent)
    }*/
}
