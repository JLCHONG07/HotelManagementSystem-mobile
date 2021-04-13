package com.example.hotelmanagementsystem_mobile.models

import android.os.Parcel
import android.os.Parcelable

data class Sport(val sportsName:String,val sportsImage:Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(sportsName)
        parcel.writeInt(sportsImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Sport> {
        override fun createFromParcel(parcel: Parcel): Sport {
            return Sport(parcel)
        }

        override fun newArray(size: Int): Array<Sport?> {
            return arrayOfNulls(size)
        }
    }
}