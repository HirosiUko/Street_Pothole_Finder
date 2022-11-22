package com.example.streetpotholefinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.DataListVO

class RecResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rec_result)


        val ResultDate = findViewById<TextView>(R.id.ResultDate)
        val ResultTime = findViewById<TextView>(R.id.ResultTime)
        val ResultLength = findViewById<TextView>(R.id.ResultLength)
        val ResultPotholeCnt = findViewById<TextView>(R.id.ResultPotholeCnt)
        val ResultCrackCnt = findViewById<TextView>(R.id.ResultCrackCnt)

        val intent = intent
        val a = intent.getIntExtra("number",5)
        var ContentList = mutableListOf<DataListVO>()

        ContentList.add(DataListVO("2022년 11월 15일","오후 2시 40분", "00:29:23","30","14"))
        ContentList.add(DataListVO("2022년 10월 25일","오전 8시 18분", "00:05:22","5","3"))

        ContentList.add(DataListVO("2022년 10월 23일","오후 3시 22분", "00:04:03","13","27"))
        ContentList.add(DataListVO("2022년 9월 17일","오전 10시 33분", "00:16:45","4","19"))
        ResultDate.setText(ContentList[a].StreetDate)
        ResultTime.setText(ContentList[a].StreetTime)
        ResultLength.setText(ContentList[a].RecordLength)
        ResultPotholeCnt.setText(ContentList[a].PotholeCnt)
        ResultCrackCnt.setText(ContentList[a].CrackCnt)


    }

}