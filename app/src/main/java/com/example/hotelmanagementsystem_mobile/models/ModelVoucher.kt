package com.example.hotelmanagementsystem_mobile.models

import android.os.Parcel
import android.os.Parcelable
import com.example.hotelmanagementsystem_mobile.activities.facilities_booking.BookingAvailable


data class ModelVoucher(
    val _image:Int,
    val timeDuration: String="", val vouchType: String="", val vouchCode: String="",
    val vouchCat: String="",

    val userID:String,
    val voucherID:String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(_image)
        parcel.writeString(timeDuration)
        parcel.writeString(vouchType)
        parcel.writeString(vouchCode)
        parcel.writeString(vouchCat)
        parcel.writeString(userID)
        parcel.writeString(voucherID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelVoucher> {
        override fun createFromParcel(parcel: Parcel): ModelVoucher {
            return ModelVoucher(parcel)
        }

        override fun newArray(size: Int): Array<ModelVoucher?> {
            return arrayOfNulls(size)
        }
    }
}