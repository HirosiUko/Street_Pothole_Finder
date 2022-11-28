package com.example.streetpotholefinder

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// context 아직 해결 안됨 : Activity로 부터 받아와야함
// MainActivity에서 DBManager의 객체를 생성하면서
// 전달인자로 context 보내기
// DBManager에서 context를 다시 DBHelper를 생성하면서
// 전달인자로 context를 보낸 상태
class DBHelper(context: Context):
    SQLiteOpenHelper(context, "accidentDatabase.db",null,1) {

    // 상속 관계 (:)
    // 부모한테 생성자가 있으면, 반드시 생성자 오버라이딩 해줘야함함

    // SQLiteOpenHelper 생성자로 보내줘야할 전달 인자
    // 1. context : 화면 정보
    // 2. name : dataBase 파일 이름
    // 3. factory : null
    // 4. version : DB버전 1version

    // Dabase파일을 생성하고, DB연결하고, 테이블생성 ㅋ
    override fun onCreate(p0: SQLiteDatabase?) {
        // 테이블 생성하는 메서드
        // SQLiteDataBase : 명령어를 전송할 수 있는 통로
        // 이 통로를 통해서 Create문을 전송하기
        // String -> varchar
        var sql = "create table chat(msg varchar(50))"
        p0?.execSQL(sql)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        // 혹시라도 Database 버전이 업데이트가 되면 호출되는 메서드
        // 개발자 입장 : Table 구조가 바뀌면 호출
        // table을 수정하고 싶은데 어플리케이션이 이미설치가 돼있으면 어풀 삭제를 진행하는게 아니라
        // 자동으로 업데이트 될 수 있도록 만드는 메서드
        var sql = "drop table chat"
        p0?.execSQL(sql) // 기존에 있던 테이블 삭제
        onCreate(p0) // 바뀐 create문을 가젹와서 실행
    }


}