package com.example.streetpotholefinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetpotholefinder.issue.Event
import java.time.format.DateTimeFormatter

class accidentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accident)

        val rvv = findViewById<RecyclerView>(R.id.accident_list_view)
        var ContentList = mutableListOf<accidentVO>()

        val curEvent : Event = Event.getInstance()
        val _category = intent.getStringExtra("Category")
        val dataArray = when(_category){
            "POTHOLE" ->{
                curEvent.accident.portholes
            }
            "CRACK" -> {
                curEvent.accident.cracks
            }
            else -> {null}
        }

        if (dataArray != null) {
//            for( data in dataArray)
//            {
//                ContentList.add(
//                    accidentVO(
//                        data.image,
//                        "위도:${data.gpsInfo.latitude}, 경도:${data.gpsInfo.longitude}",
//                        data.issueTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
//                    )
//                )
//            }
        }


        rvv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvv.setHasFixedSize(true)

        rvv.adapter = accidentAdapter(ContentList)

        rvv.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

    }
}