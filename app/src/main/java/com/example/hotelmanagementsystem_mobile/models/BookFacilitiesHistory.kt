package com.example.hotelmanagementsystem_mobile.models

import android.os.Parcel
import android.os.Parcelable

class BookFacilitiesHistory(val imageCat: Int, val catAndDuration: String, val time: String, val courtRoom: String, val color: Int, val weekOfDay: String, val monthOfDate:String,val month:String ) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(imageCat)
        parcel.writeString(catAndDuration)
        parcel.writeString(time)
        parcel.writeString(courtRoom)
        parcel.writeInt(color)
        parcel.writeString(weekOfDay)
        parcel.writeString(monthOfDate)
        parcel.writeString(month)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookFacilitiesHistory> {
        override fun createFromParcel(parcel: Parcel): BookFacilitiesHistory {
            return BookFacilitiesHistory(parcel)
        }

        override fun newArray(size: Int): Array<BookFacilitiesHistory?> {
            return arrayOfNulls(size)
        }
    }
}