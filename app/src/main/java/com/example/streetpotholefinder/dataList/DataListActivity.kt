package com.example.streetpotholefinder.dataList

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetpotholefinder.R
import com.example.streetpotholefinder.R.id.data_list_view
import com.example.streetpotholefinder.RecResultActivity
import com.example.streetpotholefinder.databinding.ActivityDataListBinding

class DataListActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityDataListBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_list)


        val rv = findViewById<RecyclerView>(data_list_view)
        //데이터목록 리스트 선언
        var ContentList = mutableListOf<DataListVO>()

        ContentList.add(DataListVO("2022년 11월 15일", "오후 2시 40분", "00:29:23", "30", "14"))
        ContentList.add(DataListVO("2022년 10월 25일", "오전 8시 18분", "00:05:22", "5", "3"))

        ContentList.add(DataListVO("2022년 10월 23일", "오후 3시 22분", "00:04:03", "13", "27"))
        ContentList.add(DataListVO("2022년 9월 17일", "오전 10시 33분", "00:16:45", "4", "19"))

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

class DataListAdapter(val dataList: MutableList<DataListVO>) :
    RecyclerView.Adapter<DataListAdapter.CustomViewHolder>() {
    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.data_list_one_lyt, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {

                var curpos: Int = adapterPosition
                var intent = Intent(context, RecResultActivity::class.java)
                intent.putExtra("previousActivityInfo", "DataListAdapter")
                intent.putExtra("number", curpos)
                context.startActivity(intent)
            }
        }
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {


        holder.RecordLength.text = dataList.get(position).RecordLength
        holder.StreetDate.text = dataList.get(position).StreetDate
        holder.StreetTime.text = dataList.get(position).StreetTime
        holder.PotholeCnt.text = dataList.get(position).PotholeCnt
        holder.CrackCnt.text = dataList.get(position).CrackCnt


    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val StreetDate = itemView.findViewById<TextView>(R.id.StreetDate)
        val StreetTime = itemView.findViewById<TextView>(R.id.StreetTime)
        val RecordLength = itemView.findViewById<TextView>(R.id.RecordLength)
        val PotholeCnt = itemView.findViewById<TextView>(R.id.PotholeCnt)
        val CrackCnt = itemView.findViewById<TextView>(R.id.CrackCnt)


    }

}

class DataListVO(
    val StreetDate: String,
    val StreetTime: String,
    val RecordLength: String,
    val PotholeCnt: String,
    val CrackCnt: String
) {

}