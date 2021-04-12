package com.example.hotelmanagementsystem_mobile.models

data class Model(val cat_name:String,val _image:Int)
data class ModelSport(val sportsName:String,val sportsImage:Int)
data class ModelTimer(val timerID:String,val timer:String)
data class ModelVoucher(val _image:Int,val timeDuration:Int, val vouchType:String, val vouchCode:String,val vouchCat: String )