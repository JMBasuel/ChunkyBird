package com.example.chunkybird.model

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chunkybird.R

class BirdDie(res : Resources) {
    var y : Int = 0
    var maxFrame = 1
    var currentFrame = 0
    private var dieArr : ArrayList<Bitmap> = arrayListOf()

    init {
        dieArr.add(BitmapFactory.decodeResource(res, R.drawable.frame_4))
    }
    fun getBirdDie(i : Int) : Bitmap {
        return dieArr[i]
    }
}