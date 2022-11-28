package com.example.streetpotholefinder

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.streetpotholefinder.accident.Accident
import com.example.streetpotholefinder.issue.Event
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecResultActivity : AppCompatActivity() {

    // var mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
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
                currentEvent.accident?.portholes?.let { it -> ResultPotholeCnt.setText(it.size.toString()) }
                currentEvent.accident?.cracks?.let { ResultCrackCnt.setText(it.size.toString()) }
                Toast.makeText(this, currentEvent.accident.portholes.toString(), Toast.LENGTH_SHORT).show()
                // currentEvent.accident.portholes에 아무것도 없음...
                // var myRef: DatabaseReference = mDatabase
                myRef.setValue(currentEvent.accident.recEndTime)


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