package com.example.streetpotholefinder.accident

import android.graphics.Bitmap
import android.location.Location
import java.io.Serializable
import java.time.LocalDateTime

class Accident
{
    val portholes =  ArrayList<Porthole>()
    val cracks = ArrayList<Crack>()
    var recStartTime = LocalDateTime.now()
    var recEndTime =  LocalDateTime.now()
}

open class Issues(_image: Bitmap, _gpsInfo: Location, _issueTime: LocalDateTime ) : Serializable{
    val image: Bitmap = _image
    val gpsInfo: Location = _gpsInfo
    val issueTime: LocalDateTime  = _issueTime
}

//val accident : Accident = null
//accident.portholes.lenght

class Porthole(_image: Bitmap, _gpsInfo: Location, _issueTime: LocalDateTime ) : Serializable
{
    val image: Bitmap = _image
    val gpsInfo: Location = _gpsInfo
    val issueTime: LocalDateTime  = _issueTime
}

class Crack(_image: Bitmap, _gpsInfo: Location, _issueTime: LocalDateTime ) : Serializable
{
    val image: Bitmap = _image
    val gpsInfo: Location = _gpsInfo
    val issueTime: LocalDateTime  = _issueTime
}
