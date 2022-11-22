package com.example.streetpotholefinder

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.DataListVO
import com.example.streetpotholefinder.R.id.data_list_view
import com.example.streetpotholefinder.databinding.ActivityDataListBinding

class DataListActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityDataListBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_list)


        val rv= findViewById<RecyclerView>(data_list_view)
        //데이터목록 리스트 선언
        var ContentList = mutableListOf<DataListVO>()

        ContentList.add(DataListVO("2022년 11월 15일","오후 2시 40분", "00:29:23","30","14"))
        ContentList.add(DataListVO("2022년 10월 25일","오전 8시 18분", "00:05:22","5","3"))

        ContentList.add(DataListVO("2022년 10월 23일","오후 3시 22분", "00:04:03","13","27"))
        ContentList.add(DataListVO("2022년 9월 17일","오전 10시 33분", "00:16:45","4","19"))

        //데이터목록 리스트 어댑터
//        val Adapter = DataListAdapter(this,R.layout.data_list_one_lyt, ContentList)
//
//        val datalistView = findViewById<ListView>(data_list_view)
//        datalistView.adapter = Adapter

        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv.setHasFixedSize(true)

        rv.adapter = DataListAdapter(ContentList)

        rv.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        //데이터목록 각 리스트 클릭 시 이벤트
//        rv.onItemClickListener = AdapterView.OnItemClickListener{
//                parent, view, position, id->
//            val selection = parent.getItemAtPosition(position) as DataListVO
//            //토스트로 작동하는지 확인
//            Toast.makeText(this,"${selection.StreetDate}", Toast.LENGTH_SHORT).show()
//        }

    }
}