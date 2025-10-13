package com.example.chunkybird.model

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chunkybird.R

class Bug(res : Resources) {
    val bug : Bitmap = BitmapFactory.decodeResource(res, R.drawable.bug1)

    var x : Int = 0
    var y : Int = 0

    val h = bug.height
    val w = bug.width
}