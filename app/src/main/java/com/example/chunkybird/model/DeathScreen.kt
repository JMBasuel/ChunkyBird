package com.example.chunkybird.model

class DeathScreen {
    companion object {
        val x = ScreenSize.SCREEN_WIDTH / 10
        val y = ScreenSize.SCREEN_HEIGHT / 3
        private val w = ScreenSize.SCREEN_WIDTH - (x * 2)
        private val h = ScreenSize.SCREEN_HEIGHT - (y * 2)
        val x2 = x + w
        val y2 = y + h

        // SCORES coordinates
        val sx = x + w / 2
        val hsy = y + y / 5
        val sy = hsy + hsy * 2 / 8

        private val w2 = w / 2 - w / 8
        val h2 = h / 4 - 25

        // REPLAY button coordinates
        val rx = x + w2 / 6
        val ry = y + h / 2 + h / 4

        val rx2 = rx + w2 + w2 / 7
        val ry2 = ry + h2

        // MENU button coordinates
        val mx = rx2 + 25
        val my = ry

        val mx2 = mx + w2 + w2 / 7
        val my2 = my + h2
    }
}