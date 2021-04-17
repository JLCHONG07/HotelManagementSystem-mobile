package com.example.hotelmanagementsystem_mobile.models.booking_details

import android.os.Parcel
import android.os.Parcelable

data class CheckInDetails(
    val checkedInUser : ArrayList<String> = ArrayList(),
    val checkInID : String = "",
    val checkInDateAndTime : String = "",
    val checkInStatus : String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createStringArrayList()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(checkedInUser)
        parcel.writeString(checkInID)
        parcel.writeString(checkInDateAndTime)
        parcel.writeString(checkInStatus)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CheckInDetails> {
        override fun createFromParcel(parcel: Parcel): CheckInDetails {
            return CheckInDetails(parcel)
        }

        override fun newArray(size: Int): Array<CheckInDetails?> {
            return arrayOfNulls(size)
        }
    }
}