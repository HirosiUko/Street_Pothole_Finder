package com.example.streetpotholefinder.accident

import android.graphics.Bitmap
import android.location.Location
import java.io.Serializable
import java.time.LocalDateTime

data class Accident(
    val portholes: Array<Porthole>,
    val cracks: Array<Crack>,
    val issueTime: LocalDateTime
) : Serializable

open class Issues(_image: Bitmap, _gpsInfo: Location, _issueTime: LocalDateTime ) {
    val image: Bitmap = _image
    val gpsInfo: Location = _gpsInfo
    val issueTime: LocalDateTime  = _issueTime
}

//val accident : Accident = null
//accident.portholes.lenght

class Porthole(_image: Bitmap, _gpsInfo: Location, _issueTime: LocalDateTime ) :
    Issues(_image, _gpsInfo, _issueTime) {
}

class Crack(_image: Bitmap, _gpsInfo: Location, _issueTime: LocalDateTime ) :
    Issues(_image, _gpsInfo, _issueTime) {

}
