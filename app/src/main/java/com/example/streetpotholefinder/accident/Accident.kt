package com.example.streetpotholefinder.accident

import android.media.Image
import android.telephony.CarrierConfigManager.Gps
import java.sql.Time

class Accident {
    val portholes : Array<Porthole>
        get() {
            TODO()
        }
    val cracks : Array<Issues>
        get() {
            TODO()
        }
}

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
