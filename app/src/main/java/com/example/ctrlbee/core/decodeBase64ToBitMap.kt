package com.example.ctrlbee.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

fun decodeBase64ToBitMap(encodedImage: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: IllegalArgumentException) {
        null
    }
}
