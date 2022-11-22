package com.example.streetpotholefinder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.DataListVO
import kotlinx.coroutines.NonDisposableHandle.parent

class DataListAdapter(context: Context, layout: Int, items: MutableList<DataListVO>): BaseAdapter() {

    val context = context
    val layout = layout
    val items = items

    val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = items.size

    override fun getItem(p0: Int): DataListVO = items[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

//        val p1 = p1 ?: inflater.inflate(layout, p2, false)

        val p1 = p1 ?: inflater.inflate(layout, p2, false)
        val StreetDate = p1.findViewById<TextView>(R.id.StreetDate)
        val StreetTime = p1.findViewById<TextView>(R.id.StreetTime)
        val RecordLength = p1.findViewById<TextView>(R.id.RecordLength)
        val PotholeCnt = p1.findViewById<TextView>(R.id.PotholeCnt)
        val CrackCnt = p1.findViewById<TextView>(R.id.CrackCnt)

        // record
        // length
        return p1

    }


}