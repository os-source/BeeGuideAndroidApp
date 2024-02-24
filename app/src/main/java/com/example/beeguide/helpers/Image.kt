package com.example.beeguide.helpers

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun imageBitmapFromBase64String(base64ImageString: String): ImageBitmap {
    val byteArray = Base64.decode(base64ImageString, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

    return bitmap.asImageBitmap()
}