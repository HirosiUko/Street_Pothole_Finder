package com.example.streetpotholefinder

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStream
import kotlin.system.exitProcess

class TestImageUpload : AppCompatActivity() {
    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_image_upload)

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            var permissions = arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.INTERNET
            )
            ActivityCompat.requestPermissions(this, permissions, 100)
        }



        var storage: FirebaseStorage = FirebaseStorage.getInstance()

        var storageRef = storage.reference




        var abpath = Environment.getExternalStorageDirectory().absolutePath

        Log.d("abPath", abpath)

        var file = Uri.fromFile(File("${abpath}/Pictures/StreetPotholeFinder/wall.jpg"))

        val wallRef = storageRef.child("people/${file.lastPathSegment}")
        var uploadTask = wallRef.putFile(file)

        uploadTask.addOnFailureListener {
            Log.d("bad", "b")
        }.addOnSuccessListener { taskSnapshot ->
            Log.d("good", taskSnapshot.metadata?.contentType.toString() ?: "0")
            Log.d("good", "g")
        }

//        val bitmap = getDrawable(R.drawable.cute_pepe)?.toBitmap()
//        val baos = ByteArrayOutputStream()
//        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        val data = baos.toByteArray()
//
//        var mountainRef = storageRef.child("mountains.jpg")
//        var uploadTask = mountainRef.putBytes(data)
//        uploadTask.addOnFailureListener {
//            Log.d("bad", "b")
//        }.addOnSuccessListener { taskSnapshot ->
//            Log.d("good", taskSnapshot.metadata?.contentType.toString() ?: "0")
//            Log.d("good", "g")
//        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode === 100) {
            if (grantResults.isNotEmpty()) {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) exitProcess(0)
                }
            }
        }
    }

}