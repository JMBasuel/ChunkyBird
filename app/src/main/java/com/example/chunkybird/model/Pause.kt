package com.example.chunkybird.model

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chunkybird.R

class Pause(res: Resources) {
    val image : Bitmap = BitmapFactory.decodeResource(res, R.drawable.pause)

    val h = image.height
}