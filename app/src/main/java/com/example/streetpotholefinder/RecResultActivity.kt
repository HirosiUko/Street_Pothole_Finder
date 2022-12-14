package com.example.streetpotholefinder

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieAnimationView
import com.dinuscxj.progressbar.CircleProgressBar
import com.example.streetpotholefinder.accident.FirebaseAccident
import com.example.streetpotholefinder.accident.SerializedAccident
import com.example.streetpotholefinder.accident.SerializedIssues
import com.example.streetpotholefinder.accidentsList.AccidentsActivity
import com.example.streetpotholefinder.dataList.DataListVO
import com.example.streetpotholefinder.databinding.ActivityRecResultBinding
import com.example.streetpotholefinder.issue.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Duration


class RecResultActivity : AppCompatActivity() {

    //// GUI Component ////
    private lateinit var tvResultDate: TextView
    private lateinit var tvResultTime: TextView
    private lateinit var tvResultLength: TextView
    private lateinit var tvResultPotholeCnt: TextView
    private lateinit var tvResultCrackCnt: TextView
    private lateinit var btnRecResultUpload: LinearLayout
    private lateinit var btnRecResultBack: LinearLayout

    ///////////////////////

    lateinit var data: String
    private lateinit var prevActivityInfo: String
    private lateinit var userid: String
    private lateinit var auth: FirebaseAuth
    private var fbRef : String = ""

    val db = Firebase.database

    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var binding: ActivityRecResultBinding

    private lateinit var progressStreet: CircleProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecResultBinding.inflate(layoutInflater)

        setContentView(binding.root)
        tvResultDate = findViewById(R.id.ResultDate)
        tvResultTime = findViewById(R.id.ResultTime)
        tvResultLength = findViewById(R.id.ResultLength)
        tvResultPotholeCnt = findViewById(R.id.ResultPotholeCnt)
        tvResultCrackCnt = findViewById(R.id.ResultCrackCnt)

        btnRecResultUpload = findViewById(R.id.btnRecResultUpload)
        btnRecResultBack = findViewById(R.id.btnRecResultBack)

        prevActivityInfo = intent.getStringExtra("previousActivityInfo").toString()
        when (prevActivityInfo) {
            "CameraView" -> onCreateViewFromCarmera()
            "DataListAdapter" -> onCreateViewFromDataList()
        }

        val potholeBtn = findViewById<LinearLayout>(R.id.linearLayoutPortHole)
        potholeBtn.setOnClickListener {
            val intent = Intent(this, AccidentsActivity::class.java)
            intent.putExtra("Category", "pothole")
            intent.putExtra("previousActivityInfo",prevActivityInfo)
            intent.putExtra("dataRef", fbRef)
            startActivity(intent)
            Log.d("RecResultActivity", "onCreate: pothole $prevActivityInfo")
        }

        val crackBtn = findViewById<LinearLayout>(R.id.linearLayoutCrack)
        crackBtn.setOnClickListener {
            val intent = Intent(this, AccidentsActivity::class.java)
            intent.putExtra("Category", "crack")
            intent.putExtra("previousActivityInfo",prevActivityInfo)
            intent.putExtra("dataRef", fbRef)
            startActivity(intent)
        }

        btnRecResultUpload.setOnClickListener {
            findViewById<ConstraintLayout>(R.id.clayoutProgress).visibility = View.VISIBLE
            uploadResult()
        }

        // firebase setting
        firebaseStorage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        userid = auth.currentUser?.displayName ?: "devmode"

        val reRecordBtn = findViewById<LinearLayout>(R.id.btnRecResultBack)
        reRecordBtn.setOnClickListener{
            val cuDialogLyt = layoutInflater.inflate(R.layout.dialog_default_lyt, null)
            val build = AlertDialog.Builder(this).apply { setView(cuDialogLyt) }
            val dialog=build.create()

            dialog.apply {
                show()
                window?.setLayout(750, ActionBar.LayoutParams.WRAP_CONTENT)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

            cuDialogLyt.findViewById<TextView>(R.id.dialog_title).text="????????????????????????????"
            cuDialogLyt.findViewById<TextView>(R.id.dialog_sub).text="???????????? ?????? ???????????? ???????????? ????????????"
            cuDialogLyt.findViewById<TextView>(R.id.dialog_btn_y).apply {
                text = "???"
                setOnClickListener{
                    val intent = Intent(it.context, CameraView::class.java)
                    startActivity(intent)
                    Event.getInstance().resetData()
                }
            }
            cuDialogLyt.findViewById<TextView>(R.id.dialog_btn_n).apply {
                text = "?????????"
                setOnClickListener{
                    dialog.dismiss()
                }
            }
        }

        //??????????????????
        progressStreet = findViewById<CircleProgressBar>(R.id.progressStreet)
        findViewById<ConstraintLayout>(R.id.clayoutProgress).visibility = View.GONE
    }

    fun uploadResult() {
        // Convert Accident data.
        var serializedAccident = SerializedAccident()
        serializedAccident.importAccident(Event.getInstance().accident)


        var fireStore = Firebase.firestore.collection(userid)
        var issueDocument = fireStore.document("Issue_${userid}_${serializedAccident.recEndTime}")


        progressStreet.max = serializedAccident.potholes.size + serializedAccident.cracks.size

        // porthole
        issueDocument.collection("pothole")
        uploadFirebase("pothole", serializedAccident.potholes, issueDocument)

        // crack
        issueDocument.collection("crack")
        uploadFirebase("crack", serializedAccident.cracks, issueDocument)

        issueDocument.set(
            hashMapOf(
                "recStartTime" to serializedAccident.recStartTime.toString(),
                "recEndTime" to serializedAccident.recEndTime.toString(),
                "cntPothole" to serializedAccident.potholes.size,
                "cntCrack" to serializedAccident.cracks.size
            )
        )

        val dlg: AlertDialog.Builder = AlertDialog.Builder(
            this,
            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
        ).setTitle("Notify")
            .setMessage("Upload??????")
            .setPositiveButton("??????") { _, _ ->
            }
    }

    private fun uploadFirebase(
        name: String,
        dataList: ArrayList<SerializedIssues>,
        fbCollection: DocumentReference
    ) {
        for ((idx, issue) in dataList.withIndex()) {

            var image = issue.image
            var image_name = "$userid/images/${issue.issueTime}.jpg"
            val storageMetadata = StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .setCustomMetadata("GPS", issue.gpsInfo)
                .setCustomMetadata("Time", issue.issueTime.toString())
                .build()

            val storageReference: StorageReference =
                firebaseStorage.getReference(image_name)

            storageReference.putBytes(image, storageMetadata).addOnSuccessListener {
                Log.d("RecResultActivity", "uploadResult: success : image : ${image_name}")

                var event = FirebaseAccident(issue.issueTime.toString(), issue.gpsInfo, image_name)

                fbCollection.collection(name)
                    .document("issue${idx}")
                    .set(hashMapOf("$name$idx" to event))

                Log.d("RecResultActivity", "uploadResult: issue : ${event}")
                progressStreet.progress++

                if(progressStreet.progress == progressStreet.max)
                {
                    gotoMain()
                }

            }.addOnFailureListener {
                Log.d("RecResultActivity", "uploadResult: failure : ${it}")
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        when (prevActivityInfo) {
            "CameraView" -> gotoMain()
            "DataListAdapter" -> finish()
        }
    }

    private fun onCreateViewFromCarmera() {
        var currentEvent: Event = Event.getInstance()
        var recStartTime: LocalDateTime? = currentEvent.accident.recStartTime
        var recEndTime: LocalDateTime? = currentEvent.accident.recEndTime
        var recTime: Duration =
            recEndTime!!.toInstant(TimeZone.currentSystemDefault()) - recStartTime!!.toInstant(
                TimeZone.currentSystemDefault()
            )
        tvResultDate.text = recEndTime.toString()
        tvResultTime.text = recEndTime.toString()
        tvResultLength.text = recTime.toString()
        tvResultPotholeCnt.text = currentEvent.accident.potholes.size.toString()
        tvResultCrackCnt.text = currentEvent.accident.cracks.size.toString()
    }

    private fun onCreateViewFromDataList() {
        btnRecResultUpload.visibility = View.INVISIBLE
        btnRecResultBack.visibility = View.INVISIBLE

        val eventRef = intent.getSerializableExtra("ref") as DataListVO

        tvResultDate.text = eventRef.strStreetDate
        tvResultTime.text = eventRef.strStreetTime
        tvResultLength.text = eventRef.strRecordLength
        tvResultPotholeCnt.text = eventRef.strPotholeCnt
        tvResultCrackCnt.text = eventRef.strCrackCnt
        fbRef = eventRef.eventRef
    }

    /** Main?????? ???????????? ?????? */
    private fun gotoMain() {
        // Vibrate for 500 milliseconds
        var v: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v.vibrate(500)

        val dlg: AlertDialog.Builder = AlertDialog.Builder(
            this,
            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
        )
        dlg.setTitle("?????? ??????") //??????
        dlg.setMessage("??????????????? ???????????????.") // ?????????
        dlg.setPositiveButton("??????") { _, _ ->
            val intent = Intent(this, MainActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK//???????????? ????????????
            startActivity(intent)
            Event.getInstance().resetData()
            finish()
        }
        dlg.setNegativeButton("??????") { _, _ ->
            // nothing
        }
        dlg.show()
    }
}