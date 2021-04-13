package com.example.hotelmanagementsystem_mobile.models

import android.os.Parcel
import android.os.Parcelable

data class categories(val cat_name:String,val _image:Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cat_name)
        parcel.writeInt(_image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<categories> {
        override fun createFromParcel(parcel: Parcel): categories {
            return categories(parcel)
        }

        override fun newArray(size: Int): Array<categories?> {
            return arrayOfNulls(size)
        }
    }

}