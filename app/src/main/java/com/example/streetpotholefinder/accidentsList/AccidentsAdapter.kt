package com.example.streetpotholefinder.accidentsList

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.streetpotholefinder.R
import com.squareup.picasso.Picasso

class AccidentsAdapter(val dataList: MutableList<accidentVO>) :
    RecyclerView.Adapter<AccidentsAdapter.CustomViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.accidentlist, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                var gpsInfo = it.findViewById<TextView>(R.id.tvGps).text
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:$gpsInfo?q=$gpsInfo(사고위치)")).apply {
                    `package` = "com.google.android.apps.maps"
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        Picasso.get().load(dataList.get(position).imgUri).into(holder.imgpot)
//        holder.imgpot.setImageBitmap()
        holder.tvGps.text = dataList.get(position).gps
        holder.tvtime.text = dataList.get(position).time
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgpot = itemView.findViewById<ImageView>(R.id.imgpot)
        val tvGps = itemView.findViewById<TextView>(R.id.tvGps)
        val tvtime = itemView.findViewById<TextView>(R.id.tvTime)
    }
}