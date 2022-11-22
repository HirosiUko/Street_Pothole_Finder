package com.example.streetpotholefinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView

class SignUpActivity : AppCompatActivity() {

 //   lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val etJoinEmail = findViewById<EditText>(R.id.etJoinEmail)
        val etJoinPw1 = findViewById<EditText>(R.id.etJoinPw1)
        val etJoinPw2 = findViewById<EditText>(R.id.etJoinPw2)
        val btnJoinJoin = findViewById<TextView>(R.id.btnJoinJoin)
        val btnJoinLogin = findViewById<TextView>(R.id.btnJoinLogin)

  //      auth = Firebase.auth

        var isJoin = true

//        btnJoinJoin.setOnClickListener {
//            val email = etJoinEmail.text.toString()
//            val pw1 = etJoinPw1.text.toString()
//            val pw2 = etJoinPw2.text.toString()
//            if(email.isEmpty()) {
//                isJoin = false
//                Toast.makeText(this,"이메일을 입력하세요 ",Toast.LENGTH_SHORT).show()
//            }
//            if(pw1.isEmpty()) {
//                isJoin = false
//                Toast.makeText(this,"pw1을 입력하세요 ",Toast.LENGTH_SHORT).show()
//            }
//            if(pw2.isEmpty()) {
//                isJoin = false
//                Toast.makeText(this,"pw2을 입력하세요 ",Toast.LENGTH_SHORT).show()
//            }
//            if(!pw1.equals(pw2)){// ! -> 다르다
//                isJoin = false
//                Toast.makeText(this, "비밀번호가 일치하지 x 다시입력하셈ㅇ",Toast.LENGTH_SHORT).show()
//            }
//            if(pw1.length < 6){
//                isJoin = false
//                Toast.makeText(this, "비밀본ㅎ를 6자리 이상으로 입력해주새요 ",Toast.LENGTH_SHORT).show()
//            }
//            if(isJoin){
//                auth.createUserWithEmailAndPassword(email, pw1)
//                    .addOnCompleteListener(this){ task ->
//
//                        if(task.isSuccessful){
//                            // 회원가입 성공
//                            Toast.makeText(this, "회원가입성공 ",Toast.LENGTH_SHORT).show()
//                        }else{
//                            // 회원가입 실패
//                            Toast.makeText(this, "회원가입 실패",Toast.LENGTH_SHORT).show()
//                        }
//
//
//                    }



            }
        }
        // EditText에 있는 id,pw1,pw2를 가져온다 (저장)
        // EditText가 Null인지 확인
        // if(email.isEmpty()) :isJoin = false(firebase저장x)
        // 토스트로 "email을 입력하세여"
        // pw1, pw2가 다르면 토스트로 "비밀번호를 똑같이 입력해주세요" -> isJoin =false
        // password가 6글자 이상인지 확인
        // 변수명.length -> "비밀번호를 6자리 이상으로 입력해주세여"
        // isJoin -> True라면 firebase에 이메일,비밀번호 전송



//
//        btnJoinLogin.setOnClickListener {
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//        }
//
//
//


