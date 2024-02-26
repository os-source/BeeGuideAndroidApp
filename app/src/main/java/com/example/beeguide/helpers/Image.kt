package com.example.beeguide.helpers

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun imageBitmapFromBase64String(base64ImageString: String): ImageBitmap {
    val byteArray = Base64.decode(base64ImageString, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

    return bitmap.asImageBitmap()
}

fun uriToFile(contentResolver: ContentResolver, uri: Uri): File? {
    val inputStream: InputStream? = contentResolver.openInputStream(uri)
    val fileName = "${System.currentTimeMillis()}_image.jpg"
    val file = File.createTempFile(fileName, null)
    val outputStream = FileOutputStream(file)
    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
    return file
}