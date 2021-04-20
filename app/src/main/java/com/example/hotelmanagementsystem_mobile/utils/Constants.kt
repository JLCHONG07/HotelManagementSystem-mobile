package com.example.hotelmanagementsystem_mobile.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    //users
    const val USERS : String = "users"
    const val NAME : String = "name"
    const val IMAGE : String = "image"
    const val EMAIL : String = "email"
    const val PASSPORT : String = "passportNumber"
    const val ASSIGNED_TO : String = "assignedTo"
    const val READ_STORAGE_PERMISSION_CODE = 1
    const val PICK_IMAGE_REQUEST_CODE = 2

    //Check In Activity constants
    const val BOOKING_DETAILS = "booking_details"
    //booking_details
    const val BOOKING_ID : String = "bookingID"
    //room reservation details
    const val RESERVATION_ID : String = "reservationID"
    const val ROOM_RESERVATION_DETAILS_PATH : String = "room_reservation_details"
    //check in details
    const val CHECK_IN_DETAILS_PATH : String = "check_in_details"
    const val CHECKED_IN_USER : String = "checkedInUser"
    const val STATUS : String = "status"
    //Check out details
    const val CHECK_OUT_DETAILS_PATH : String = "check_out_details"

    //facilities_booking
    const val FACILITIES_BOOKING:String="facilities_booking"

    //booking history
    const val BOOKING_HISTORY:String="booking_history"


    fun showImageChooser(activity: Activity) {
        var galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?) : String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }


}