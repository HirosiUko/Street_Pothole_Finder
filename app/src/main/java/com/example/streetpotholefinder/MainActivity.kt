package com.example.streetpotholefinder

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // 회원 정보 Info
        auth = FirebaseAuth.getInstance()
        val email = intent.getStringExtra("email")
        val name = intent.getStringExtra("name")
        val photoUrl = intent.getStringExtra("photoUrl")
        findViewById<TextView>(R.id.tvLoginInfo).text = email + "\n" + name + "\n" + photoUrl

        if (photoUrl != null) {
            Log.d(TAG, photoUrl)
        }

        // 촬영 버튼
        val btnStartRecord = findViewById<LinearLayout>(R.id.btnStartRecord)
        btnStartRecord.setOnClickListener {
            val intent = Intent(this, CameraView::class.java)
            startActivity(intent)
        }

        // Logout 버튼
        val btnLogout = findViewById<ImageView>(R.id.ivLogout)
        btnLogout.setOnClickListener{
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        //상단 '환영합니다'에 띄울 유저 이름 가져오기
        val txtGoogleName = findViewById<TextView>(R.id.txt_main_googlename)
        txtGoogleName.text=name
    }

//    override fun onBackPressed() {
//        if (Build.VERSION.SDK_INT >= 21) {
//            finishAndRemoveTask();
//        } else {
//            finish();
//        }
//        // Activity 종료
//        ActivityCompat.finishAffinity(this)
//        // App종료
//        System.exit(0);
//    }
}