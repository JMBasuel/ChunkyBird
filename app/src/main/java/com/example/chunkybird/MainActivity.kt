package com.example.chunkybird

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import com.example.chunkybird.model.ScreenSize
import com.example.chunkybird.ui.GameActivity

@SuppressLint("CommitPrefEdits")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ScreenSize.getScreenSize(this)

        val btnPlay = findViewById<ImageButton>(R.id.btnPlay)

        btnPlay.setOnClickListener {
            Log.d("MainActivity", "Button pressed")
            val isPlayGame = Intent(this@MainActivity, GameActivity::class.java)
            startActivity(isPlayGame)
            finish()
        }
    }
}