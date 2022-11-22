package com.example.streetpotholefinder

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.streetpotholefinder.user.User
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var userInfo : User
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 회원 정보 Info
        auth = FirebaseAuth.getInstance()
//        val email = auth.currentUser?.email
        val name = auth.currentUser?.displayName
//        val photoUrl = auth.currentUser?.photoUrl
////        findViewById<TextView>(R.id.tvLoginInfo).text = email + "\n" + name + "\n" + photoUrl.toString()

        // 촬영 버튼
        val btnStartRecord = findViewById<LinearLayout>(R.id.btnStartRecord)
        btnStartRecord.setOnClickListener {
            val intent = Intent(this, CameraView::class.java)
            startActivity(intent)
        }

        // Logout 버튼
        val btnLogout = findViewById<ImageView>(R.id.ivLogout)
        btnLogout.setOnClickListener{
            val dlg: AlertDialog.Builder = AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
            dlg.setTitle("\uD83D\uDD06 경고") //제목
            dlg.setMessage("계정을 로그아웃 합니다.") // 메시지
            dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            })
            dlg.setNegativeButton("취소", DialogInterface.OnClickListener{
                    dialog, which ->
                // Do nothing
            })
            dlg.show()
        }


        //상단 '환영합니다'에 띄울 유저 이름 가져오기
        val txtGoogleName = findViewById<TextView>(R.id.txt_main_googlename)
        txtGoogleName.text=name

        // 등록 데이터 확인 버튼
        val btnMainData = findViewById<LinearLayout>(R.id.btn_main_data)
        btnMainData.setOnClickListener{
            val intent = Intent(this, DataListActivity::class.java)
            startActivity(intent)
        }

//        // 내 정보 확인 버튼
//        val btnMainMypage = findViewById<LinearLayout>(R.id.btn_main_mypage)
//        btnMainMypage.setOnClickListener{
//            val intent = Intent(this, MypageActivity::class.java)
//            startActivity(intent)
//        }
        var btnMainMypage = findViewById<LinearLayout>(R.id.btn_main_mypage)
        btnMainMypage.setOnClickListener{
            val intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle("\uD83D\uDD06 경고") //제목
        dlg.setMessage("App을 종료합니다.") // 메시지
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                // Workaround for Android Q memory leak issue in IRequestFinishCallback$Stub.
                // (https://issuetracker.google.com/issues/139738913)
                finishAfterTransition()
            } else {
                super.onBackPressed()
            }
        })
        dlg.setNegativeButton("취소", DialogInterface.OnClickListener{
                dialog, which ->
            // Do nothing
        })
        dlg.show()
    }
}