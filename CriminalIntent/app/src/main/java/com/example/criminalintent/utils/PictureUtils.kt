package com.example.criminalintent.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.math.roundToInt

/* get the Bitmap from a file according to the specified width and height*/
fun getScaledBitmap(path: String, desWidth: Int, desHeight: Int): Bitmap {
    // Read in the dimensions of the image on disc
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    // as I understand, options will receive main sizes from path file's
    BitmapFactory.decodeFile(path, options)

    // width and height of photo, that we saved in file
    val srcWidth = options.outWidth.toFloat()
    val srcHeight = options.outHeight.toFloat()

    /* Figure out how much to scale down by.
    * - des - view element
    * - src - photo */
    val sampleSize = if(srcHeight <= desHeight && srcWidth <= desWidth){
        1
    } else {
        val heightScale = srcHeight / desHeight
        val widthScale = srcWidth / desWidth
        minOf(heightScale, widthScale).roundToInt()
    }

    // Read in and create final Bitmap
    return BitmapFactory.decodeFile(path, BitmapFactory.Options().apply {
        inSampleSize = sampleSize
    })
}