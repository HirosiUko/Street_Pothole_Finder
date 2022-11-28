package com.example.streetpotholefinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.accidentVO

class accidentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accident)

        val rvv = findViewById<RecyclerView>(R.id.accident_list_view)
        var ContentList = mutableListOf<accidentVO>()

        ContentList.add(accidentVO(R.drawable.cute_pepe,"134.541", "00:29:23"))
        ContentList.add(accidentVO(R.drawable.cute_pepe,"18.14512", "00:29:23"))
        ContentList.add(accidentVO(R.drawable.cute_pepe,"19.5556541", "00:29:23"))
        rvv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvv.setHasFixedSize(true)

        rvv.adapter = accidentAdapter(ContentList)

        rvv.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

    }
}