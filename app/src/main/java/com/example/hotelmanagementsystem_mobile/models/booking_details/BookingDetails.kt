package com.example.hotelmanagementsystem_mobile.models.booking_details

import android.os.Parcel
import android.os.Parcelable

data class BookingDetails(
    var bookingID : String = "",
    val reservationID : String = "",
    val room_reservation_details: ArrayList<RoomReservationDetails> = ArrayList(),
    var check_in_details : ArrayList<CheckInDetails> = ArrayList(),
    //TODO: add facilities booking

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(RoomReservationDetails.CREATOR)!!,
        parcel.createTypedArrayList(CheckInDetails.CREATOR)!!
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, p1: Int) = with(dest) {
        writeString(bookingID)
        writeString(reservationID)
        writeTypedList(room_reservation_details)
        writeTypedList(check_in_details)
    }

    companion object CREATOR : Parcelable.Creator<BookingDetails> {
        override fun createFromParcel(parcel: Parcel): BookingDetails {
            return BookingDetails(parcel)
        }

        override fun newArray(size: Int): Array<BookingDetails?> {
            return arrayOfNulls(size)
        }
    }
}