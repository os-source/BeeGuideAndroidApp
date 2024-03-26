package com.example.beeguide.helpers

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.caverock.androidsvg.SVG
import java.io.ByteArrayInputStream
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

fun svgStringToImageBitmap(svgString: String): ImageBitmap {
    // Convert SVG string to InputStream
    val inputStream = ByteArrayInputStream(svgString.toByteArray())

    // Parse SVG from InputStream
    val svg = SVG.getFromInputStream(inputStream)

    // Render SVG to bitmap
    val bitmap = Bitmap.createBitmap(
        svg.documentWidth.toInt(),
        svg.documentHeight.toInt(),
        Bitmap.Config.ARGB_8888
    )

    val canvas = android.graphics.Canvas(bitmap)

    svg.renderToCanvas(canvas)

    // Convert bitmap to ImageBitmap
    val imageBitmap = bitmap.asImageBitmap()

    return imageBitmap
}