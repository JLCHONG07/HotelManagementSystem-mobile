package com.example.hotelmanagementsystem_mobile.models.booking_details

import android.os.Parcel
import android.os.Parcelable

data class CheckOutDetails(
    val checkOutID : String = "",
    val checkOutDateTime : String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(checkOutID)
        parcel.writeString(checkOutDateTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CheckOutDetails> {
        override fun createFromParcel(parcel: Parcel): CheckOutDetails {
            return CheckOutDetails(parcel)
        }

        override fun newArray(size: Int): Array<CheckOutDetails?> {
            return arrayOfNulls(size)
        }
    }
}