package com.example.streetpotholefinder.accidentsList

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetpotholefinder.R
import com.example.streetpotholefinder.issue.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class AccidentsActivity : AppCompatActivity() {
    private lateinit var rvv: RecyclerView
    private var contentList = mutableListOf<accidentVO>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accident)

        rvv = findViewById<RecyclerView>(R.id.accident_list_view)


        var fbRef = intent.getStringExtra("dataRef") ?: ""
        var categ = intent.getStringExtra("Category") ?: ""

        var prevActivityInfo = intent.getStringExtra("previousActivityInfo").toString()
        when (prevActivityInfo) {
            "CameraView" -> displayFromCarmera()
            "DataListAdapter" -> displayFromDataList(fbRef, categ)
        }

        rvv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvv.setHasFixedSize(true)

        rvv.adapter = AccidentsAdapter(contentList)

        rvv.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
    }

    fun displayFromCarmera() {
        val curEvent: Event = Event.getInstance()
        val dataArray = when (intent.getStringExtra("Category")) {
            "pothole" -> {
                curEvent.accident.potholes
            }
            "crack" -> {
                curEvent.accident.cracks
            }
            else -> {
                null
            }
        }

//        Log.d("AccidentsActivity", "displayFromCarmera: $dataArray")
        if (dataArray != null) {
            for ( (key, data) in dataArray.withIndex()) {
                getImageUri(data.image)?.let {
                    contentList.add(
                        accidentVO(
                            it,
                            "${data.gpsInfo.latitude},${data.gpsInfo.longitude}",
                            data.issueTime.toString()
                        )
                    )
                }
            }
        }
    }

    private fun getImageUri(inImage: Bitmap): Uri? {
        val tempFile = File(externalCacheDir, System.currentTimeMillis().toString() + ".png")
        try {
            tempFile.createNewFile()
            val out = FileOutputStream(tempFile)
            inImage.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.close()

        } catch (e: FileNotFoundException) {
            Log.e("AccidentsActivity", "FileNotFoundException : " + e.message)
        } catch (e: IOException) {
            Log.e("AccidentsActivity", "IOException : " + e.message)
        }

        return tempFile.toUri()
    }

    fun displayFromDataList(fbRef: String, categ: String) {
        Log.d("AccidentsActivity", "onCreate: ${categ} ${fbRef}")
        var auth = FirebaseAuth.getInstance()
        var fireStore = Firebase.firestore.collection(auth.currentUser?.displayName ?: "devmode")
            .document(fbRef)
        fireStore.collection(categ).get().addOnSuccessListener { document ->
            document.documents.forEach { issue ->
                issue.data?.values?.forEach { e ->
                    val event = e as Map<String, String>

                    val storageReference: StorageReference =
                        FirebaseStorage.getInstance().getReference(event["imgUri"] as String)

                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        Log.d("AccidentsActivity", "displayFromDataList: $uri")
                        insertItem(
                            uri,
                            event["gpsInfo"] as String,
                            event["issueTime"] as String
                        )

                    }
                    Log.d("AccidentsActivity", "displayFromDataList: $event ${contentList.size}")
                }
            }
        }

    }

    fun insertItem(uri: Uri, gpsInfo: String, issueTime: String) {
        contentList.add(
            accidentVO(
                uri,
                gpsInfo,
                issueTime
            )
        )
        rvv.adapter?.notifyItemInserted(contentList.size)
    }
}