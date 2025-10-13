package com.example.chunkybird.model

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chunkybird.R

class Bird(res : Resources) {
    var x : Int = 0
    var y : Int = 0
    val maxFrame : Int = 15
    var currentFrame : Int = 0
    private var birdList1 : ArrayList<Bitmap> = arrayListOf()
    private var birdList2 : ArrayList<Bitmap> = arrayListOf()
    private var birdList3 : ArrayList<Bitmap> = arrayListOf()
    private var birdList4 : ArrayList<Bitmap> = arrayListOf()

    init {

        birdList1.add(BitmapFactory.decodeResource(res, R.drawable.frame_0))
        birdList1.add(BitmapFactory.decodeResource(res, R.drawable.frame_0))
        birdList1.add(BitmapFactory.decodeResource(res, R.drawable.frame_0))
        birdList1.add(BitmapFactory.decodeResource(res, R.drawable.frame_0))
        birdList1.add(BitmapFactory.decodeResource(res, R.drawable.frame_1))
        birdList1.add(BitmapFactory.decodeResource(res, R.drawable.frame_1))
        birdList1.add(BitmapFactory.decodeResource(res, R.drawable.frame_1))
        birdList1.add(BitmapFactory.decodeResource(res, R.drawable.frame_1))
        birdList1.add(BitmapFactory.decodeResource(res, R.drawable.frame_2))
        birdList1.add(BitmapFactory.decodeResource(res, R.drawable.frame_2))
        birdList1.add(BitmapFactory.decodeResource(res, R.drawable.frame_2))
        birdList1.add(BitmapFactory.decodeResource(res, R.drawable.frame_2))
        birdList1.add(BitmapFactory.decodeResource(res, R.drawable.frame_3))
        birdList1.add(BitmapFactory.decodeResource(res, R.drawable.frame_3))
        birdList1.add(BitmapFactory.decodeResource(res, R.drawable.frame_3))
        birdList1.add(BitmapFactory.decodeResource(res, R.drawable.frame_3))

        birdList2.add(BitmapFactory.decodeResource(res, R.drawable.frame_0))
        birdList2.add(BitmapFactory.decodeResource(res, R.drawable.frame_0))
        birdList2.add(BitmapFactory.decodeResource(res, R.drawable.frame_0))
        birdList2.add(BitmapFactory.decodeResource(res, R.drawable.frame_0))
        birdList2.add(BitmapFactory.decodeResource(res, R.drawable.frame_1))
        birdList2.add(BitmapFactory.decodeResource(res, R.drawable.frame_1))
        birdList2.add(BitmapFactory.decodeResource(res, R.drawable.frame_1))
        birdList2.add(BitmapFactory.decodeResource(res, R.drawable.frame_1))
        birdList2.add(BitmapFactory.decodeResource(res, R.drawable.frame_2))
        birdList2.add(BitmapFactory.decodeResource(res, R.drawable.frame_2))
        birdList2.add(BitmapFactory.decodeResource(res, R.drawable.frame_2))
        birdList2.add(BitmapFactory.decodeResource(res, R.drawable.frame_2))
        birdList2.add(BitmapFactory.decodeResource(res, R.drawable.frame_3))
        birdList2.add(BitmapFactory.decodeResource(res, R.drawable.frame_3))
        birdList2.add(BitmapFactory.decodeResource(res, R.drawable.frame_3))
        birdList2.add(BitmapFactory.decodeResource(res, R.drawable.frame_3))

        birdList3.add(BitmapFactory.decodeResource(res, R.drawable.frame_0))
        birdList3.add(BitmapFactory.decodeResource(res, R.drawable.frame_0))
        birdList3.add(BitmapFactory.decodeResource(res, R.drawable.frame_0))
        birdList3.add(BitmapFactory.decodeResource(res, R.drawable.frame_0))
        birdList3.add(BitmapFactory.decodeResource(res, R.drawable.frame_1))
        birdList3.add(BitmapFactory.decodeResource(res, R.drawable.frame_1))
        birdList3.add(BitmapFactory.decodeResource(res, R.drawable.frame_1))
        birdList3.add(BitmapFactory.decodeResource(res, R.drawable.frame_1))
        birdList3.add(BitmapFactory.decodeResource(res, R.drawable.frame_2))
        birdList3.add(BitmapFactory.decodeResource(res, R.drawable.frame_2))
        birdList3.add(BitmapFactory.decodeResource(res, R.drawable.frame_2))
        birdList3.add(BitmapFactory.decodeResource(res, R.drawable.frame_2))
        birdList3.add(BitmapFactory.decodeResource(res, R.drawable.frame_3))
        birdList3.add(BitmapFactory.decodeResource(res, R.drawable.frame_3))
        birdList3.add(BitmapFactory.decodeResource(res, R.drawable.frame_3))
        birdList3.add(BitmapFactory.decodeResource(res, R.drawable.frame_3))

        birdList4.add(BitmapFactory.decodeResource(res, R.drawable.frame_0))
        birdList4.add(BitmapFactory.decodeResource(res, R.drawable.frame_0))
        birdList4.add(BitmapFactory.decodeResource(res, R.drawable.frame_0))
        birdList4.add(BitmapFactory.decodeResource(res, R.drawable.frame_0))
        birdList4.add(BitmapFactory.decodeResource(res, R.drawable.frame_1))
        birdList4.add(BitmapFactory.decodeResource(res, R.drawable.frame_1))
        birdList4.add(BitmapFactory.decodeResource(res, R.drawable.frame_1))
        birdList4.add(BitmapFactory.decodeResource(res, R.drawable.frame_1))
        birdList4.add(BitmapFactory.decodeResource(res, R.drawable.frame_2))
        birdList4.add(BitmapFactory.decodeResource(res, R.drawable.frame_2))
        birdList4.add(BitmapFactory.decodeResource(res, R.drawable.frame_2))
        birdList4.add(BitmapFactory.decodeResource(res, R.drawable.frame_2))
        birdList4.add(BitmapFactory.decodeResource(res, R.drawable.frame_3))
        birdList4.add(BitmapFactory.decodeResource(res, R.drawable.frame_3))
        birdList4.add(BitmapFactory.decodeResource(res, R.drawable.frame_3))
        birdList4.add(BitmapFactory.decodeResource(res, R.drawable.frame_3))

        x = ScreenSize.SCREEN_WIDTH/3 - birdList1[0].width/2
        y = ScreenSize.SCREEN_HEIGHT/2 - birdList1[0].width/2
    }

    fun getBird1(current : Int) : Bitmap {
        return birdList1[current]
    }

    fun getBird2(current: Int): Bitmap {
        return birdList2[current]
    }

    fun getBird3(current: Int): Bitmap {
        return birdList3[current]
    }

    fun getBird4(current: Int): Bitmap {
        return birdList4[current]
    }
}