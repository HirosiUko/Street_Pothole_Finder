package com.example.streetpotholefinder.accidentsList

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetpotholefinder.R
import com.example.streetpotholefinder.issue.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AccidentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accident)

        val rvv = findViewById<RecyclerView>(R.id.accident_list_view)
        var contentList = mutableListOf<accidentVO>()

        var fbRef = intent.getStringExtra("dataRef") ?: ""
        var categ = intent.getStringExtra("Category") ?: ""

        var prevActivityInfo = intent.getStringExtra("previousActivityInfo").toString()
        when (prevActivityInfo) {
            "CameraView" -> displayFromCarmera(contentList)
            "DataListAdapter" -> displayFromDataList(contentList, fbRef, categ)
        }

        rvv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvv.setHasFixedSize(true)

        rvv.adapter = AccidentsAdapter(contentList)

        rvv.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

    }

    fun displayFromCarmera(contentList: MutableList<accidentVO>)
    {
        val curEvent : Event = Event.getInstance()
        val dataArray = when(intent.getStringExtra("Category")){
            "POTHOLE" ->{
                curEvent.accident.potholes
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
    }

    fun displayFromDataList(contentList: MutableList<accidentVO>, fbRef: String, categ: String)
    {
        Log.d("AccidentsActivity", "onCreate: ${categ} ${fbRef}")
        var auth = FirebaseAuth.getInstance()
        var fireStore = Firebase.firestore.collection(auth.currentUser?.displayName ?: "devmode").document(fbRef)
        fireStore.collection(categ).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    for( event in document.documents) {
                        Log.d("AccidentsActivity", "onCreate: " + event.data.toString())
//                        contentList.add(
//                            accidentVO(
//                                Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888),
//                                "${event.data!!["gpsInfo"] as String}",
//                                event.data!!["issueTime"] as String
//                            )
//                        )
                    }
                }else
                {
                    Log.d("AccidentsActivity", "document: null")
                }
            }
    }
}