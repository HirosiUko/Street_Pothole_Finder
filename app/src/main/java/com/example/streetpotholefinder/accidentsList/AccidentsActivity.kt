package com.example.streetpotholefinder.accidentsList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetpotholefinder.R
import com.example.streetpotholefinder.issue.Event
import java.time.format.DateTimeFormatter

class AccidentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accident)

        val rvv = findViewById<RecyclerView>(R.id.accident_list_view)
        var contentList = mutableListOf<accidentVO>()

        val curEvent : Event = Event.getInstance()
        val dataArray = when(intent.getStringExtra("Category")){
            "POTHOLE" ->{
                curEvent.accident.portholes
            }
            "CRACK" -> {
                curEvent.accident.cracks
            }
            else -> {null}
        }

        if (dataArray != null) {
            for( data in dataArray)
            {
                contentList.add(
                    accidentVO(
                        data.image,
                        "${data.gpsInfo.latitude},${data.gpsInfo.longitude}",
                        data.issueTime.toString()
                    )
                )
            }
        }


        rvv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvv.setHasFixedSize(true)

        rvv.adapter = AccidentsAdapter(contentList)

        rvv.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

    }
}