package com.example.hotelmanagementsystem_mobile.models

import android.os.Parcel
import android.os.Parcelable

data class CheckIn(
    val reservationNumber : String = "",
    val numberOfDays : Int = 0,
    val numberOfNights : Int = 0,
    val numberOfRooms : Int = 0,
    val numberOfQuests : Int = 0,
    val roomTypes : String = "",
    val checkInId : String = "",
    val checkInDateAndTime : String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt()!!,
        parcel.readInt()!!,
        parcel.readInt()!!,
        parcel.readInt()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, p1: Int) = with(dest) {
        writeString(reservationNumber)
        writeInt(numberOfDays)
        writeInt(numberOfNights)
        writeInt(numberOfQuests)
        writeInt(numberOfRooms)
        writeString(roomTypes)
        writeString(checkInId)
        writeString(checkInDateAndTime)
    }

    companion object CREATOR : Parcelable.Creator<CheckIn> {
        override fun createFromParcel(parcel: Parcel): CheckIn {
            return CheckIn(parcel)
        }

        override fun newArray(size: Int): Array<CheckIn?> {
            return arrayOfNulls(size)
        }
    }
}