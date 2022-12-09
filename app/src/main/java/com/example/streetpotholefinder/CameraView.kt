package com.example.streetpotholefinder

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorListenerAdapter
import android.app.ActionBar
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.animation.Animation.AnimationListener
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.airbnb.lottie.LottieAnimationView
import com.example.streetpotholefinder.databinding.ActivityCameraViewBinding
import com.example.streetpotholefinder.issue.Event
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDateTime
import java.util.EventListener

class CameraView : AppCompatActivity() {

    private val TAG = "CameraView"
    private lateinit var activityCameraView: ActivityCameraViewBinding
    private lateinit var aniView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCameraView = ActivityCameraViewBinding.inflate(layoutInflater)
        setContentView(activityCameraView.root)

        val btnRecEnd: ImageView = findViewById(R.id.btnRecStart)
        btnRecEnd.setOnClickListener {
            val _event: Event = Event.getInstance()
            _event.accident.recEndTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            Log.d(
                TAG,
                "onCreate: ${_event.accident.recStartTime}, ${_event.accident.potholes.size}"
            )
            val intent = Intent(this, RecResultActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("previousActivityInfo", "CameraView")
            startActivity(intent)
            finish()
        }

        //카운트 다운 애니메이션 없앰
//        aniView = findViewById<LottieAnimationView>(R.id.animationView)
//        aniView.addAnimatorListener(object : AnimatorListener {
//            override fun onAnimationStart(p0: Animator) {}
//            override fun onAnimationEnd(p0: Animator) {
//                aniView.visibility = View.GONE
//            }

//            override fun onAnimationCancel(p0: Animator) {}
//            override fun onAnimationRepeat(p0: Animator) {}
//        })
    }


    override fun onBackPressed() {
        // Vibrate for 500 milliseconds
        var v: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v.vibrate(500)

//        val dlg: AlertDialog.Builder = AlertDialog.Builder(
//            this,
//            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
//        )
//        dlg.setTitle("⚠️ 경고") //제목
//        dlg.setMessage("촬영이 취소 됩니다. 진행하시겠습니까?") // 메시지
//        dlg.setPositiveButton("확인") { _, _ ->
//            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
//                // Workaround for Android Q memory leak issue in IRequestFinishCallback$Stub.
//                // (https://issuetracker.google.com/issues/139738913)
//                finishAfterTransition()
//            } else {
//                super.onBackPressed()
//            }
//        }
//        dlg.setNegativeButton("취소") { _, _ ->
//            // Do nothing
//        }
//        dlg.show()


        val cuDialogLyt = layoutInflater.inflate(R.layout.dialog_default_lyt, null)
        val build = AlertDialog.Builder(this).apply { setView(cuDialogLyt) }
        val dialog=build.create()

        dialog.apply {
            show()
            window?.setLayout(750, ActionBar.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        cuDialogLyt.findViewById<TextView>(R.id.dialog_title).text="촬영이 취소됩니다. 진행하시겠습니까?"
        cuDialogLyt.findViewById<TextView>(R.id.dialog_title).setPadding(40,0,20,0)
        cuDialogLyt.findViewById<TextView>(R.id.dialog_sub).text="촬영이 취소됩니다. 데이터가 저장되지 않습니다"

        cuDialogLyt.findViewById<TextView>(R.id.dialog_btn_y).apply {
            text = "예"
            setOnClickListener{
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                    finishAfterTransition()
                } else {
                    super.onBackPressed()
                }
            }
        }
        cuDialogLyt.findViewById<TextView>(R.id.dialog_btn_n).apply {
            text = "아니오"
            setOnClickListener{
                dialog.dismiss()
            }
        }
    }
}