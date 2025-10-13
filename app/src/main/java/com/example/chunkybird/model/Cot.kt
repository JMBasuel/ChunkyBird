package com.example.chunkybird.model

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chunkybird.R

class Cot(res : Resources) {
    val cotTop: Bitmap = BitmapFactory.decodeResource(res, R.drawable.cot_top)
    val cotBottom: Bitmap = BitmapFactory.decodeResource(res, R.drawable.cot_bottom)

    val w = cotTop.width
    val h = cotTop.height

    var x : Int = 0

    var ccY : Int = 0

    fun getTopY() : Int {
        return ccY - h
    }

    fun getBottomY() : Int {
        return ccY + ScreenSize.SCREEN_HEIGHT / 3
    }
}