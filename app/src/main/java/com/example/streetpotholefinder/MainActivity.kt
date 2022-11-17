package com.example.streetpotholefinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val btnStartRecord = findViewById<ImageView>(R.id.btnStartRecord)
        //-->지영수정. 버튼이 이미지뷰에서 리니어레이아웃으로 바뀌었음
        val btnStartRecord = findViewById<LinearLayout>(R.id.btnStartRecord)
        btnStartRecord.setOnClickListener {
            val intent = Intent(this, CameraXAPP::class.java)
            startActivity(intent)
        }
    }
}