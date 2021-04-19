package com.example.hotelmanagementsystem_mobile.models.booking_details

import android.os.Parcel
import android.os.Parcelable

data class RoomReservationDetails(
    val reservationDateTime : String = "",
    val numberOfDays : Int = 0,
    val numberOfNights : Int = 0,
    val numberOfRooms : Int = 0,
    val numberOfGuests : Int = 0,
    val roomTypes : String = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt()!!,
        parcel.readInt()!!,
        parcel.readInt()!!,
        parcel.readInt()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(reservationDateTime)
        parcel.writeInt(numberOfDays)
        parcel.writeInt(numberOfNights)
        parcel.writeInt(numberOfRooms)
        parcel.writeInt(numberOfGuests)
        parcel.writeString(roomTypes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RoomReservationDetails> {
        override fun createFromParcel(parcel: Parcel): RoomReservationDetails {
            return RoomReservationDetails(parcel)
        }

        override fun newArray(size: Int): Array<RoomReservationDetails?> {
            return arrayOfNulls(size)
        }
    }
}