package com.example.streetpotholefinder.accident

import android.graphics.Bitmap
import android.location.Location
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.ByteArrayOutputStream

data class Accident(
    var potholes: ArrayList<Issues>,
    var cracks: ArrayList<Issues>,
    var recStartTime: LocalDateTime,
    var recEndTime: LocalDateTime
)

open class Issues() {
    lateinit var image: Bitmap
    lateinit var gpsInfo: Location
    lateinit var issueTime: LocalDateTime

    constructor(_image: Bitmap, _gpsInfo: Location, _issueTime: LocalDateTime) : this() {
        this.image = _image
        gpsInfo = _gpsInfo
        issueTime = _issueTime
    }
}

@kotlinx.serialization.Serializable
class SerializedAccident(){
    var potholes: ArrayList<SerializedIssues> = ArrayList<SerializedIssues>()
    var cracks: ArrayList<SerializedIssues> = ArrayList<SerializedIssues>()
    var recStartTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    var recEndTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    private fun convBitmapToByteArry(bitmap: Bitmap) : ByteArray{
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    private fun convLocationtoString(gpsInfo: Location): String {
        return "${gpsInfo.latitude},${gpsInfo.longitude}"
    }

    fun importAccident(accident: Accident) {
        for(pothole in accident.potholes)
        {
            var img = convBitmapToByteArry(pothole.image)
            var gps = convLocationtoString(pothole.gpsInfo)
            this.potholes.add(SerializedIssues(img,gps,pothole.issueTime))
        }

        for(crack in accident.cracks)
        {
            var img = convBitmapToByteArry(crack.image)
            var gps = convLocationtoString(crack.gpsInfo)
            this.cracks.add(SerializedIssues(img,gps,crack.issueTime))
        }

        this.recStartTime = accident.recStartTime
        this.recEndTime = accident.recEndTime
    }
}

@kotlinx.serialization.Serializable
open class SerializedIssues() {
    lateinit var image: ByteArray
    lateinit var gpsInfo: String
    lateinit var issueTime: LocalDateTime
    var image_uri: String = ""

    constructor(_image: ByteArray, _gpsInfo: String, _issueTime: LocalDateTime) : this() {
        image = _image
        gpsInfo = _gpsInfo
        issueTime = _issueTime
    }
}

data class FirebaseAccident(
    val issueTime : String? = null,
    val gpsInfo: String? = null,
    var ImgUri: String? = null
)