package com.example.streetpotholefinder

//
//class DataListAdapter(context: Context, layout: Int, items: MutableList<DataListVO>): BaseAdapter() {
//
//    val context = context
//    val layout = layout
//    val items = items
//
//    val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//
//    override fun getCount(): Int = items.size
//
//    override fun getItem(p0: Int): DataListVO = items[p0]
//
//    override fun getItemId(p0: Int): Long = p0.toLong()
//
//    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
//
////        val p1 = p1 ?: inflater.inflate(layout, p2, false)
//
//        val p1 = p1 ?: inflater.inflate(layout, p2, false)
//        val StreetDate = p1.findViewById<TextView>(R.id.StreetDate)
//        val StreetTime = p1.findViewById<TextView>(R.id.StreetTime)
//        val RecordLength = p1.findViewById<TextView>(R.id.RecordLength)
//        val PotholeCnt = p1.findViewById<TextView>(R.id.PotholeCnt)
//        val CrackCnt = p1.findViewById<TextView>(R.id.CrackCnt)
//
//        // record
//        // length
//        return p1
//
//    }
//
//
//}

import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.DataListVO

class DataListAdapter (val dataList : MutableList<DataListVO>) : RecyclerView.Adapter<DataListAdapter.CustomViewHolder>()
{  private lateinit var context: Context



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataListAdapter.CustomViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_list_one_lyt, parent, false)
        return CustomViewHolder(view).apply {
             itemView.setOnClickListener {

                 var curpos : Int = adapterPosition
                 var intent = Intent(context, RecResultActivity::class.java)
                 intent.putExtra("number",curpos)
                 context.startActivity(intent)


             }

        }
    }



    override fun onBindViewHolder(holder: DataListAdapter.CustomViewHolder, position: Int) {



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