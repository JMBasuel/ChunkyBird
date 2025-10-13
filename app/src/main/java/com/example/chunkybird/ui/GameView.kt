package com.example.chunkybird.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.chunkybird.MainActivity
import com.example.chunkybird.model.DeathScreen
import com.example.chunkybird.model.ScreenSize
import com.example.chunkybird.thread.GameThread
import java.io.File

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private val tag = "GameView"
    private var gameThread: GameThread? = null
    private val file = File(context.filesDir, "HighScore.txt")

    init {
        val holder = holder
        holder.addCallback(this)
        isFocusable = true
        gameThread = GameThread(file.absolutePath, context, holder, resources)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        if (!gameThread!!.isRunning) {
            gameThread!!.isRunning = false
            gameThread = GameThread(file.absolutePath, context, p0, resources)
        } else {
            gameThread!!.start()
        }
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {}

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        if (gameThread!!.isRunning) {
            gameThread!!.isRunning = false
            val isCheck = true
            while (isCheck) {
                try {
                    gameThread!!.join()
                } catch (e: InterruptedException) {
                    Log.d(tag, "Interrupted")
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event!!.x
        val y = event.y
        val ev = event.action
        if (ev == MotionEvent.ACTION_DOWN) {
            if (!gameThread!!.isRunning) {                                                              // GAME OVER
                if (x in DeathScreen.rx.toFloat()..DeathScreen.rx2.toFloat() &&                   // REPLAY
                    y in DeathScreen.ry.toFloat()..DeathScreen.ry2.toFloat()) {
                    Log.d(tag, "Replay")
                    val intent = Intent(context, GameActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(intent)
                } else if (x in DeathScreen.mx.toFloat()..DeathScreen.mx2.toFloat() &&            // MENU
                    y in DeathScreen.my.toFloat()..DeathScreen.my2.toFloat()) {
                    Log.d(tag, "Menu")
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    context.startActivity(intent)
                    (context as Activity).finish()
                }
            } else {
                if (x in (ScreenSize.SCREEN_WIDTH / 36).toFloat()..                                  // PAUSE GAME
                    (ScreenSize.SCREEN_WIDTH / 9).toFloat() &&
                    y in (ScreenSize.SCREEN_HEIGHT / 54).toFloat()..
                    (ScreenSize.SCREEN_HEIGHT / 16).toFloat()) {
                    gameThread!!.pause()
                } else {
                    gameThread!!.jump()                                                                 // JUMP BIRD
                }
            }
        }
        return true
    }
}