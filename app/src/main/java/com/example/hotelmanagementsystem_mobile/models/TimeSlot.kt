package com.example.hotelmanagementsystem_mobile.models

import android.os.Parcel
import android.os.Parcelable

data class TimeSlot(val timerID:String,val timer:String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(timerID)
        parcel.writeString(timer)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TimeSlot> {
        override fun createFromParcel(parcel: Parcel): TimeSlot {
            return TimeSlot(parcel)
        }

        override fun newArray(size: Int): Array<TimeSlot?> {
            return arrayOfNulls(size)
        }
    }

}