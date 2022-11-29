package com.example.streetpotholefinder

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.streetpotholefinder.accidentsList.AccidentsActivity
import com.example.streetpotholefinder.dataList.DataListVO
import com.example.streetpotholefinder.issue.Event
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecResultActivity : AppCompatActivity() {

    //// GUI Component ////
    private lateinit var tvResultDate: TextView
    private lateinit var tvResultTime: TextView
    private lateinit var tvResultLength: TextView
    private lateinit var tvResultPotholeCnt: TextView
    private lateinit var tvResultCrackCnt: TextView
    ///////////////////////

    private lateinit var database: DatabaseReference
    lateinit var data: String
    private lateinit var prevActivityInfo: String

    val db = Firebase.database
    val myRef = db.getReference("currentEvent")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rec_result)

        tvResultDate = findViewById(R.id.ResultDate)
        tvResultTime = findViewById(R.id.ResultTime)
        tvResultLength = findViewById(R.id.ResultLength)
        tvResultPotholeCnt = findViewById(R.id.ResultPotholeCnt)
        tvResultCrackCnt = findViewById(R.id.ResultCrackCnt)

        prevActivityInfo = intent.getStringExtra("previousActivityInfo").toString()
        when (prevActivityInfo) {
            "CameraView" -> displayFromCarmera()
            "DataListAdapter" -> displayFromDataList()
        }

        val portholeBtn = findViewById<LinearLayout>(R.id.linearLayoutPortHole)
        portholeBtn.setOnClickListener {
            val intent = Intent(this, AccidentsActivity::class.java)
            intent.putExtra("Category", "POTHOLE")
            startActivity(intent)
        }

        val crackBtn = findViewById<LinearLayout>(R.id.linearLayoutCrack)
        crackBtn.setOnClickListener {
            val intent = Intent(this, AccidentsActivity::class.java)
            intent.putExtra("Category", "CRACK")
            startActivity(intent)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        when (prevActivityInfo) {
            "CameraView" -> gotoMain()
            "DataListAdapter" -> finish()
        }
    }

    private fun displayFromCarmera() {
        var currentEvent: Event = Event.getInstance()
        var recStartTime: LocalDateTime? = currentEvent.accident.recStartTime
        var recEndTime: LocalDateTime? = currentEvent.accident.recEndTime
        var recTime = Duration.between(recStartTime, recEndTime)
        tvResultDate.text = recEndTime?.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        tvResultTime.text = recEndTime?.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        tvResultLength.text = recTime?.seconds.toString() + " sec"
        tvResultPotholeCnt.text = currentEvent.accident.portholes.size.toString()
        tvResultCrackCnt.text = currentEvent.accident.cracks.size.toString()
        Toast.makeText(this, currentEvent.accident.portholes.toString(), Toast.LENGTH_SHORT)
            .show()
        Log.d(
            "RecResultActivity",
            "onCreate: CameraView cntofporthole:" + currentEvent.accident.portholes.size.toString()
        )
    }

    private fun displayFromDataList(){
        val a = intent.getIntExtra("number", 5)
        var contentList = mutableListOf<DataListVO>()

        contentList.add(DataListVO("2022년 11월 15일", "오후 2시 40분", "00:29:23", "30", "14"))
        contentList.add(DataListVO("2022년 10월 25일", "오전 8시 18분", "00:05:22", "5", "3"))

        contentList.add(DataListVO("2022년 10월 23일", "오후 3시 22분", "00:04:03", "13", "27"))
        contentList.add(DataListVO("2022년 9월 17일", "오전 10시 33분", "00:16:45", "4", "19"))
        tvResultDate.text = contentList[a].StreetDate
        tvResultTime.text = contentList[a].StreetTime
        tvResultLength.text = contentList[a].RecordLength
        tvResultPotholeCnt.text = contentList[a].PotholeCnt
        tvResultCrackCnt.text = contentList[a].CrackCnt
    }

    private fun gotoMain() {
        // Vibrate for 500 milliseconds
        var v: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v.vibrate(500)

        val dlg: AlertDialog.Builder = AlertDialog.Builder(
            this,
            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
        )
        dlg.setTitle("⚠️ 경고") //제목
        dlg.setMessage("메뉴판으로 돌아갑니다.") // 메시지
        dlg.setPositiveButton("확인") { _, _ ->
            val intent = Intent(this, MainActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK//액티비티 스택제거
            startActivity(intent)
            finish()
        }
        dlg.setNegativeButton("취소") { _, _ ->
            // nothing
        }
        dlg.show()
    }
}