package com.example.streetpotholefinder

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class MypageActivity : AppCompatActivity() {

    private lateinit var circleImg : CircleImageView
    private lateinit var auth : FirebaseAuth
    private val TAG = "MypageActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        auth = FirebaseAuth.getInstance()

        circleImg = findViewById(R.id.circleImageView)
        var imageUri = auth.currentUser?.photoUrl
        if (imageUri != null)
        {
            Picasso.get().load(imageUri).into(circleImg)
        }

        findViewById<TextView>(R.id.tvUserName).text = auth.currentUser?.displayName
        findViewById<TextView>(R.id.tvUserEmail).text = auth.currentUser?.email

        val ivBackBtn = findViewById<LinearLayout>(R.id.ivMypageBackBtn)
        ivBackBtn.setOnClickListener{
            onBackPressed()
            finish()
        }
    }
}