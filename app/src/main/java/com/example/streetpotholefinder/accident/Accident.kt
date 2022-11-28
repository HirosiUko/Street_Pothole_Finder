package com.example.streetpotholefinder.accident

import android.media.Image
import android.telephony.CarrierConfigManager.Gps
import java.io.Serializable
import java.sql.Time

data class Accident (
    val portholes : Array<Porthole>,
    val cracks : Array<Crack>
) : Serializable

interface Issues {
    val image : Image
        get() {
            TODO()
        }
    val gpsInfo : Gps
        get() {
            TODO()
        }
    val issueTime : Time
        get() {
            TODO()
        }
}

//val accident : Accident = null
//accident.portholes.lenght

class Porthole : Issues {

}

class Crack : Issues {

}
