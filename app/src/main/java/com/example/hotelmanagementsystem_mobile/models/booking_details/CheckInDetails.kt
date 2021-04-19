package com.example.hotelmanagementsystem_mobile.models.booking_details

import android.os.Parcel
import android.os.Parcelable

data class CheckInDetails(
    val checkInID : String = "",
    val checkInDateAndTime : String = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(checkInID)
        parcel.writeString(checkInDateAndTime)
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