package com.example.streetpotholefinder

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.streetpotholefinder.accident.Accident
import com.example.streetpotholefinder.issue.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecResultActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    lateinit var data: String

    val db = Firebase.database
    val myRef = db.getReference("currentEvent")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rec_result)

        val ResultDate = findViewById<TextView>(R.id.ResultDate)
        val ResultTime = findViewById<TextView>(R.id.ResultTime)
        val ResultLength = findViewById<TextView>(R.id.ResultLength)
        val ResultPotholeCnt = findViewById<TextView>(R.id.ResultPotholeCnt)
        val ResultCrackCnt = findViewById<TextView>(R.id.ResultCrackCnt)

        val intent = intent
        val prevAcivityInfo = intent.getStringExtra("previousActivityInfo")
        when (prevAcivityInfo) {
            "CameraView" -> {
                var currentEvent: Event = Event.getInstance()
                var recStartTime: LocalDateTime? = currentEvent.accident?.recStartTime
                var recEndTime: LocalDateTime? = currentEvent.accident?.recEndTime
                var recTime = Duration.between(recStartTime, recEndTime)
                ResultDate.setText(recEndTime?.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                ResultTime.setText(recEndTime?.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                ResultLength.setText(recTime?.seconds.toString() + " sec")
                ResultPotholeCnt.setText(currentEvent.accident!!.portholes.size.toString())
                ResultCrackCnt.setText(currentEvent.accident!!.cracks.size.toString())
                Toast.makeText(this, currentEvent.accident.portholes.toString(), Toast.LENGTH_SHORT).show()
                Log.d("RecResultActivity", "onCreate: CameraView cntofporthole:"+currentEvent.accident!!.portholes.size.toString())

                // Firebase Code.!!
                // currentEvent.accident.portholes에 아무것도 없음...
                // var myRef: DatabaseReference = mDatabase

//                val fbAuth = FirebaseAuth.getInstance()
//                val fbFirestore = FirebaseFirestore.getInstance()
//
//                var userInfo = currentEvent.accident
//
//                fbFirestore?.collection(
//                    fbAuth?.uid.toString()
//                )?.document(
//                    recEndTime!!.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"))
//                )?.set(currentEvent.accident)
//
//                database = Firebase.database.reference


//                myRef.setValue(currentEvent.accident.cracks)

//                mDatabase.setValue(currentEvent.accident)
//                    .addOnSuccessListener {
//                        // 저장 성공 시
//                        Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
//                    }
//                    .addOnFailureListener { e ->
//                        // 저장 실패 시
//                        Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()
//                    }
//

            }
            "DataListAdapter" -> {
                val a = intent.getIntExtra("number", 5)
                var ContentList = mutableListOf<DataListVO>()

                ContentList.add(DataListVO("2022년 11월 15일", "오후 2시 40분", "00:29:23", "30", "14"))
                ContentList.add(DataListVO("2022년 10월 25일", "오전 8시 18분", "00:05:22", "5", "3"))

                ContentList.add(DataListVO("2022년 10월 23일", "오후 3시 22분", "00:04:03", "13", "27"))
                ContentList.add(DataListVO("2022년 9월 17일", "오전 10시 33분", "00:16:45", "4", "19"))
                ResultDate.setText(ContentList[a].StreetDate)
                ResultTime.setText(ContentList[a].StreetTime)
                ResultLength.setText(ContentList[a].RecordLength)
                ResultPotholeCnt.setText(ContentList[a].PotholeCnt)
                ResultCrackCnt.setText(ContentList[a].CrackCnt)
            }
        }

        val portholeBtn = findViewById<LinearLayout>(R.id.linearLayoutPortHole)
        portholeBtn.setOnClickListener {
            val intent = Intent(this, accidentActivity::class.java)
            intent.putExtra("Category", "POTHOLE")
            startActivity(intent)
        }

        val crackBtn = findViewById<LinearLayout>(R.id.linearLayoutCrack)
        crackBtn.setOnClickListener {
            val intent = Intent(this, accidentActivity::class.java)
            intent.putExtra("Category", "CRACK")
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        // Vibrate for 500 milliseconds
        var v: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v.vibrate(500)

        val dlg: AlertDialog.Builder = AlertDialog.Builder(
            this,
            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
        )
        dlg.setTitle("⚠️ 경고") //제목
        dlg.setMessage("메뉴판으로 돌아갑니다.") // 메시지
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { _, _ ->

        })
        dlg.setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which ->
            // Do nothing
        })
        dlg.show()
    }
}