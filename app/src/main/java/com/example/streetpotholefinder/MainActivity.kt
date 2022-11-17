package com.example.streetpotholefinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStartRecord = findViewById<ImageView>(R.id.btnStartRecord)
        btnStartRecord.setOnClickListener {
            val intent = Intent(this, CameraXAPP::class.java)
            startActivity(intent)
        }
    }
}