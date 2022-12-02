package com.example.streetpotholefinder

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.streetpotholefinder.dataList.DataListActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 회원 정보 Info
        auth = FirebaseAuth.getInstance()
        val name = auth.currentUser?.displayName

        // 촬영 버튼
        val btnStartRecord = findViewById<LinearLayout>(R.id.btnStartRecord)
        btnStartRecord.setOnClickListener {
            val intent = Intent(this, CameraView::class.java)
            startActivity(intent)
            // Vibrate for 500 milliseconds
            var v : Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.vibrate(500)
        }

        // Logout 버튼  - 호준
//        val btnLogout = findViewById<ImageView>(R.id.ivLogout)
//        btnLogout.setOnClickListener{
//            val dlg: AlertDialog.Builder = AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
//            dlg.setTitle("\uD83D\uDD06 경고") //제목
//            dlg.setMessage("계정을 로그아웃 합니다.") // 메시지
//            dlg.setPositiveButton("확인", DialogInterface.OnClickListener {_,_ ->
//                auth.signOut()
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//            })
//            dlg.setNegativeButton("취소", DialogInterface.OnClickListener{
//                    _,_ ->
//                // Do nothing
//            })
//            dlg.show()
//        }

        // Logout버튼 - 지영
        val btnLogout = findViewById<ImageView>(R.id.ivLogout)
        btnLogout.setOnClickListener{
            val cuDialogLyt = layoutInflater.inflate(R.layout.dialog_default_lyt, null)
            val build = AlertDialog.Builder(it.context).apply { setView(cuDialogLyt) }
            val dialog=build.create()

            dialog.apply {
                show()
                window?.setLayout(750, LayoutParams.WRAP_CONTENT)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

            cuDialogLyt.findViewById<TextView>(R.id.dialog_title).text="로그아웃 하시겠습니까?"
            cuDialogLyt.findViewById<TextView>(R.id.dialog_sub).text="계정이 로그아웃 됩니다"
            cuDialogLyt.findViewById<TextView>(R.id.dialog_btn_y).apply {
                text = "예"
                setOnClickListener{
                    auth.signOut()
                    val intent = Intent(it.context,LoginActivity::class.java)
                    startActivity(intent)
                }
            }
            cuDialogLyt.findViewById<TextView>(R.id.dialog_btn_n).apply {
                text = "아니오"
                setOnClickListener{
                    dialog.dismiss()
                }
            }

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
        var btnMainMypage = findViewById<LinearLayout>(R.id.btn_main_mypage)
        btnMainMypage.setOnClickListener{
            val intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        var v : Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v.vibrate(500)

//        val dlg: AlertDialog.Builder = AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
//        dlg.setTitle("\uD83D\uDD06 경고") //제목
//        dlg.setMessage("App을 종료합니다.") // 메시지
//        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { _,_ ->
//            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
//                // Workaround for Android Q memory leak issue in IRequestFinishCallback$Stub.
//                // (https://issuetracker.google.com/issues/139738913)
//                finishAfterTransition()
//            } else {
//                super.onBackPressed()
//            }
//        })
//        dlg.setNegativeButton("취소", DialogInterface.OnClickListener{ _,_ ->
//            // Do nothing
//        })
//        dlg.show()


        val cuDialogLyt = layoutInflater.inflate(R.layout.dialog_default_lyt, null)
        val build = AlertDialog.Builder(this).apply { setView(cuDialogLyt) }
        val dialog=build.create()

        dialog.apply {
            show()
            window?.setLayout(750, LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        cuDialogLyt.findViewById<TextView>(R.id.dialog_title).text="종료하시겠습니까?"
        cuDialogLyt.findViewById<TextView>(R.id.dialog_sub).text="App이 종료됩니다"
        cuDialogLyt.findViewById<TextView>(R.id.dialog_btn_y).apply {
            text = "예"
            setOnClickListener{
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                finishAfterTransition()
            } else {
                super.onBackPressed()
            }
            }
        }
        cuDialogLyt.findViewById<TextView>(R.id.dialog_btn_n).apply {
            text = "아니오"
            setOnClickListener{
                dialog.dismiss()
            }
        }
    }



}