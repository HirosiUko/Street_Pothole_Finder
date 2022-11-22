package com.example.streetpotholefinder

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.example.streetpotholefinder.databinding.ActivityCameraViewBinding

class CameraView : AppCompatActivity() {

    private val TAG = "CameraView"
    private lateinit var activityCameraView: ActivityCameraViewBinding

    //kakao map view.
//    private lateinit var mapView: MapView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCameraView = ActivityCameraViewBinding.inflate(layoutInflater)
        setContentView(activityCameraView.root)

        val btnRecStart : ImageView = findViewById(R.id.btnRecStart)
        btnRecStart.setOnClickListener{
            val intent = Intent(this, CameraXAPP_result::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            finish()
        }
    }

    override fun onBackPressed() {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle("⚠️ 경고") //제목
        dlg.setMessage("촬영이 취소 됩니다. 진행하시겠습니까?") // 메시지
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                // Workaround for Android Q memory leak issue in IRequestFinishCallback$Stub.
                // (https://issuetracker.google.com/issues/139738913)
                finishAfterTransition()
            } else {
                super.onBackPressed()
            }
        })
        dlg.setNegativeButton("취소", DialogInterface.OnClickListener{
            dialog, which ->
            // Do nothing
        })
        dlg.show()
    }

//    @SuppressLint("MissingPermission")
//    private fun startTracking() {
//        mapView.currentLocationTrackingMode =
//            MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading  //이 부분
////        mapView.setShowCurrentLocationMarker(true)
//
////        val lm: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
////        val userNowLocation: Location? = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
////        //위도 , 경도
////        val uLatitude = userNowLocation?.latitude
////        val uLongitude = userNowLocation?.longitude
////        val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude!!, uLongitude!!)
////
////        // 현 위치에 마커 찍기
////        val marker = MapPOIItem()
//////        marker.itemName = "현 위치"
////        marker.mapPoint =uNowPosition
////        marker.markerType = MapPOIItem.MarkerType.BluePin
////        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
////        mapView.addPOIItem(marker)
//    }
//
//    // 위치추적 중지
//    private fun stopTracking() {
//        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
//    }
}