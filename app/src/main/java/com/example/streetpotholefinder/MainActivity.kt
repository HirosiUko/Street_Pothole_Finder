package com.example.streetpotholefinder

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_startrecode = findViewById<ImageView>(R.id.btn_startrecode)
        btn_startrecode.setOnClickListener {
            val intent = Intent(this, CameraXAPP::class.java)
            startActivity(intent)
        }
    }
}